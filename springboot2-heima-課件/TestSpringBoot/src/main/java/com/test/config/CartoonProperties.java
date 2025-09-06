package com.test.config;

import com.test.pojo.Cat;
import com.test.pojo.Mouse;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
//类与配置文件绑定
@ConfigurationProperties(prefix = "cartoon")
public class CartoonProperties {
    private Cat cat;
    private Mouse mouse;
}