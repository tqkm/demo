package com.example.demo.nio.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NioServer {

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();//创建选择器
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);//设置通道非阻塞
        serverSocketChannel.bind(new InetSocketAddress(8080));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);//注册通道到选择器上
        while (true) {
            selector.select();//监听事件
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel)key.channel();
                    SocketChannel socketChannel = channel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel)key.channel();
                    System.out.println(read(socketChannel));
                    socketChannel.close();
                }
                iterator.remove();
            }
        }
    }

    private static String read(SocketChannel sChannel) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        StringBuffer sb = new StringBuffer();
        while (true) {
            byteBuffer.clear();
            int read = sChannel.read(byteBuffer);
            if (read == -1)
                break;
            byteBuffer.flip();
            int limit = byteBuffer.limit();
            char[] c = new char[limit];
            for (int i = 0; i < c.length; i++)
                c[i] = (char)byteBuffer.get(i);
            sb.append(c);
            byteBuffer.clear();
        }
        return sb.toString();
    }

}
