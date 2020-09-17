package com.example.demo.nettyserver.route;

import com.example.demo.nettyserver.dao.GroupDao;
import com.example.demo.nettyserver.dao.RelationDao;
import com.example.demo.nettyserver.message.receive.MsgFromClientReceive;
import com.example.demo.nettyserver.message.receive.MsgFromGroupReceive;
import com.example.demo.nettyserver.util.Constants;
import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RouterImpl implements Router{

    private final ConcurrentHashMap<String, Object> clientRouter = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, GroupInfo> groupRouter = new ConcurrentHashMap<>();

    private GroupDao groupDao;
    private RelationDao relationDao;

    public RouterImpl(GroupDao groupDAO, RelationDao relationDAO)
    {
        this.groupDao = groupDAO;
        this.relationDao = relationDAO;
    }


    class GroupInfo {
        String groupName;
        ConcurrentHashMap<String, Object> members = new ConcurrentHashMap<>();

        public GroupInfo(String groupName) {
            this.groupName = groupName;
        }
    }

    @Override
    public void clientOffline(String clientId, Channel channel) {
        Object exist = clientRouter.get(clientId);
        while (true) {
            if (exist == channel) {
                if (clientRouter.remove(clientId, channel))
                    break;
                else
                    exist = clientRouter.get(clientId);
            } else if (exist instanceof Channel[]){
                Channel[] array = (Channel[])exist;
                if (array.length == 2) {
                    Channel newValue = array[0] == channel ? array[1] : array[0];
                    if (clientRouter.replace(clientId, exist, newValue))
                        break;
                } else {
                    ArrayList<Channel> newArrays = new ArrayList<>();
                    for (Channel each: array) {
                        if (each != channel)
                            newArrays.add(each);
                    }
                    Channel[] channels = newArrays.toArray(new Channel[0]);
                    if (clientRouter.replace(clientId, exist, channels))
                        break;
                }
                exist = clientRouter.get(clientId);
            } else {
                throw new IllegalStateException();
            }
        }
    }

    @Override
    public void clientOnline(String clientId, Channel channel) {
        Object exist = clientRouter.get(clientId);
        while (true) {
            if (exist == null) {
                if ((exist = clientRouter.putIfAbsent(clientId, channel)) == null) {
                    break;
                }
            } else {
                Channel[] array = null;
                if (exist instanceof Channel) {
                    array = new Channel[2];
                    array[0] = (Channel)exist;
                    array[1] = channel;
                } else {
                    Channel[] oldArray = (Channel[])exist;
                    array = new Channel[oldArray.length + 1];
                    System.arraycopy(oldArray, 0, array, 1, oldArray.length);
                    array[0] = channel;
                }
                if (clientRouter.replace(clientId, exist, array))
                    break;
                else
                    exist = clientRouter.get(clientId);
            }
        }
    }

    @Override
    public void sendToClient(String clientId, String msg) {
        Object exist = clientRouter.get(clientId);
        if (exist == null)
            return;
        sendToChannel(msg, exist);
    }

    private void sendToChannel(String msg, Object dest) {
        if (dest instanceof Channel) {
            MsgFromClientReceive receive = new MsgFromClientReceive();
            Channel channel = (Channel)dest;
            receive.setClientName(channel.attr(Constants.CLIENT_NAME).get());
            receive.setMsg(msg);
            channel.writeAndFlush(receive);
        } else {
            Channel[] channels = (Channel[])dest;
            for (Channel each : channels) {
                try {
                    MsgFromClientReceive receive = new MsgFromClientReceive();
                    receive.setClientName(each.attr(Constants.CLIENT_NAME).get());
                    receive.setMsg(msg);
                    each.writeAndFlush(receive);
                } finally {

                }
            }
        }
    }

    @Override
    public void addToGroup(String groupId, String clientId, Channel channel) {
        GroupInfo groupInfo = new GroupInfo(groupId);
        addElementToMap(groupInfo.members, clientId, channel);
    }

    private void addElementToMap(ConcurrentHashMap<String, Object> map, String clientId, Channel channel) {
        Object exist = map.get(clientId);
        while (true) {
            if (exist == null) {
                if ((exist = map.putIfAbsent(clientId, channel)) == null)
                    break;
            } else {
                Channel[] array = null;
                if (exist instanceof Channel) {
                    if (exist == channel)
                        break;
                    array = new Channel[2];
                    array[0] = (Channel)exist;
                    array[1] = channel;
                } else {
                    Channel[] oldArray = (Channel[])exist;
                    for (Channel member: oldArray) {
                        if (member == channel)
                            break;
                    }
                    array = new Channel[oldArray.length + 1];
                    System.arraycopy(oldArray, 0, array, 1, oldArray.length);
                    array[0] = channel;
                }
                if (map.replace(clientId, exist, array))
                    break;
                else
                    exist = map.get(clientId);
            }
        }

    }

    @Override
    public void newGroup(String groupId, String groupName) {
        GroupInfo groupInfo = new GroupInfo(groupName);
        groupRouter.putIfAbsent(groupId, groupInfo);
    }

    @Override
    public void sendToGroup(String groupName, String srcClientId, String msg) {
        String groupId = groupDao.find(groupName);
        if (groupId == null)
            return;
        GroupInfo groupInfo = initGroupIfNeed(groupId);
        Object src = groupInfo.members.get(srcClientId);
        String clientName;
        if (src instanceof Channel)
            clientName = ((Channel)src).attr(Constants.CLIENT_NAME).get();
        else
            clientName = ((Channel[]) src)[0].attr(Constants.CLIENT_NAME).get();
        MsgFromGroupReceive receive = new MsgFromGroupReceive();
        receive.setMsg(msg);
        receive.setClientName(clientName);
        receive.setGroupName(groupName);
        for (Map.Entry<String, Object> each : groupInfo.members.entrySet()) {
            if (each.getKey().equals(srcClientId))
                continue;
            Object dest = each.getValue();
            try {
                if (dest instanceof Channel)
                    ((Channel)dest).writeAndFlush(receive);
                else {
                    for (Channel channel: ((Channel[])dest)) {
                        channel.writeAndFlush(receive);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private GroupInfo initGroupIfNeed(String groupId) {
        GroupInfo groupInfo = groupRouter.get(groupId);
        if (groupInfo == null) {
            groupInfo = new GroupInfo(groupDao.getGroupNameById(groupId));
            for (String memberId : relationDao.findClients(groupId)) {
                Object o = clientRouter.get(memberId);
                if (o != null) {
                    groupInfo.members.put(memberId, o);
                }
            }
            groupRouter.putIfAbsent(groupId, groupInfo);
        }
        return groupInfo;
    }
}
