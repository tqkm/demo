package com.example.demo.nettyserver.message.receive;

import com.example.demo.nettyserver.message.Receive;
import com.example.demo.nettyserver.message.ReceiveType;
import io.netty.buffer.ByteBuf;

public class HeartBeatReceive implements Receive {

    @Override
    public ReceiveType type()
    {
        return ReceiveType.HEART_BEAT;
    }

    @Override
    public void writeToMessage(ByteBuf buf)
    {
        buf.writeByte(7);
    }

}
