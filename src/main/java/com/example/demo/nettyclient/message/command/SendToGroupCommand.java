package com.example.demo.nettyclient.message.command;

import com.example.demo.nettyclient.message.Command;
import com.example.demo.nettyclient.message.CommandType;
import com.example.demo.nettyclient.util.Constants;
import io.netty.buffer.ByteBuf;

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

    @Override
    public void writeToBuf(ByteBuf buf) {
        buf.writeByte(6);
        byte[] groupNameBytes = groupName.getBytes(Constants.CHARSET);
        buf.writeInt(groupNameBytes.length);
        buf.writeBytes(groupNameBytes);
        byte[] msgBytes = msg.getBytes(Constants.CHARSET);
        buf.writeInt(msgBytes.length);
        buf.writeBytes(msgBytes);
    }
}
