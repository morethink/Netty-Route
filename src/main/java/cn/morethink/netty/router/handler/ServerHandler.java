package cn.morethink.netty.router.handler;


import cn.morethink.netty.router.Action;
import cn.morethink.netty.router.HttpLabel;
import cn.morethink.netty.router.HttpRouter;
import cn.morethink.netty.router.RequestBody;
import cn.morethink.netty.router.util.GeneralResponse;
import cn.morethink.netty.router.util.JsonUtil;
import cn.morethink.netty.router.util.ParamParser;
import cn.morethink.netty.router.util.ResponseUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.lang.annotation.Annotation;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;


/**
 * @author 李文浩
 * @date 2018/9/5
 */
@Slf4j
public class ServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final String DELIMITER = "?";

    HttpRouter httpRouter;

    public ServerHandler(HttpRouter httpRouter) {
        this.httpRouter = httpRouter;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) {
        String uri = request.uri();
        GeneralResponse generalResponse;
        if (uri.contains(DELIMITER)) {
            uri = uri.substring(0, uri.indexOf(DELIMITER));
        }
        //根据不同的请求API做不同的处理(路由分发)
        Action action = httpRouter.getRoute(new HttpLabel(uri, request.method()));
        if (action != null) {
            String s = request.uri();
            if (request.headers().get(HttpHeaderNames.CONTENT_TYPE.toString()).equals(HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())) {
                s = s + "&" + request.content().toString(StandardCharsets.UTF_8);
            }
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(s);
            Map<String, List<String>> parameters = queryStringDecoder.parameters();
            List<Class> list = action.getParamsClassList();
            Object[] objects = new Object[list.size()];
            for (int i = 0; i < list.size(); i++) {
                Class c = list.get(i);
                Annotation[] parameterAnnotation = action.getMethod().getParameterAnnotations()[i];
                if (parameterAnnotation.length > 0) {
                    for (int j = 0; j < parameterAnnotation.length; j++) {
                        if (parameterAnnotation[j].annotationType() == RequestBody.class &&
                                request.headers().get(HttpHeaderNames.CONTENT_TYPE.toString()).equals(HttpHeaderValues.APPLICATION_JSON.toString())) {
                            objects[i] = JsonUtil.fromJson(request, c);
                        }
                    }
                } else if (c.isArray()) {
                    String paramName = action.getMethod().getParameters()[i].getName();
                    List<String> paramList = parameters.get(paramName);
                    if (CollectionUtils.isNotEmpty(paramList)) {
                        objects[i] = ParamParser.INSTANCE.parseArray(c.getComponentType(), paramList);
                    }
                } else {
                    String paramName = action.getMethod().getParameters()[i].getName();
                    List<String> paramList = parameters.get(paramName);
                    if (CollectionUtils.isNotEmpty(paramList)) {
                        objects[i] = ParamParser.INSTANCE.parseValue(c, paramList.get(0));
                    } else {
                        objects[i] = ParamParser.INSTANCE.parseValue(c, null);
                    }
                }
            }
            ResponseUtil.response(ctx, HttpUtil.isKeepAlive(request), action.call(objects));
        } else {
            //错误处理
            generalResponse = new GeneralResponse(HttpResponseStatus.BAD_REQUEST, "请检查你的请求方法及url", null);
            ResponseUtil.response(ctx, HttpUtil.isKeepAlive(request), generalResponse);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
        log.warn("{}", e);
        ResponseUtil.response(ctx, false, new GeneralResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR, String.format("Internal Error: %s", ExceptionUtils.getRootCause(e)), null));
        ctx.close();
    }
}
