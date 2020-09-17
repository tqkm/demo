package com.example.demo.nettyserver.message.command;

import com.example.demo.nettyserver.message.Command;
import com.example.demo.nettyserver.message.CommandType;

public class CreateGroupCommand implements Command {

    private String groupName;

    public String getGroupName()
    {
        return groupName;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    @Override
    public CommandType type() {
        return CommandType.CREATE_GROUP;
    }
}
