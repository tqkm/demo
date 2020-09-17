package com.example.demo.nettyserver.message.command;

import com.example.demo.nettyserver.message.Command;
import com.example.demo.nettyserver.message.CommandType;

public class SendToGroupCommand implements Command {

    private String groupName;
    private String msg;

    public String getGroupName()
    {
        return groupName;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }


    @Override
    public CommandType type() {
        return CommandType.SEND_TO_GROUP;
    }
}
