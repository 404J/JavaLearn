package com.mars.simpleNetty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

public class SelectorThreadGroup {
    SelectorThread[] selectorThreads;
    ServerSocketChannel serverSocketChannel;
    AtomicInteger counter = new AtomicInteger(0);

    SelectorThreadGroup worker = this;
    String groupName;

    SelectorThreadGroup(int num, String groupName) {
        this.groupName = groupName;
        this.selectorThreads = new SelectorThread[num];
        for (int i = 0; i < num; i++) {
            selectorThreads[i] = new SelectorThread(this);
            new Thread(selectorThreads[i]).start();
        }
    }

    public void setWorker (SelectorThreadGroup worker) {
        this.worker = worker;
    }

    public void bind(int port) {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(port));
            System.out.println("Server is started at " + port + "...");
            nextBossSelector(serverSocketChannel);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void nextWorkerSelector(Channel channel) {
        int index = counter.getAndIncrement() % worker.selectorThreads.length;
        SelectorThread selectorThread = worker.selectorThreads[index];
        selectorThread.setWorker(worker);
        selectorThread.taskQueue.add(channel);
        selectorThread.selector.wakeup();
    }

    private void nextBossSelector(Channel channel) {
        int index = counter.getAndIncrement() % selectorThreads.length;
        SelectorThread selectorThread = selectorThreads[index];
        selectorThread.taskQueue.add(channel);
        selectorThread.selector.wakeup();
    }
}
