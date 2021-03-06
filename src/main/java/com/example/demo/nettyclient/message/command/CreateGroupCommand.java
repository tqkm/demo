package com.example.demo.nettyclient.message.command;

import com.example.demo.nettyclient.message.Command;
import com.example.demo.nettyclient.message.CommandType;
import com.example.demo.nettyclient.util.Constants;
import io.netty.buffer.ByteBuf;

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

    @Override
    public void writeToBuf(ByteBuf buf) {
        buf.writeByte(4);
        byte[] groupNameBytes = groupName.getBytes(Constants.CHARSET);
        buf.writeInt(groupNameBytes.length);
        buf.writeBytes(groupNameBytes);
    }
}
