package com.example.demo.nettyclient.message.command;

import com.example.demo.nettyclient.message.Command;
import com.example.demo.nettyclient.message.CommandType;
import com.example.demo.nettyclient.util.Constants;
import io.netty.buffer.ByteBuf;

public class LoginCommand implements Command {

    private String loginName;

    public String getLoginName()
    {
        return loginName;
    }

    public void setLoginName(String loginName)
    {
        this.loginName = loginName;
    }

    @Override
    public CommandType type() {
        return CommandType.LOGIN;
    }

    @Override
    public void writeToBuf(ByteBuf buf) {
        buf.writeByte(2);
        byte[] loginNameBytes = loginName.getBytes(Constants.CHARSET);
        buf.writeInt(loginNameBytes.length);
        buf.writeBytes(loginNameBytes);
    }
}
