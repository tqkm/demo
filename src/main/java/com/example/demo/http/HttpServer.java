package com.example.demo.http;

import com.example.demo.http.handler.DownloadHandler;
import com.example.demo.http.handler.DownloadHandler2;
import com.example.demo.http.handler.ListFileHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.io.File;


public class HttpServer {

    public void start() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(new NioEventLoopGroup(1), new NioEventLoopGroup()).channel(NioServerSocketChannel.class).
                childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new HttpRequestDecoder()).addLast(new HttpResponseEncoder()).
                                addLast(new HttpObjectAggregator(1024)).
                                addLast(new ListFileHandler(new File("c:/film"))).
                                addLast(new DownloadHandler(new File("c:/film")));
                    }
                });
        ChannelFuture bind = bootstrap.bind(80);
        bind.sync();
        bind.channel().closeFuture().sync();
    }

    public static void main(String[] args) throws InterruptedException {
        new HttpServer().start();
    }

}
