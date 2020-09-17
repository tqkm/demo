package com.example.demo.nettyserver.message.receive;

import com.example.demo.nettyserver.message.Receive;
import com.example.demo.nettyserver.message.ReceiveType;
import com.example.demo.nettyserver.util.Constants;
import io.netty.buffer.ByteBuf;

public class MsgFromClientReceive implements Receive {

    private String clientName;
    private String msg;

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

    @Override
    public ReceiveType type() {
        return ReceiveType.MSG_FROM_CLIENT;
    }

    @Override
    public void writeToMessage(ByteBuf buf) {
        buf.writeByte(3);
        byte[] clientNameBytes = clientName.getBytes(Constants.CHARSET);
        buf.writeInt(clientNameBytes.length).writeBytes(clientNameBytes);
        byte[] msgBytes = msg.getBytes(Constants.CHARSET);
        buf.writeInt(msgBytes.length).writeBytes(msgBytes);
    }
}
