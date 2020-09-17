package com.example.demo.nettyclient.dao;

public interface ClientDao {

    String find(String clientName);

    String saveClient(String clientName);

}
