package com.example.demo.nettyclient.message;

import io.netty.buffer.ByteBuf;

public interface Receive {

    ReceiveType type();

    void readFromBuf(ByteBuf buf);
}
