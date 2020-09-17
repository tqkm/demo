package com.example.demo.nettyserver.dao;

public interface GroupDao {

    String find(String groupName);

    String getGroupNameById(String groupId);

    String saveGroup(String groupName);

}
