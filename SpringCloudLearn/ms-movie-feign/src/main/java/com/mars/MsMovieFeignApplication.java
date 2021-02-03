package com.mars;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsMovieFeignApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsMovieFeignApplication.class, args);
    }
}
