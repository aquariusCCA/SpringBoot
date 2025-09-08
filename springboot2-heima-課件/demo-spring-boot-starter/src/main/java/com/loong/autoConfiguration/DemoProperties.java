package com.loong.autoConfiguration;

import org.springframework.boot.context.properties.ConfigurationProperties;

// 定义配置文件中的属性前缀
@ConfigurationProperties(prefix = "demo")
public class DemoProperties {

    /**
     * 使用者名稱；預設 "World"。
     */
    private String name = "World";

    /**
     * 日期字串（建議格式：yyyy-MM-dd）；預設 "1970-01-01"。
     */
    private String date = "1970-01-01";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}