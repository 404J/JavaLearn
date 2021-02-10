package com.mars.simpleNetty;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SelectorThread implements Runnable{

    Selector selector;
    SelectorThreadGroup group;
    BlockingQueue<Channel> taskQueue = new LinkedBlockingQueue<Channel>();

    SelectorThread(SelectorThreadGroup group) {
        try {
            this.group = group;
            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        while (true) {
            try {
                int i = selector.select();

                if (i > 0) {
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = keys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        iterator.remove();
                        if (selectionKey.isAcceptable()) {
                            acceptHandler(selectionKey);
                        } else if (selectionKey.isReadable()) {
                            readHandler(selectionKey);
                        }
                    }
                }

                if (!taskQueue.isEmpty()) {
                    Channel channel = taskQueue.take();
                    if (channel instanceof ServerSocketChannel) {
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) channel;
                        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
                    } else if (channel instanceof SocketChannel) {
                        SocketChannel socketChannel = (SocketChannel) channel;
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        socketChannel.register(selector, SelectionKey.OP_READ, buffer);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void readHandler(SelectionKey selectionKey) {
        try {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
            int i = socketChannel.read(buffer);
            if (i > 0) {
                buffer.flip();
                byte[] dst = new byte[buffer.limit()];
                buffer.get(dst);
                String result = new String(dst);
                System.out.println(group.groupName + " - " + Thread.currentThread().getName() + " - Read data from " + socketChannel.getRemoteAddress() + " : " + result);
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptHandler(SelectionKey selectionKey) {
        try {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            System.out.println(group.groupName + " - " + Thread.currentThread().getName() + " - Get connection from " + socketChannel.getRemoteAddress());
            group.nextWorkerSelector(socketChannel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setWorker (SelectorThreadGroup worker) {
        this.group = worker;
    }
}
