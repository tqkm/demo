package com.example.demo.nettyclient;

import com.example.demo.nettyclient.handler.commandprocessor.CommandEncoder;
import com.example.demo.nettyclient.handler.commandprocessor.ReceiveDecoder;
import com.example.demo.nettyclient.message.Receive;
import com.example.demo.nettyclient.message.ReceiveType;
import com.example.demo.nettyclient.message.command.HeartBeatCommand;
import com.example.demo.nettyclient.message.command.RegisterCommand;
import com.example.demo.nettyclient.message.receive.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.EnumMap;

public class Cilent {

    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup(1)).channel(NioSocketChannel.class);
        EnumMap<ReceiveType, Class<? extends Receive>> enumMap = new EnumMap<ReceiveType, Class<? extends Receive>>(ReceiveType.class);
        enumMap.put(ReceiveType.LOGIN, LoginReceive.class);
        enumMap.put(ReceiveType.REGISTER, RegisterReceive.class);
        enumMap.put(ReceiveType.MSG_FROM_CLIENT, MsgFromClientReceive.class);
        enumMap.put(ReceiveType.CREATE_GROUP, CreateGroupReceive.class);
        enumMap.put(ReceiveType.JOIN_GROUP, JoinGroupReceive.class);
        enumMap.put(ReceiveType.MSG_FROM_GROUP, MsgFromGroupReceive.class);
        enumMap.put(ReceiveType.HEART_BEAT, HeartBeatReceive.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4)).
                        addLast(new ReceiveDecoder(enumMap)).addLast(new ChannelInboundHandlerAdapter(){
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
                    {
                        if (msg instanceof LoginReceive) {
                            String codeMsg = ((LoginReceive)msg).getCodeMsg();
                            System.out.println("登录响应:" + codeMsg);
                        } else if (msg instanceof RegisterReceive) {
                            String codeMsg = ((RegisterReceive)msg).getCodeMsg();
                            System.out.println("注册响应：" + codeMsg);
                        } else if (msg instanceof MsgFromClientReceive) {
                            String clientName = ((MsgFromClientReceive)msg).getClientName();
                            String msg1       = ((MsgFromClientReceive)msg).getMsg();
                            System.out.println("收到来自客户端:" + clientName + " 的消息：" + msg1);
                        } else if (msg instanceof CreateGroupReceive) {
                            String codeMsg = ((CreateGroupReceive)msg).getCodeMsg();
                            System.out.println("群聊创建响应:" + codeMsg);
                        } else if (msg instanceof JoinGroupReceive) {
                            String codeMsg = ((JoinGroupReceive)msg).getCodeMsg();
                            System.out.println("加入群聊响应:" + codeMsg);
                        } else if (msg instanceof MsgFromGroupReceive) {
                            String clientName = ((MsgFromGroupReceive)msg).getClientName();
                            String groupName  = ((MsgFromGroupReceive)msg).getGroupName();
                            String msg1       = ((MsgFromGroupReceive)msg).getMsg();
                            System.out.println("收到群聊{" + groupName + "}中{" + clientName + "}的消息:" + msg1);
                        } else if (msg instanceof HeartBeatReceive) {
                            System.out.println("收到心跳响应");
                        } else {
                            System.out.println("当前还未识别的响应的内容");
                        }
                    }

                    @Override
                    public void channelInactive(ChannelHandlerContext ctx) throws Exception
                    {
                        System.out.println("连接被关闭了");
                    }
                }).addLast(new CommandEncoder()).addLast(new IdleStateHandler(0, 1, 0)).
                        addLast(new ChannelInboundHandlerAdapter()
                        {
                            @Override
                            public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception
                            {
                                if (evt == IdleStateEvent.WRITER_IDLE_STATE_EVENT || evt == IdleStateEvent.FIRST_WRITER_IDLE_STATE_EVENT)
                                {
                                    System.out.println("客户端空闲写出1s，发送心跳指令");
                                    ctx.channel().writeAndFlush(new HeartBeatCommand());
                                }
                            }
                        });
            }
        });
        ChannelFuture connect = bootstrap.connect("127.0.0.1", 8888);
        connect.sync();
        Channel channel = connect.channel();
        System.out.println("第一次发送注册请求");
        RegisterCommand registerCommand = new RegisterCommand();
        registerCommand.setRegisterName("111222333");
        channel.writeAndFlush(registerCommand);
        System.out.println("第二次发送同样的注册请求");
        registerCommand.setRegisterName("111222333");
        channel.writeAndFlush(registerCommand);
        channel.closeFuture().sync();
    }

}
