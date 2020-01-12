package cn.morethink.netty.demo;

import cn.morethink.netty.demo.controller.DemoController;
import cn.morethink.netty.router.HttpRouter;
import cn.morethink.netty.router.handler.RouterHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 李文浩
 * @date 2018/9/5
 */
@Slf4j
public final class Demo {

    public static final int PORT = 8080;


    public static void main(String[] args) throws Exception {
        HttpRouter httpRouter = new HttpRouter();
        httpRouter.addRouter(DemoController.class.getName());

        EventLoopGroup group = new NioEventLoopGroup(1);
        ServerBootstrap b = new ServerBootstrap();
        b.option(ChannelOption.SO_BACKLOG, 1024);
        b.group(group)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        //HTTP 服务的解码器
                        p.addLast(new HttpServerCodec());
                        //HTTP 消息的合并处理
                        p.addLast(new HttpObjectAggregator(10 * 1024));
                        //自己写的路由处理
                        p.addLast(new RouterHandler(httpRouter));
                    }
                });

        Channel ch = b.bind(PORT).sync().channel();
        log.info("http://127.0.0.1:{}/ start", PORT);
        ch.closeFuture().sync();
    }
}
