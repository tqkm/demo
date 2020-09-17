package com.example.demo.nio;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class TimeServer {

    public static void main(String[] args) {
        int port = 9898;
        new TimeServer().bind(port);
    }

    public void bind(int port) {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss, worker).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ChildChannelHandler());
        ChannelFuture sync = bootstrap.bind(port).sync();
        System.out.println(Thread.currentThread().getName() + ",服务器开始监听端口，等待客户端连接.........");
        sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel sc) throws Exception {
            //sc.pipeline().addLast(new LineBasedFrameDecoder(1024)).addLast(new StringDecoder()).addLast(new TimerServerHandler());
            ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
            sc.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter)).addLast(new StringDecoder()).
                    addLast(new TimerServerHandler());
        }
    }

}
