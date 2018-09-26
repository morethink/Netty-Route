package cn.morethink.netty;

import cn.morethink.netty.handler.ServerInitializer;
import cn.morethink.netty.router.HttpRouter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 李文浩
 * @date 2018/9/5
 */
@Slf4j
public final class Server {

    public static final int PORT = 8080;


    public static void main(String[] args) throws Exception {
        HttpRouter httpRouter = new HttpRouter();
        httpRouter.addRouter("cn.morethink.netty.controller.DemoController");

        EventLoopGroup group = new NioEventLoopGroup(1);
        ServerBootstrap b = new ServerBootstrap();
        b.option(ChannelOption.SO_BACKLOG, 1024);
        b.group(group)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ServerInitializer(httpRouter));

        Channel ch = b.bind(PORT).sync().channel();
        log.info("http://127.0.0.1:{}/ start", PORT);
        ch.closeFuture().sync();
    }
}
