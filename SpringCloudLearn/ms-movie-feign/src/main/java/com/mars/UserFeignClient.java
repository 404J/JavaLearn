package com.mars;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-user", fallbackFactory = UserFeignClientFallbackFactory.class)
public interface UserFeignClient {
    @GetMapping("users/{id}")
    User findById(@PathVariable("id") Long id);

    @GetMapping("users/ribbon-retry")
    String testRibbonRetry();

    @GetMapping("users/hystrix-thread")
    String testHystrixThread();

    @GetMapping("users/hystrix-breaker")
    String testHystrixBreaker();
}

