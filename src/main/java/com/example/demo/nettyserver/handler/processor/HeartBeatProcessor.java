package com.example.demo.nettyserver.handler.processor;

import com.example.demo.nettyserver.handler.CommandHandler;
import com.example.demo.nettyserver.message.Command;
import com.example.demo.nettyserver.message.receive.HeartBeatReceive;
import io.netty.channel.ChannelHandlerContext;

public class HeartBeatProcessor implements CommandHandler.CommandProcessor {

    @Override
    public void process(Command command, ChannelHandlerContext ctx) {
        ctx.channel().writeAndFlush(new HeartBeatReceive());
    }
}
