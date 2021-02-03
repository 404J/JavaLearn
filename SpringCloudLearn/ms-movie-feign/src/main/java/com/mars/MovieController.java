package com.mars;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("movies")
public class MovieController {

    @Autowired
    private UserFeignClient userFeignClient;

    @GetMapping("/users/{id}")
    public User findById(@PathVariable Long id) {
        User user = userFeignClient.findById(id);
        return user;
    }

    @GetMapping("/ribbon-retry")
    public String testRibbonRetry() {
        String str = userFeignClient.testRibbonRetry();
        return str;
    }

    @GetMapping("/hystrix-thread")
    public String testHystrixThread() {
        String str = userFeignClient.testHystrixThread();
        if (!"降级了".equals(str)) {
            System.out.println("测试 Hystrix Thread 成功返回");
        }
        return str;
    }

    @GetMapping("/hystrix-breaker")
    public String testHystrixBreaker() {
        String str = userFeignClient.testHystrixBreaker();
        if (!"降级了".equals(str)) {
            System.out.println("测试 Hystrix Breaker 成功返回");
        }
        return str;
    }
}
