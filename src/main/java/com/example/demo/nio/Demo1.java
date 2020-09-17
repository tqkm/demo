package com.example.demo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Demo1 {

    static class ClientProcessor implements Runnable{

        private Selector selector;

        public ClientProcessor(Selector selector){
            this.selector = selector;
        }

        @Override
        public void run() {
            while (true){
                try {
                    selector.select();
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        if (!key.isValid()) {
                            continue;
                        }
                        if (key.isReadable()) {
                            SocketChannel clientChannel = (SocketChannel) key.channel();
                            ByteBuffer buffer = (ByteBuffer) key.attachment();
                            int read = clientChannel.read(buffer);
                            if (read == -1) {
                                key.cancel();
                                clientChannel.close();
                            } else {
                                buffer.flip();
                                int position = buffer.position();
                                int limit = buffer.limit();
                                List<ByteBuffer> buffers = new ArrayList<>();
                                for (int i = position; i < limit; i++){
                                    if (buffer.get(i) == '\r'){
                                        ByteBuffer message = ByteBuffer.allocate(i - buffer.position() + 1);
                                        buffer.limit(i + 1);
                                        message.put(buffer);
                                        buffer.limit(limit);
                                        message.flip();
                                        buffers.add(message);
                                        buffer.limit(limit);
                                    }
                                }
                                for (ByteBuffer buffer1 :buffers)
                                    while (buffer1.hasRemaining())
                                        clientChannel.write(buffer1);
                                buffer.compact();
                                clientChannel.close();
                            }
                        }
                    }
                    selectionKeys.clear();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(8899));
        final Selector[] selectors = new Selector[Runtime.getRuntime().availableProcessors()];
        for (int i = 0; i < selectors.length; i++) {
            final Selector selector = Selector.open();
            selectors[i] = selector;
            new Thread(new ClientProcessor(selector)).start();
        }
        AtomicInteger id = new AtomicInteger();
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                iterator.next();
                SocketChannel socketChannel = serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
                Selector selectForChild = selectors[id.getAndIncrement() % selectors.length];
                socketChannel.register(selectForChild, SelectionKey.OP_READ, ByteBuffer.allocate(128 * 2));
                selectForChild.wakeup();
                iterator.remove();
            }
        }
    }

}
