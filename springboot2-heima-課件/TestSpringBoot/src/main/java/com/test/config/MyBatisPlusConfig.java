package com.test.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.test.mapper")
public class MyBatisPlusConfig {
}
