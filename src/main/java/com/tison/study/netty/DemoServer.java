package com.tison.study.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;



/**
 * @author Tian Guangxin
 * @date 2017/03/08
 * @since 1.0
 */
public class DemoServer {

    public void bind(int port) throws InterruptedException {
        EventLoopGroup bossGrroup = new NioEventLoopGroup();
        EventLoopGroup workerGrroup = new NioEventLoopGroup();

        try {
            ServerBootstrap boot = new ServerBootstrap();
            boot.group(bossGrroup,workerGrroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childHandler(new ChildChannelHandler());

            ChannelFuture future = boot.bind(port).sync();
            future.channel().closeFuture().sync();
        } finally {
            bossGrroup.shutdownGracefully();
            workerGrroup.shutdownGracefully();
        }

    }


    private class ChildChannelHandler extends ChannelInitializer<SocketChannel>{

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new DemoServerHandler());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 7777;
        if(args != null && args.length > 0){
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {

            }
        }
        new DemoServer().bind(port);
    }




}
