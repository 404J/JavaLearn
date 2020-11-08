package com.mars.redistest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class TestRedis {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public void testRedis() {
        redisTemplate.opsForValue().set("k1", "hello");
        System.out.println(redisTemplate.opsForValue().get("k1"));

        stringRedisTemplate.opsForValue().set("k2", "world");
        System.out.println(stringRedisTemplate.opsForValue().get("k2"));

    }
}
