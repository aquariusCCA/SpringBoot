package com.test.config;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Component
public class InfoConfig implements InfoContributor {
    private final long startTime = ManagementFactory.getRuntimeMXBean().getStartTime();

    @Override
    public void contribute(Info.Builder builder) {
        long uptimeMs = System.currentTimeMillis() - startTime;

        builder.withDetail("app", Map.of(
                "name", "Test Application",
                "uptime", Duration.ofMillis(uptimeMs).toString()
        ));

        builder.withDetail("company", Map.of(
                "name", "ACME",
                "owner", "SIAO"
        ));
    }
}