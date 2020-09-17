package com.example.demo.nettyserver.handler.processor;

import com.example.demo.nettyserver.handler.CommandHandler;
import com.example.demo.nettyserver.message.Command;
import com.example.demo.nettyserver.message.command.SendToGroupCommand;
import com.example.demo.nettyserver.route.Router;
import com.example.demo.nettyserver.util.Constants;
import io.netty.channel.ChannelHandlerContext;

public class SendToGroupProcessor implements CommandHandler.CommandProcessor {

    private Router router;

    public SendToGroupProcessor(Router router) {
        this.router = router;
    }

    @Override
    public void process(Command command, ChannelHandlerContext ctx) {
        String groupName = ((SendToGroupCommand)command).getGroupName();
        String msg = ((SendToGroupCommand)command).getMsg();
        String clientId = ctx.channel().attr(Constants.CLIENT_ID).get();
        router.sendToGroup(groupName, clientId, msg);
    }
}
