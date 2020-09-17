package com.example.demo.nettyclient.message;

public enum ReceiveType {

    REGISTER, LOGIN, MSG_FROM_CLIENT, CREATE_GROUP, JOIN_GROUP, MSG_FROM_GROUP, HEART_BEAT;

    public static ReceiveType value(byte value)
    {
        switch (value)
        {
            case 1:
                return REGISTER;
            case 2:
                return LOGIN;
            case 3:
                return MSG_FROM_CLIENT;
            case 4:
                return CREATE_GROUP;
            case 5:
                return JOIN_GROUP;
            case 6:
                return MSG_FROM_GROUP;
            case 7:
                return HEART_BEAT;
            default:
                throw new IllegalArgumentException();
        }
    }

}
