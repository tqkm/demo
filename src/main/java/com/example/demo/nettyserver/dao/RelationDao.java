package com.example.demo.nettyserver.dao;

import java.util.Collection;

public interface RelationDao {

    void save(String groupId, String clientId);

    boolean findGroups(String clientId, String groupId);

    Collection<String> findClients(String groupId);

    Collection<String> findGroups(String cliendId);

}
