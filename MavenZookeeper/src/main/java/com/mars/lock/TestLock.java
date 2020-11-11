package com.mars.lock;

import com.mars.Utils.ZkUtils;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestLock {
    ZooKeeper zooKeeper;

    @Before
    public void before() {
        zooKeeper = ZkUtils.getZK();
    }

    @After
    public void after() throws InterruptedException {
        zooKeeper.close();
    }

    @Test
    public void test() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                String threadName = Thread.currentThread().getName();
                WatchCallBack watchCallBack = new WatchCallBack();
                watchCallBack.setZooKeeper(zooKeeper);
                watchCallBack.setThreadName(threadName);
                watchCallBack.tryLock();
                System.out.println(threadName + ": working...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                watchCallBack.unLock();
            }).start();
        }

        while (true) {

        }
    }
}
