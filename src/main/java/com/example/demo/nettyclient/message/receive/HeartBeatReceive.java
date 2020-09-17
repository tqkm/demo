package com.example.demo.nettyclient.message.receive;

import com.example.demo.nettyclient.message.Receive;
import com.example.demo.nettyclient.message.ReceiveType;
import io.netty.buffer.ByteBuf;

public class HeartBeatReceive implements Receive {

    @Override
    public ReceiveType type()
    {
        return ReceiveType.HEART_BEAT;
    }

    @Override
    public void readFromBuf(ByteBuf buf) {

    }
}
