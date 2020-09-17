package com.example.demo.nettyserver.handler;

import com.example.demo.nettyserver.util.Constants;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

public class ClientIdleHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt == IdleStateEvent.FIRST_READER_IDLE_STATE_EVENT) {
            System.out.println(ctx.channel().attr(Constants.CLIENT_NAME)+"第一次出现读取空闲");
        }
        if (evt == IdleStateEvent.READER_IDLE_STATE_EVENT)
        {
            System.out.println(ctx.channel().attr(Constants.CLIENT_NAME)+"到达超时限制，关闭连接");
            ctx.channel().close();
        }
    }
}
