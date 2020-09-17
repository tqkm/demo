package com.example.demo.nettyserver.handler.processor;

import com.example.demo.nettyserver.dao.GroupDao;
import com.example.demo.nettyserver.dao.RelationDao;
import com.example.demo.nettyserver.handler.CommandHandler;
import com.example.demo.nettyserver.message.Command;
import com.example.demo.nettyserver.message.command.JoinGroupCommand;
import com.example.demo.nettyserver.message.receive.JoinGroupReceive;
import com.example.demo.nettyserver.route.Router;
import com.example.demo.nettyserver.util.Constants;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;

public class JoinGroupProcessor implements CommandHandler.CommandProcessor {

    private GroupDao groupDao;
    private RelationDao relationDao;
    private Router router;

    public JoinGroupProcessor(GroupDao groupDao, RelationDao relationDao, Router router) {
        this.groupDao = groupDao;
        this.relationDao = relationDao;
        this.router = router;
    }

    @Override
    public void process(Command command, ChannelHandlerContext ctx) {
        String groupName = ((JoinGroupCommand)command).getGroupName();
        String groupId = groupDao.find(groupName);
        JoinGroupReceive receive   = new JoinGroupReceive();
        if (groupId != null) {
            receive.setCode((byte) 100);
            receive.setCodeMsg("群聊不存在");
        }
        Attribute<String> attr     = ctx.channel().attr(Constants.CLIENT_ID);
        String            clientId = attr.get();
        try
        {
            relationDao.save(groupId, clientId);
            router.addToGroup(groupId, clientId,ctx.channel());
            receive.setCode((byte) 0);
            receive.setCodeMsg("加入群聊成功");
        }
        catch (Exception e)
        {
            receive.setCode((byte) 1);
            receive.setCodeMsg(e.getMessage());
        }
        ctx.channel().writeAndFlush(receive);
    }
}
