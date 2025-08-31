package com.test.pojo.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MyTask {
    @Scheduled(cron = "0/3 * * * * ?")
    public void print() {
        System.out.println(Thread.currentThread().getName() + ": spring task run...");
    }
}