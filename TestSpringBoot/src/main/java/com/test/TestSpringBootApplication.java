package com.test;

import com.test.config.ServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TestSpringBootApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(TestSpringBootApplication.class, args);
        ServerConfig serverConfig = context.getBean(ServerConfig.class);
        System.out.println(serverConfig);
    }
}
