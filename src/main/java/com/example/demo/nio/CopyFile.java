package com.example.demo.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class CopyFile {

    public static void main(String[] args) throws IOException {
        String str = "";
        FileInputStream fis = new FileInputStream(str);
        FileChannel fcin = fis.getChannel();
        FileOutputStream fos = new FileOutputStream(str);
        FileChannel fcout = fos.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        while (true) {
            int read = fcin.read(byteBuffer);
            if (read == -1)
                break;
            byteBuffer.flip();
            fcout.write(byteBuffer);
            byteBuffer.clear();
        }
    }

}
