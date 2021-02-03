package com.mars;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    private volatile int count = 1;

    @Value("${server.port}")
    private String port;

    @GetMapping("/{id}")
    public Optional<User> findById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user;
    }

    @GetMapping("/ribbon-retry")
    public String testRibbonRetry() {
//        System.out.println("第" + count + "次成功的 ribbon 请求" + port);
        System.out.println("第" + count + "次失败的 ribbon 请求" + port);
        count ++;
        try {
//            Thread.sleep(300);
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "port: " + port;
    }

    @GetMapping("/hystrix-thread")
    public String testHystrixThread() {
        System.out.println("第" + count + "次 hystrix 请求" + port);
        count ++;
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "port: " + port;
    }

    @GetMapping("/hystrix-breaker")
    public String testHystrixBreaker() {
        System.out.println("第" + count + "次 hystrix 请求" + port);
        count ++;
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int i = 1 / 0;
        return "port: " + port;
    }
}
