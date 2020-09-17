package com.example.demo.nettyclient.message.command;

import com.example.demo.nettyclient.message.Command;
import com.example.demo.nettyclient.message.CommandType;
import io.netty.buffer.ByteBuf;

public class HeartBeatCommand implements Command {

    @Override
    public CommandType type() {
        return CommandType.HEART_BEAT;
    }

    @Override
    public void writeToBuf(ByteBuf buf) {
        buf.writeByte(7);
    }
}
