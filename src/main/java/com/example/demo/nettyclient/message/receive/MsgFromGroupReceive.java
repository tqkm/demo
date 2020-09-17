package com.example.demo.nettyclient.message.receive;

import com.example.demo.nettyclient.message.Receive;
import com.example.demo.nettyclient.message.ReceiveType;
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
    public void readFromBuf(ByteBuf buf) {
        byte[] bytes = new byte[buf.readInt()];
        buf.readBytes(bytes);
        groupName = new String(bytes,Constants.CHARSET);
        bytes = new byte[buf.readInt()];
        buf.readBytes(bytes);
        clientName = new String(bytes,Constants.CHARSET);
        bytes = new byte[buf.readInt()];
        buf.readBytes(bytes);
        msg = new String(bytes,Constants.CHARSET);

    }
}
