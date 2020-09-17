package com.example.demo.nettyclient.message.command;

import com.example.demo.nettyclient.message.Command;
import com.example.demo.nettyclient.message.CommandType;
import com.example.demo.nettyclient.util.Constants;
import io.netty.buffer.ByteBuf;

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

    @Override
    public void writeToBuf(ByteBuf buf) {
        buf.writeByte(1);
        byte[] registerNameBytes = registerName.getBytes(Constants.CHARSET);
        buf.writeInt(registerNameBytes.length);
        buf.writeBytes(registerNameBytes);
    }
}
