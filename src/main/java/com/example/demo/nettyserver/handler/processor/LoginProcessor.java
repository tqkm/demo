package com.example.demo.nettyserver.handler.processor;

import com.example.demo.nettyserver.dao.ClientDao;
import com.example.demo.nettyserver.handler.CommandHandler;
import com.example.demo.nettyserver.message.Command;
import com.example.demo.nettyserver.message.command.LoginCommand;
import com.example.demo.nettyserver.message.receive.LoginReceive;
import com.example.demo.nettyserver.route.Router;
import com.example.demo.nettyserver.util.Constants;
import io.netty.channel.ChannelHandlerContext;

public class LoginProcessor implements CommandHandler.CommandProcessor {

    private ClientDao clientDAO;
    private Router router;

    public LoginProcessor(ClientDao clientDAO, Router router)
    {
        this.clientDAO = clientDAO;
        this.router = router;
    }

    @Override
    public void process(Command command, ChannelHandlerContext ctx) {
        String loginName = ((LoginCommand)command).getLoginName();
        String clientId = clientDAO.find(loginName);
        LoginReceive receive = new LoginReceive();
        if (clientId != null) {
            ctx.channel().attr(Constants.CLIENT_ID).set(clientId);
            ctx.channel().attr(Constants.CLIENT_NAME).set(loginName);
            receive.setCode((byte)0);
            receive.setCodeMsg("登陆成功");
            router.clientOnline(clientId, ctx.channel());
        } else {
            receive.setCode((byte)1);
            receive.setCodeMsg("客户端标识不存在");
        }
        ctx.channel().writeAndFlush(receive);
    }
}
