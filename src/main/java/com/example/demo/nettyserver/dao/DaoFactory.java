package com.example.demo.nettyserver.dao;

public interface DaoFactory {

    ClientDao getClientDao();

    GroupDao getGroupDao();

    RelationDao getRelationDao();

}
