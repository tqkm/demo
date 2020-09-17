package com.example.demo.nettyclient.dao;

public interface DaoFactory {

    ClientDao getClientDao();

    GroupDao getGroupDao();

    RelationDao getRelationDao();

}
