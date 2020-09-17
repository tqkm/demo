package com.example.demo.nettyserver.handler;

import com.example.demo.nettyserver.route.Router;
import com.example.demo.nettyserver.util.Constants;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.IOException;

public class OfflineHandler extends ChannelInboundHandlerAdapter {

    private Router router;

    public OfflineHandler(Router router) {
        this.router = router;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String clientId = ctx.channel().attr(Constants.CLIENT_ID).get();
        if (clientId == null)
            return;
        System.out.println("客户端" + clientId + "下线");
        router.clientOffline(clientId, ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (!(cause instanceof IOException))
            cause.printStackTrace();
    }
}
