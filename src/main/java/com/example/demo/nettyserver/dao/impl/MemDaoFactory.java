package com.example.demo.nettyserver.dao.impl;

import com.example.demo.nettyserver.dao.ClientDao;
import com.example.demo.nettyserver.dao.DaoFactory;
import com.example.demo.nettyserver.dao.GroupDao;
import com.example.demo.nettyserver.dao.RelationDao;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class MemDaoFactory implements DaoFactory {

    private ClientDao clientDao;
    private GroupDao groupDao;
    private RelationDao relationDao;

    public MemDaoFactory() {
        this.clientDao = new MemClientDao();
        this.groupDao = new MemGroupDao();
        this.relationDao = new MemRelationDao();
    }

    @Override
    public ClientDao getClientDao() {
        return clientDao;
    }

    @Override
    public GroupDao getGroupDao() {
        return groupDao;
    }

    @Override
    public RelationDao getRelationDao() {
        return relationDao;
    }

    class MemRelationDao implements RelationDao {

        private ConcurrentMap<String, Set<String>> clientJoinGroups = new ConcurrentHashMap<>();
        private ConcurrentMap<String, Set<String>> groupHasClients  = new ConcurrentHashMap<>();

        @Override
        public void save(String groupId, String clientId) {
            Set<String> groupIds = clientJoinGroups.get(clientId);
            if (groupIds == null) {
                groupIds = new ConcurrentSkipListSet<>();
                clientJoinGroups.put(clientId, groupIds);
            }
            if (groupIds.contains(groupId))
                throw new IllegalStateException("该群聊已经包含该客户端，不需要重复加入");
            groupIds.add(clientId);
            Set<String> clientIds = groupHasClients.get(groupId);
            if (clientIds == null) {
                clientIds = new ConcurrentSkipListSet<>();
                groupHasClients.put(groupId, clientIds);
            }
            if (clientIds.contains(clientId))
                throw new IllegalStateException("该群聊已经包含该客户端，不需要重复加入");
            clientIds.add(clientId);
        }

        @Override
        public boolean findGroups(String clientId, String groupId) {
            Set<String> groupIds = clientJoinGroups.get(clientId);
            if (groupIds == null || !groupIds.contains(groupId))
                return false;
            return true;
        }

        @Override
        public Collection<String> findClients(String groupId) {
            return groupHasClients.get(groupId);
        }

        @Override
        public Collection<String> findGroups(String cliendId) {
            return clientJoinGroups.get(cliendId);
        }
    }

    class MemClientDao implements ClientDao {

        private ConcurrentMap<String, String> store = new ConcurrentHashMap<>();

        @Override
        public String find(String clientName) {
            return store.get(clientName);
        }

        @Override
        public String saveClient(String clientName) {
            String clientId = UUID.randomUUID().toString();
            String pre = store.putIfAbsent(clientName, clientId);
            if (pre != null)
                throw new IllegalStateException("不允许重复的客户端标识");
            return clientId;
        }
    }

    class MemGroupDao implements GroupDao {

        private ConcurrentMap<String, String> store = new ConcurrentHashMap<>();
        private ConcurrentMap<String, String> query = new ConcurrentHashMap<>();

        @Override
        public String find(String groupName) {
            return store.get(groupName);
        }

        @Override
        public String getGroupNameById(String groupId) {
            return query.get(groupId);
        }

        @Override
        public String saveGroup(String groupName) {
            String groupId = UUID.randomUUID().toString();
            String pre = store.putIfAbsent(groupName, groupId);
            if (pre != null)
                throw new IllegalStateException("不允许重复的客户端标识");
            query.put(groupId, groupName);
            return groupId;
        }
    }
}
