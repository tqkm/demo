package com.example.demo.nettyclient.message;

public enum CommandType {

    REGISTER, LOGIN, SEND_TO_CLIENT, CREATE_GROUP, JOIN_GROUP, SEND_TO_GROUP, HEART_BEAT;

    public static CommandType value(byte value) {
        switch (value) {
            case 1:
                return REGISTER;
            case 2:
                return LOGIN;
            case 3:
                return SEND_TO_CLIENT;
            case 4:
                return CREATE_GROUP;
            case 5:
                return JOIN_GROUP;
            case 6:
                return SEND_TO_GROUP;
            case 7:
                return HEART_BEAT;
            default:
                throw new IllegalArgumentException();
        }
    }
}
