package com.loong.autoConfiguration;

import com.loong.service.DemoService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = DemoProperties.class)
public class DemoAutoConfiguration {

    private final DemoProperties demoProperties;

    public DemoAutoConfiguration(DemoProperties demoProperties) {
        this.demoProperties = demoProperties;
    }

    @Bean
    // 当容器中没有 DemoService Bean 时，才注入默认的 DemoService
    @ConditionalOnMissingBean(DemoService.class)
    public DemoService demoService() {
        return new DemoService(demoProperties);
    }
}