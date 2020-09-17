package com.example.demo.nettyserver.message.command;

import com.example.demo.nettyserver.message.Command;
import com.example.demo.nettyserver.message.CommandType;

public class HeartBeatCommand implements Command {

    @Override
    public CommandType type() {
        return CommandType.HEART_BEAT;
    }
}
