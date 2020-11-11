package com.mars.Utils;

import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

public class ZkUtils {
    static ZooKeeper zooKeeper;
    static String conStr = "localhost:2181/testLock";
    static DefaultWatch defaultWatch = new DefaultWatch();
    static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static ZooKeeper getZK() {
        try {
            zooKeeper = new ZooKeeper(conStr, 1000, defaultWatch);
            defaultWatch.setCountDownLatch(countDownLatch);
            countDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zooKeeper;
    }
}
