package cn.morethink.netty.handler;

import cn.morethink.netty.router.HttpRouter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author 李文浩
 * @date 2018/9/5
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    HttpRouter httpRouter;

    public ServerInitializer(HttpRouter httpRouter) {
        this.httpRouter = httpRouter;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
        //HTTP 服务的解码器
        p.addLast(new HttpServerCodec());
        //HTTP 消息的合并处理
        p.addLast(new HttpObjectAggregator(10 * 1024));
        //自己写的服务器逻辑处理
        p.addLast(new ServerHandler(httpRouter));
    }
}
