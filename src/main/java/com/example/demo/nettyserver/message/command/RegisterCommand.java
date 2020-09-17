package com.example.demo.nettyserver.message.command;

import com.example.demo.nettyserver.message.Command;
import com.example.demo.nettyserver.message.CommandType;

public class RegisterCommand implements Command {

    private String registerName;

    public String getRegisterName()
    {
        return registerName;
    }

    public void setRegisterName(String registerName)
    {
        this.registerName = registerName;
    }

    @Override
    public CommandType type() {
        return CommandType.REGISTER;
    }
}
