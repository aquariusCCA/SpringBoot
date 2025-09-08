package com.test.testautoconfiguration;

import com.loong.service.DemoService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TestAutoConfigurationApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(TestAutoConfigurationApplication.class, args);
        DemoService bean = run.getBean(DemoService.class);
        System.out.println(bean.getDate() + " === " + bean.getName());
    }
}
