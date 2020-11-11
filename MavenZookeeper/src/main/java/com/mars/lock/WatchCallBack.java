package com.mars.lock;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class WatchCallBack implements Watcher, AsyncCallback.StringCallback, AsyncCallback.Children2Callback, AsyncCallback.StatCallback {
    ZooKeeper zooKeeper;
    String threadName;
    CountDownLatch countDownLatch = new CountDownLatch(1);
    String pathName;

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }

    public void setZooKeeper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    public void tryLock() {
        try {
            zooKeeper.create("/lock", threadName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL, this, "ctx");
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void unLock() {
        try {
            zooKeeper.delete(pathName, -1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        switch (watchedEvent.getType()) {
            case None:
            case NodeCreated:
            case NodeDataChanged:
            case NodeChildrenChanged:
            case DataWatchRemoved:
            case ChildWatchRemoved:
            case PersistentWatchRemoved:
                break;
            case NodeDeleted:
                zooKeeper.getChildren("/", false, this, "ctx");
                break;
        }
    }

    @Override
    public void processResult(int i, String s, Object o, String s1) {
        // create call back
        if(s1 != null) {
            pathName = s1;
            zooKeeper.getChildren("/", false, this, "ctx");
        }
    }

    @Override
    public void processResult(int i, String s, Object o, List<String> list, Stat stat) {
        Collections.sort(list);
        int index = list.indexOf(pathName.substring(1));
        if(index == 0) {
            countDownLatch.countDown();
        } else {
            zooKeeper.exists("/" + list.get(index - 1), this, this, "ctx");
        }
    }

    @Override
    public void processResult(int i, String s, Object o, Stat stat) {
    }
}
