package com.mars.simpleNetty;

public class MainThread {
    public static void main(String[] args) {
        SelectorThreadGroup bossGroup = new SelectorThreadGroup(3, "boss");
        SelectorThreadGroup workerGroup = new SelectorThreadGroup(3, "worker");
        bossGroup.setWorker(workerGroup);
        bossGroup.bind(6666);
        bossGroup.bind(7777);
    }
}
