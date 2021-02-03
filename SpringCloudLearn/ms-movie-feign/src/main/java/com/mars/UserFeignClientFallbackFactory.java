package com.mars;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class UserFeignClientFallbackFactory implements FallbackFactory<UserFeignClient> {
    @Override
    public UserFeignClient create(Throwable throwable) {
        UserFeignClientFallBackImpl userFeignClient = new UserFeignClientFallBackImpl();
        userFeignClient.setCause(throwable);
        return userFeignClient;
    }
}

