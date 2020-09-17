package com.example.demo.nettyserver.message.receive;

import com.example.demo.nettyserver.message.Receive;
import com.example.demo.nettyserver.message.ReceiveType;
import com.example.demo.nettyserver.util.Constants;
import io.netty.buffer.ByteBuf;

public class RegisterReceive implements Receive {

    private byte code;
    private String codeMsg;

    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }

    public String getCodeMsg() {
        return codeMsg;
    }

    public void setCodeMsg(String codeMsg) {
        this.codeMsg = codeMsg;
    }

    @Override
    public ReceiveType type() {
        return ReceiveType.REGISTER;
    }

    @Override
    public void writeToMessage(ByteBuf buf) {
        buf.writeByte(1);
        buf.writeByte(code);
        byte[] msgBytes = codeMsg.getBytes(Constants.CHARSET);
        buf.writeInt(msgBytes.length);
        buf.writeBytes(msgBytes);
    }
}
