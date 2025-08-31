package com.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class J2CacheConfig extends net.oschina.j2cache.J2CacheConfig {

    @Bean(destroyMethod = "close")
    public net.oschina.j2cache.CacheChannel cacheChannel() {
        var builder = net.oschina.j2cache.J2CacheBuilder.init(new J2CacheConfig()); // classpath
        return builder.getChannel();
    }
}
