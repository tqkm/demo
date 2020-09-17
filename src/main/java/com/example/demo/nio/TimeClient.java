package com.example.demo.nio;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class TimeClient {

    public static void main(String[] args) {
        for (int i = 0; i < 2; i++) {
            new Thread(new MyThread()).start();
        }
    }

    static class MyThread implements Runnable {
        @Override
        public void run() {
            connect("localhost", 9898);
        }

        private void connect(String host, int port) {
            EventLoopGroup group = new NioEventLoopGroup();
            try {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
                                /*socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024)).
                                        addLast(new StringDecoder()).addLast(new TimeClientHandler());*/
                                socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter)).
                                        addLast(new StringDecoder()).addLast(new TimeClientHandler());
                            }
                        });
                ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
                System.out.println(Thread.currentThread().getName() + ",客户端发起异步连接..........");
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                group.shutdownGracefully();
            }
        }
    }

}
