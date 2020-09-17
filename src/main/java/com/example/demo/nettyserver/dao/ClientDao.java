package com.example.demo.nettyserver.dao;

public interface ClientDao {

    String find(String clientName);

    String saveClient(String clientName);

}
