package com.example.demo.nettyclient.handler.commandprocessor;

import com.example.demo.nettyclient.message.Receive;
import com.example.demo.nettyclient.message.ReceiveType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.EnumMap;

public class ReceiveDecoder extends ChannelInboundHandlerAdapter {

    private EnumMap<ReceiveType, Class<? extends Receive>> enumMap;

    public ReceiveDecoder(EnumMap<ReceiveType, Class<? extends Receive>> enumMap) {
        this.enumMap = enumMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buffer = (ByteBuf)msg;
        byte b = buffer.readByte();
        ReceiveType receiveType = ReceiveType.value(b);
        Class<? extends Receive> aClass = enumMap.get(receiveType);
        Receive receive = aClass.newInstance();
        receive.readFromBuf(buffer);
        ctx.fireChannelRead(receive);
    }
}
