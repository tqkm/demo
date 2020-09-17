package com.example.demo.nettyserver.handler;

import com.example.demo.nettyserver.message.Command;
import com.example.demo.nettyserver.message.CommandType;
import com.example.demo.nettyserver.message.command.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

public class CommandDecoder extends ChannelInboundHandlerAdapter {

    private static final Charset CHARSET = Charset.forName("UTF-8");

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf)msg;
        byte b = buf.readByte();
        CommandType commandType = CommandType.value(b);
        Command command;
        switch (commandType) {
            case REGISTER: {
                String clientName = readString(buf);
                command = new RegisterCommand();
                ((RegisterCommand)command).setRegisterName(clientName);
                break;
            }
            case LOGIN:
            {
                String clientName = readString(buf);
                command = new LoginCommand();
                ((LoginCommand)command).setLoginName(clientName);
                break;
            }
            case SEND_TO_CLIENT:
            {
                String destClientName = readString(buf);
                String content        = readString(buf);
                command = new SendToClientCommand();
                ((SendToClientCommand)command).setClientName(destClientName);
                ((SendToClientCommand)command).setMsg(content);
                break;
            }
            case CREATE_GROUP:
            {
                String groupName = readString(buf);
                command = new CreateGroupCommand();
                ((CreateGroupCommand)command).setGroupName(groupName);
                break;
            }
            case JOIN_GROUP:
            {
                String groupName = readString(buf);
                command = new JoinGroupCommand();
                ((JoinGroupCommand)command).setGroupName(groupName);
                break;
            }
            case SEND_TO_GROUP:
            {
                String groupName = readString(buf);
                String content   = readString(buf);
                command = new SendToGroupCommand();
                ((SendToGroupCommand)command).setGroupName(groupName);
                ((SendToGroupCommand)command).setMsg(content);
                break;
            }
            case HEART_BEAT:
            {
                command = new HeartBeatCommand();
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + commandType);
        }
        buf.release();
        ctx.fireChannelRead(command);
    }

    private String readString(ByteBuf buf) {
        int length = buf.readInt();
        byte[] content = new byte[length];
        buf.readBytes(content);
        return new String(content, CHARSET);
    }
}
