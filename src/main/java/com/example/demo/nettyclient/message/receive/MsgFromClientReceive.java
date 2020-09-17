package com.example.demo.nettyclient.message.receive;

import com.example.demo.nettyclient.message.Receive;
import com.example.demo.nettyclient.message.ReceiveType;
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
    public void readFromBuf(ByteBuf buf) {
        byte[] bytes = new byte[buf.readInt()];
        buf.readBytes(bytes);
        clientName = new String(bytes, Constants.CHARSET);
        bytes = new byte[buf.readInt()];
        buf.readBytes(bytes);
        msg = new String(bytes, Constants.CHARSET);
    }
}
