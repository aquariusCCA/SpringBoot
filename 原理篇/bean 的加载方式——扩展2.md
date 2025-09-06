---
up:
  - "[[SpringBoot 課程描述]]"
---
**加载配置类并加载配置文件（系统迁移）**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <!--声明自定义bean-->
    <bean id="bookService"
          class="com.test.service.impl.BookServiceImpl"
          scope="singleton"/>

    <!--声明第三方开发bean-->
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource"/>
</beans>
```

```java
public interface BookService {  }

public class BookServiceImpl implements BookService {  }
```

```java
@Configuration
@ComponentScan("com.test")
@ImportResource("classpath:beans.xml")
public class SpringConfig {  }
```

```java
@SpringBootApplication
public class TestSpringBootApplication {
    public static void main(String[] args) {
        ApplicationContext ctx =
                new AnnotationConfigApplicationContext(SpringConfig.class);
        String[] names = ctx.getBeanDefinitionNames();
        for (String name : names) {
            System.out.println(name);
        }
    }
}
```