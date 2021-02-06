package com.mars;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SocketMultiplexingIOServer {

    private ServerSocketChannel serverSocketChannel;

    // 对应这 OS 中的多路复用器（select poll epoll）
    private Selector selector;

    public static void main(String[] args) {
        SocketMultiplexingIOServer socketMultiplexingIOServer = new SocketMultiplexingIOServer();
        socketMultiplexingIOServer.initServer();
        socketMultiplexingIOServer.processing();
    }

    private void initServer () {
        try {
            // 获取到 server 的 fd_s
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(9090));

            /*
            对应 epoll 模型中的 epoll_create() 获取到 epoll 的 fd_e
             */
            selector = Selector.open();

            /*
            register
            1. 如果是 select 或者 poll 模型，相当于在 jvm 中开辟一个集合，将 fd_s 放入
            2. 如果是 epoll 模型，调用 epoll_ctl(fd_e, EPOLL_CTL_ADD, fd_s, EPOLLIN)
             */
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Server started at 9090 ...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processing() {
        try {
            while (true) {
                Set<SelectionKey> keys = selector.keys();
                System.out.println(keys.size() + " size");

                /*
                select
                1. 如果是 select 或者 poll 模型，调用内核的 select()\poll()
                2. 如果是 epoll 模型，调用内核的 epoll_wait()
                 */
                while (selector.select() > 0) {
                    //返回的有状态的 fd 集合
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iter = selectionKeys.iterator();
                    while (iter.hasNext()) {
                        SelectionKey selectionKey = iter.next();
                        iter.remove();
                        if (selectionKey.isAcceptable()) {
                            acceptHandler(selectionKey);
                        } else if (selectionKey.isReadable()) {
                            readHandler(selectionKey);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptHandler(SelectionKey selectionKey) {
        try {
            ServerSocketChannel serverChannel = (ServerSocketChannel) selectionKey.channel();
            // 返回 fd_c
            SocketChannel socketChannel = serverChannel.accept();
            socketChannel.configureBlocking(false);

            /*
            register
            1. 如果是 select 或者 poll 模型，相当于在 jvm 中开辟一个集合，将 fd_c 放入
            2. 如果是 epoll 模型，调用 epoll_ctl(fd_e, EPOLL_CTL_ADD, fd_c, EPOLLIN)
             */
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            socketChannel.register(selector, SelectionKey.OP_READ, buffer);
            System.out.println("Client " + socketChannel.getRemoteAddress() + " was accepted ...");
        } catch (IOException e) {
            e.printStackTrace();
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
                System.out.println("Read data from " + socketChannel.getRemoteAddress() + " : " + result);
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
