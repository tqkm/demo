package com.example.demo.nio;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.atomic.AtomicInteger;

public class TimerServerHandler extends ChannelInboundHandlerAdapter {

    private static AtomicInteger atomicInteger = new AtomicInteger();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        /*ByteBuf buf = (ByteBuf)msg;
        byte[] reg = new byte[buf.readableBytes()];
        buf.readBytes(reg);
        String body = new String(reg, "utf-8");
        System.out.println(Thread.currentThread().getName() + ",The server receive  order : " + body);
        String respMsg = "I am Server，消息接收 success!";
        ByteBuf byteBuf = Unpooled.copiedBuffer(respMsg.getBytes());
        ctx.write(byteBuf);*/
        String body = (String)msg;
        System.out.println((atomicInteger.addAndGet(1)) + "--->" + Thread.currentThread().getName() + ",The server receive  order : " + body);
        //String respMsg = "I am Server,消息接受success!" + Thread.currentThread().getName() + System.getProperty("line.separator");
        String respMsg = body + "$_";
        ByteBuf byteBuf = Unpooled.copiedBuffer(respMsg.getBytes());
        ctx.writeAndFlush(byteBuf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("-----客户端关闭:" + ctx.channel().remoteAddress());
        ctx.close();
    }
}
