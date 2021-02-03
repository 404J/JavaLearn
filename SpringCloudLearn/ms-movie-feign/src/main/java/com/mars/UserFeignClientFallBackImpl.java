package com.mars;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserFeignClientFallBackImpl implements UserFeignClient{
    @Setter
    private Throwable cause;

    @Override
    public User findById(Long id) {
        log.error("获取用户失败, " + cause.getMessage());
        return new User(0, "defaultUser");
    }

    @Override
    public String testRibbonRetry() {
        log.error("测试 Ribbon Retry 降级, " + cause.getMessage());
        return "降级了";
    }

    @Override
    public String testHystrixThread() {
        log.error("测试 Hystrix Thread 降级, " + cause.getMessage());
        System.out.println("测试 Hystrix Thread 降级");
        return "降级了";
    }

    @Override
    public String testHystrixBreaker() {
        log.error("测试 Hystrix Breaker 降级, " + cause.getMessage());
        System.out.println("测试 Hystrix Breaker 降级");
        return "降级了";
    }
}
