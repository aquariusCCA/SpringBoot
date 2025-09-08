---
up:
  - "[[SpringBoot 課程描述]]"
---

> [!NOTE] **參考文章**
> 
> - [SpringBoot自定义Starter作为依赖在其他项目引入](https://blog.csdn.net/imamanong/article/details/128706287)
> - [springboot自定义starter，实现自动装配](https://blog.csdn.net/weixin_40482816/article/details/136166185)

> [!NOTE] **自定义一个starter**
> 
> 先创建一个项目，在该项目中定义 Starter 的内容，然后通过 Maven 将其打成 jar 包，之后在另一个项目中使用该 Starter 。

# 创建一个maven 项目，并引入如下依赖：

创建一个 Maven 项目，在其 pom 文件中引入自动装配的依赖，并定义好 Starter 的名称。

`spring-boot-autoconfigure` 依赖是必须要引入的，`spring-boot-configuration-processor` 的引入是为了在配置文件中使用属性时有提示。

```xml
<!-- 自動配置基礎 -->  
<dependency>  
    <groupId>org.springframework.boot</groupId>  
    <artifactId>spring-boot-autoconfigure</artifactId>  
</dependency>  
  
<!-- 讓 IDE 產生 metadata（可選，但很有幫助） -->  
<dependency>  
    <groupId>org.springframework.boot</groupId>  
    <artifactId>spring-boot-configuration-processor</artifactId>  
    <optional>true</optional>  
</dependency>
```

# 新建一个 `Properties` 配置类

配置类用于保存外部化配置文件中定义的配置数据，其中配置文件包括 properties 或 yml 。

```java
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
```

# 新建一个功能类

> 功能类  主要用来返回 `DemoProperties` 中的 name 和 date 属性。

```java
public class DemoService {  
  
    private DemoProperties demoProperties;  
  
    public DemoService(DemoProperties demoProperties) {  
        this.demoProperties = demoProperties;  
    }  
  
    public String getName() {  
        return demoProperties.getName();  
    }  
  
    public String getDate() {  
        return demoProperties.getDate();  
    }  
}
```

# 创建自动配置类

 在该配置类中完成 Starter 的功能。这里，通过构造器注入 `DemoProperties` 配置类对象，并初始化 `DemoService` 功能类。

```java
@Configuration  
@EnableConfigurationProperties(value = DemoProperties.class)  
public class DemoAutoConfiguration {  
  
    private final DemoProperties demoProperties;  
  
    public DemoAutoConfiguration(DemoProperties demoProperties) {  
        this.demoProperties = demoProperties;  
    }  
  
    @Bean  
    // 当容器中没有 DemoService Bean 时，才注入默认的 DemoService    @ConditionalOnMissingBean(DemoService.class)  
    public DemoService demoService() {  
        return new DemoService(demoProperties);  
    }  
}
```

# 註冊自動配置

新建 `src/main/resources/META-INF/spring.factories` 写入DemoAutoConfiguration 全限定名。 **多个配置类逗号隔开，换行使用反斜杠。**

```xml
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\  
    com.loong.autoConfiguration.DemoAutoConfiguration
```

# 进行自定义提示功能开发

**新增** `src/main/resources/META-INF/additional-spring-configuration-metadata.json`

```json
{
  "properties": [
    {
      "name": "demo.name",
      "type": "java.lang.String",
      "description": "使用者名稱。",
      "defaultValue": "World"
    },
    {
      "name": "demo.date",
      "type": "java.lang.String",
      "description": "日期字串（格式 yyyy-MM-dd）。",
      "defaultValue": "1970-01-01"
    }
  ],
  "hints": [
    {
      "name": "demo.date",
      "values": [
        { "value": "2025-01-01", "description": "示例（yyyy-MM-dd）" }
      ]
    }
  ]
}
```

# 安裝到本機與使用

```shell
mvn clean install
```

### 在另一个项目中引入该 Starter 的 Maven 依赖

```xml
<dependency>
	<groupId>com.loong</groupId>
	<artifactId>demo-spring-boot-starter</artifactId>
	<version>0.0.1-SNAPSHOT</version>
</dependency>
```

### 在 yml 文件中定义配置数据

```yml
demo:
  name: kevin
  date: 2025-01-01
```

### 获取自动配置类

```java
@SpringBootApplication
public class TestAutoConfigurationApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(TestAutoConfigurationApplication.class, args);
        DemoService bean = run.getBean(DemoService.class);
        System.out.println(bean.getDate() + " === " + bean.getName());
    }
}
```

最后，查看控制台的输出。