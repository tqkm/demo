package com.example.demo.nettyclient.message;

import io.netty.buffer.ByteBuf;

public interface Command {

    CommandType type();

    void writeToBuf(ByteBuf buf);

}
