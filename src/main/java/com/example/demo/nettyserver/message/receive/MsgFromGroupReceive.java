package com.example.demo.nettyserver.message.receive;

import com.example.demo.nettyserver.message.Receive;
import com.example.demo.nettyserver.message.ReceiveType;
import com.example.demo.nettyserver.util.Constants;
import io.netty.buffer.ByteBuf;

public class MsgFromGroupReceive implements Receive {
    private String groupName;
    private String msg;
    private String clientName;

    public String getClientName()
    {
        return clientName;
    }

    public void setClientName(String clientName)
    {
        this.clientName = clientName;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public String getGroupName()
    {
        return groupName;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    @Override
    public ReceiveType type()
    {
        return ReceiveType.MSG_FROM_GROUP;
    }

    @Override
    public void writeToMessage(ByteBuf buf)
    {
        buf.writeByte(6);
        byte[] groupNameBytes = groupName.getBytes(Constants.CHARSET);
        buf.writeInt(groupNameBytes.length).writeBytes(groupNameBytes);
        byte[] clientNameBytes = clientName.getBytes(Constants.CHARSET);
        buf.writeInt(clientNameBytes.length).writeBytes(clientNameBytes);
        byte[] msgBytes = msg.getBytes(Constants.CHARSET);
        buf.writeInt(msgBytes.length).writeBytes(msgBytes);
    }
}
