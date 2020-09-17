package com.example.demo.nettyserver.route;

import io.netty.channel.Channel;

public interface Router {

    void clientOffline(String clientId, Channel channel);

    void clientOnline(String clientId, Channel channel);

    void sendToClient(String clientId, String msg);

    /**
     * 将客户端加入到群聊中。只有客户端发送加入指令的时候才需要调用
     * @param groupId
     * @param clientId
     */
    void addToGroup(String groupId, String clientId,Channel channel);

    void newGroup(String groupId,String groupName);

    void sendToGroup(String groupName, String srcClientId, String msg);

}
