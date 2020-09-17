package com.example.demo.nio.demo;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class NioClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8080);
        OutputStream outputStream = socket.getOutputStream();
        String s = "Hello Word!";
        outputStream.write(s.getBytes());
        outputStream.close();
    }

}
