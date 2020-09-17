package com.example.demo.nettyserver.message;

import io.netty.buffer.ByteBuf;

public interface Receive {

    ReceiveType type();

    void writeToMessage(ByteBuf buf);

}
