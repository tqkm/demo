package com.example.demo.nettyserver.handler;

import com.example.demo.nettyserver.message.Command;
import com.example.demo.nettyserver.message.CommandType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.EnumMap;

public class CommandHandler extends ChannelInboundHandlerAdapter {

    private volatile EnumMap<CommandType, CommandProcessor> processors;

    public CommandHandler(EnumMap<CommandType, CommandProcessor> processors) {
        this.processors = processors;
    }

    public EnumMap<CommandType, CommandProcessor> getProcessors() {
        return processors;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Command command = (Command)msg;
        processors.get(command.type()).process(command, ctx);
    }

    public void setProcessors(EnumMap<CommandType, CommandProcessor> processors) {
        this.processors = processors;
    }

    public interface CommandProcessor {
        void process(Command command, ChannelHandlerContext ctx);
    }
}
