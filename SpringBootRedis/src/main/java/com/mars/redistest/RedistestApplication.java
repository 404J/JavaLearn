package com.mars.redistest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class RedistestApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(RedistestApplication.class, args);
        TestRedis redis = ctx.getBean(TestRedis.class);
        redis.testRedis();
    }

}
