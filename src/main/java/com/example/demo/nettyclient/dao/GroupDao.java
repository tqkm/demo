package com.example.demo.nettyclient.dao;

public interface GroupDao {

    String find(String groupName);

    String getGroupNameById(String groupId);

    String saveGroup(String groupName);

}
