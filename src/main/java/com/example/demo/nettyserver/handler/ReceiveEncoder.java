package com.example.demo.nettyserver.handler;

import com.example.demo.nettyserver.message.Receive;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class ReceiveEncoder extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        Receive receive = (Receive)msg;
        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writerIndex(4);
        receive.writeToMessage(buffer);
        ctx.write(buffer, promise);
    }
}
