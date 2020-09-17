package com.example.demo.nettyclient.message.receive;

import com.example.demo.nettyclient.message.Receive;
import com.example.demo.nettyclient.message.ReceiveType;
import com.example.demo.nettyserver.util.Constants;
import io.netty.buffer.ByteBuf;

public class JoinGroupReceive implements Receive {

    private byte code;
    private String codeMsg;

    public byte getCode()
    {
        return code;
    }

    public void setCode(byte code)
    {
        this.code = code;
    }

    public String getCodeMsg()
    {
        return codeMsg;
    }

    public void setCodeMsg(String codeMsg)
    {
        this.codeMsg = codeMsg;
    }

    @Override
    public ReceiveType type()
    {
        return ReceiveType.JOIN_GROUP;
    }

    @Override
    public void readFromBuf(ByteBuf buf) {
        code = buf.readByte();
        int length = buf.readInt();
        byte[] content = new byte[length];
        buf.readBytes(content);
        codeMsg = new String(content, Constants.CHARSET);
    }
}
