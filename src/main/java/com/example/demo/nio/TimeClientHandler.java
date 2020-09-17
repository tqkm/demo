package com.example.demo.nio;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.atomic.AtomicInteger;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {

    private static AtomicInteger atomicInteger = new AtomicInteger();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 5; i++){
            //String reqMsg = "我是客户端" + i + Thread.currentThread().getName() + System.getProperty("line.separator");
            String reqMsg = "我是客户端" + i + Thread.currentThread().getName() + "$_";
            byte[] bytes = reqMsg.getBytes("UTF-8");
            ByteBuf buffer = Unpooled.buffer(bytes.length);
            buffer.writeBytes(bytes);
            ctx.writeAndFlush(buffer);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        /*ByteBuf buf = (ByteBuf)msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        System.out.println(Thread.currentThread().getName() + ",Server return Message：" + body);
        ctx.close();*/
        String body = (String)msg;
        System.out.println((atomicInteger.addAndGet(1)) + "---" + Thread.currentThread().getName() + ",Server return Message：" + body);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
