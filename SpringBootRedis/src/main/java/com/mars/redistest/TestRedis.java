package com.mars.redistest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class TestRedis {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public void testRedis() {
        stringRedisTemplate.opsForValue().set("k1", "hello");
        System.out.println(stringRedisTemplate.opsForValue().get("k1"));

        stringRedisTemplate.opsForValue().set("k2", "world");
        System.out.println(stringRedisTemplate.opsForValue().get("k2"));

        ArrayList<String> keys = new ArrayList<>();
        keys.add("k1");
        keys.add("k2");

        List<String> values = stringRedisTemplate.opsForValue().multiGet(keys);
        System.out.println(values);

        stringRedisTemplate.opsForValue().append("k1", " mars");
        System.out.println(stringRedisTemplate.opsForValue().get("k1"));

        System.out.println(stringRedisTemplate.opsForValue().get("k1", 1 , 2));

        stringRedisTemplate.opsForValue().set("k3", "5");
        System.out.println(stringRedisTemplate.opsForValue().increment("k3"));

        stringRedisTemplate.opsForValue().setBit("k4", 2, false);
        System.out.println(stringRedisTemplate.opsForValue().getBit("k4", 2));

        Set<String> allKeys = stringRedisTemplate.keys("*");
        System.out.println(allKeys);

    }
}
