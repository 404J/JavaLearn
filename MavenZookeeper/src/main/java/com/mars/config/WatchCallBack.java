package com.mars.config;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

public class WatchCallBack implements Watcher, AsyncCallback.StatCallback, AsyncCallback.DataCallback {
    ZooKeeper zooKeeper;
    MyConfig myConfig;
    CountDownLatch countDownLatch = new CountDownLatch(1);

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }

    public void setZooKeeper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    public MyConfig getMyConfig() {
        return myConfig;
    }

    public void setMyConfig(MyConfig myConfig) {
        this.myConfig = myConfig;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    public void await() {
        zooKeeper.exists("/appConf", this, this, "ctx");
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        // exists/get watch
        switch (watchedEvent.getType()) {
            case None:
            case NodeChildrenChanged:
            case DataWatchRemoved:
            case ChildWatchRemoved:
            case PersistentWatchRemoved:
                break;
            case NodeCreated:
            case NodeDataChanged:
                zooKeeper.getData("/appConf", this, this, "ctx");
                break;
            case NodeDeleted:
                myConfig.setConf("");
                countDownLatch = new CountDownLatch(1);
                break;
        }
    }

    @Override
    public void processResult(int i, String s, Object o, Stat stat) {
        // exist callback
        if(stat != null) {
            // exist
            zooKeeper.getData("/appConf", this, this, "ctx");
        }
    }

    @Override
    public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {
        // get callback
        if(bytes != null) {
            myConfig.setConf(new String(bytes));
            countDownLatch.countDown();
        }
    }
}
