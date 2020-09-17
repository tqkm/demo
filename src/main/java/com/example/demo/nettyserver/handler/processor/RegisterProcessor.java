package com.example.demo.nettyserver.handler.processor;

import com.example.demo.nettyserver.dao.ClientDao;
import com.example.demo.nettyserver.handler.CommandHandler;
import com.example.demo.nettyserver.message.Command;
import com.example.demo.nettyserver.message.command.RegisterCommand;
import com.example.demo.nettyserver.message.receive.RegisterReceive;
import com.example.demo.nettyserver.route.Router;
import com.example.demo.nettyserver.util.Constants;
import io.netty.channel.ChannelHandlerContext;

public class RegisterProcessor implements CommandHandler.CommandProcessor {

    private ClientDao clientDao;
    private Router router;

    public RegisterProcessor(ClientDao clientDao, Router router) {
        this.clientDao = clientDao;
        this.router = router;
    }

    @Override
    public void process(Command command, ChannelHandlerContext ctx) {
        String registerName = ((RegisterCommand)command).getRegisterName();
        RegisterReceive registerReceive = new RegisterReceive();
        try {
            String clientId = clientDao.saveClient(registerName);
            router.clientOnline(clientId, ctx.channel());
            registerReceive.setCode((byte)0);
            registerReceive.setCodeMsg("注册成功");
            ctx.channel().attr(Constants.CLIENT_ID).set(clientId);
            ctx.channel().attr(Constants.CLIENT_NAME).set(registerName);
        } catch (Exception e) {
            registerReceive.setCode((byte)1);
            registerReceive.setCodeMsg("客户端标识已存在");
        }
        ctx.channel().writeAndFlush(registerReceive);
    }
}
