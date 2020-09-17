package com.example.demo.nettyserver;

import com.example.demo.nettyserver.dao.DaoFactory;
import com.example.demo.nettyserver.dao.impl.MemDaoFactory;
import com.example.demo.nettyserver.handler.*;
import com.example.demo.nettyserver.handler.processor.*;
import com.example.demo.nettyserver.message.CommandType;
import com.example.demo.nettyserver.route.Router;
import com.example.demo.nettyserver.route.RouterImpl;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.nio.charset.Charset;
import java.util.EnumMap;
import java.util.SortedMap;

public class Server {

    public static void main(String[] args) throws InterruptedException {
        DaoFactory daoFactory = new MemDaoFactory();
        Router router = new RouterImpl(daoFactory.getGroupDao(), daoFactory.getRelationDao());
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.channel(NioServerSocketChannel.class).group(new NioEventLoopGroup(1),
                new NioEventLoopGroup()).handler(new ChannelInboundHandlerAdapter(){
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                System.out.println("msg对象是一个SocketChannel：" + (msg instanceof SocketChannel));
                ctx.fireChannelRead(msg);
            }
        });
        EnumMap<CommandType, CommandHandler.CommandProcessor> enumMap = new EnumMap<>(CommandType.class);
        enumMap.put(CommandType.LOGIN, new LoginProcessor(daoFactory.getClientDao(), router));
        enumMap.put(CommandType.REGISTER, new RegisterProcessor(daoFactory.getClientDao(), router));
        enumMap.put(CommandType.CREATE_GROUP, new CreateGroupProcessor(daoFactory.getGroupDao(),
                daoFactory.getRelationDao(), router));
        enumMap.put(CommandType.JOIN_GROUP, new JoinGroupProcessor(daoFactory.getGroupDao(),
                daoFactory.getRelationDao(), router));
        enumMap.put(CommandType.SEND_TO_CLIENT, new SendToClientProcessor(daoFactory.getClientDao(), router));
        enumMap.put(CommandType.SEND_TO_GROUP, new SendToGroupProcessor(router));
        enumMap.put(CommandType.HEART_BEAT, new HeartBeatProcessor());
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new IdleStateHandler(2, 0, 0)).
                        addLast(new ClientIdleHandler()).
                        addLast(new LengthFieldBasedFrameDecoder(1024, 0 , 4, 0, 4)).
                        addLast(new CommandDecoder()).addLast(new CommandHandler(enumMap)).
                        addLast(new OfflineHandler(router)).addLast(new MessageEncoder()).
                        addLast(new ReceiveEncoder());
            }
        });
        ChannelFuture future = serverBootstrap.bind(8888);
        future.sync();
        future.channel().closeFuture().sync();
    }

}
