package com.example.demo.nettyclient.message.command;

import com.example.demo.nettyclient.message.Command;
import com.example.demo.nettyclient.message.CommandType;
import com.example.demo.nettyclient.util.Constants;
import io.netty.buffer.ByteBuf;

public class SendToClientCommand implements Command {

    private String clientName;
    private String msg;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public CommandType type() {
        return CommandType.SEND_TO_CLIENT;
    }

    @Override
    public void writeToBuf(ByteBuf buf) {
        buf.writeByte(3);
        byte[] nameBytes = clientName.getBytes(Constants.CHARSET);
        buf.writeInt(nameBytes.length);
        buf.writeBytes(nameBytes);
        byte[] msgBytes = msg.getBytes(Constants.CHARSET);
        buf.writeInt(msgBytes.length);
        buf.writeBytes(msgBytes);
    }
}
