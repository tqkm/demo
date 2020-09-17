package com.example.demo.nettyclient.handler.commandprocessor;

import com.example.demo.nettyclient.message.Command;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class CommandEncoder extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof Command) {
            Command command = (Command) msg;
            ByteBuf buffer  = ctx.alloc().buffer();
            buffer.writerIndex(4);
            command.writeToBuf(buffer);
            int writerIndex = buffer.writerIndex();
            //消息协议前四个字节是整型变量，需要计算报文体长度
            buffer.writerIndex(0).writeInt(writerIndex - 4).writerIndex(writerIndex);
            ctx.write(buffer, promise);
        } else {
            throw new IllegalArgumentException();
        }
    }
}
