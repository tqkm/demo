package com.example.demo.nettyserver.handler.processor;

import com.example.demo.nettyserver.dao.ClientDao;
import com.example.demo.nettyserver.handler.CommandHandler;
import com.example.demo.nettyserver.message.Command;
import com.example.demo.nettyserver.message.command.SendToClientCommand;
import com.example.demo.nettyserver.route.Router;
import io.netty.channel.ChannelHandlerContext;

public class SendToClientProcessor implements CommandHandler.CommandProcessor {

    private ClientDao clientDao;
    private Router router;

    public SendToClientProcessor(ClientDao clientDao, Router router) {
        this.clientDao = clientDao;
        this.router = router;
    }

    public ClientDao getClientDao() {
        return clientDao;
    }

    public void setClientDao(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    public Router getRouter() {
        return router;
    }

    public void setRouter(Router router) {
        this.router = router;
    }

    @Override
    public void process(Command command, ChannelHandlerContext ctx) {
        String clientName = ((SendToClientCommand)command).getClientName();
        String msg = ((SendToClientCommand)command).getMsg();
        String clientId = clientDao.find(clientName);
        router.sendToClient(clientId, msg);
    }
}
