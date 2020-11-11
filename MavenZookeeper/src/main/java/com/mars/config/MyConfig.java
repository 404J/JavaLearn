package com.mars.config;

public class MyConfig {
    private volatile String conf;

    public MyConfig(String conf) {
        this.conf = conf;
    }

    public MyConfig() {
    }

    public String getConf() {
        return conf;
    }

    public void setConf(String conf) {
        this.conf = conf;
    }
}
