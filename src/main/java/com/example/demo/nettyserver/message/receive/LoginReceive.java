package com.example.demo.nettyserver.message.receive;

import com.example.demo.nettyserver.message.Receive;
import com.example.demo.nettyserver.message.ReceiveType;
import com.example.demo.nettyserver.util.Constants;
import io.netty.buffer.ByteBuf;

public class LoginReceive implements Receive {

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
        return ReceiveType.LOGIN;
    }

    @Override
    public void writeToMessage(ByteBuf buf) {
        buf.writeByte(2);
        buf.writeByte(code);
        byte[] codeMsg = this.codeMsg.getBytes(Constants.CHARSET);
        buf.writeInt(codeMsg.length);
        buf.writeBytes(codeMsg);
    }
}
