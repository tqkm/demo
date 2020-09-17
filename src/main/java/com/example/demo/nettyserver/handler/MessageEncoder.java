package com.example.demo.nettyserver.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class MessageEncoder extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf buf = (ByteBuf)msg;
        int oldWriteIndex = buf.writerIndex();
        int length = oldWriteIndex - 4;
        buf.writerIndex(0).writeInt(length).writerIndex(oldWriteIndex);
        ctx.write(msg, promise);
    }
}
