package com.example.demo.nettyserver.handler.processor;

import com.example.demo.nettyserver.dao.GroupDao;
import com.example.demo.nettyserver.dao.RelationDao;
import com.example.demo.nettyserver.handler.CommandHandler;
import com.example.demo.nettyserver.message.Command;
import com.example.demo.nettyserver.message.command.CreateGroupCommand;
import com.example.demo.nettyserver.message.receive.CreateGroupReceive;
import com.example.demo.nettyserver.route.Router;
import com.example.demo.nettyserver.util.Constants;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;

public class CreateGroupProcessor implements CommandHandler.CommandProcessor {

    private GroupDao groupDao;
    private RelationDao relationDao;
    private Router router;

    public CreateGroupProcessor(GroupDao groupDao, RelationDao relationDao, Router router) {
        this.groupDao = groupDao;
        this.relationDao = relationDao;
        this.router = router;
    }

    @Override
    public void process(Command command, ChannelHandlerContext ctx) {
        String groupName = ((CreateGroupCommand)command).getGroupName();
        String groupId = groupDao.saveGroup(groupName);
        Attribute<String> attr = ctx.channel().attr(Constants.CLIENT_ID);
        String clientId = attr.get();
        CreateGroupReceive receive = new CreateGroupReceive();
        try {
            relationDao.save(groupId, clientId);
            receive.setCode((byte)0);
            receive.setCodeMsg("创建群聊成功");
            router.newGroup(groupId, groupName);
            router.addToGroup(groupId, clientId, ctx.channel());
        } catch (Exception e) {
            receive.setCode((byte) 1);
            receive.setCodeMsg(e.getMessage());
        }
        ctx.channel().writeAndFlush(receive);
    }
}
