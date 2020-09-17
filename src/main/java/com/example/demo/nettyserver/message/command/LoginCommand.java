package com.example.demo.nettyserver.message.command;

import com.example.demo.nettyserver.message.Command;
import com.example.demo.nettyserver.message.CommandType;

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
}
