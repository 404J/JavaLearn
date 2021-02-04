package com.mars;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;

public class SocketNIOServer {
    public static void main(String[] args) throws Exception {
        LinkedList<SocketChannel> socketChannels = new LinkedList<>();

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(9090));
        // 设置 OS 为 NonBlocking
        serverSocketChannel.configureBlocking(false);

        while (true) {
            Thread.sleep(1000);
            SocketChannel socketChannel = serverSocketChannel.accept();
            if (socketChannel == null) {
                System.out.println("socketChannel is null ..." );
            } else {
                // 设置 OS 为 NonBlocking
                socketChannel.configureBlocking(false);
                int port = socketChannel.socket().getPort();
                System.out.println("socketChannel's port is " + port);
                socketChannels.add(socketChannel);
            }

            ByteBuffer buffer = ByteBuffer.allocate(4096);
            for (SocketChannel sc : socketChannels) {
                int i = sc.read(buffer);
                if (i > 0) {
                    buffer.flip();
                    byte[] dst = new byte[buffer.limit()];
                    buffer.get(dst);

                    String result = new String(dst);
                    System.out.println(sc.socket().getPort() + " : " + result);
                    buffer.clear();
                }
            }
        }
    }
}
