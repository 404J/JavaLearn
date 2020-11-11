package com.mars.config;

import com.mars.Utils.ZkUtils;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestConfig {

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
        MyConfig myConfig = new MyConfig();
        WatchCallBack watchCallBack = new WatchCallBack();
        watchCallBack.setZooKeeper(zooKeeper);
        watchCallBack.setMyConfig(myConfig);
        watchCallBack.await();
        while(true) {
            if("".equals(myConfig.getConf())) {
                System.out.println("conf dui le ...");
                watchCallBack.await();
            } else {
                System.out.println(myConfig.getConf());
            }
            Thread.sleep(1000);
        }
    }
}
