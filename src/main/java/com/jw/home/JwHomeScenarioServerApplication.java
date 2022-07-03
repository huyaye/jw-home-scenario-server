package com.jw.home;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class JwHomeScenarioServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(JwHomeScenarioServerApplication.class, args);
    }
}
