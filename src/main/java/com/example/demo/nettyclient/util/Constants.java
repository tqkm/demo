package com.example.demo.nettyclient.util;

import io.netty.util.AttributeKey;

import java.nio.charset.Charset;

public class Constants {

    public static final AttributeKey<String> CLIENT_ID   = AttributeKey.valueOf("clientId");
    public static final AttributeKey<String> CLIENT_NAME = AttributeKey.valueOf("clientName");
    public static final Charset CHARSET = Charset.forName("UTF-8");

}
