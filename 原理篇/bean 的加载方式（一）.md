---
up:
  - "[[SpringBoot 課程描述]]"
---
```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-context</artifactId>
</dependency>

<dependency>
	<groupId>com.alibaba</groupId>
	<artifactId>druid-spring-boot-starter</artifactId>
	<version>1.1.24</version>
</dependency>
```

```java
public interface BookService { }

public class BookServiceImpl implements BookService {  }
```

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
@SpringBootApplication
public class TestSpringBootApplication {
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
        System.out.println(ctx.getBean("bookService"));
        System.out.println(ctx.getBean("dataSource"));
        String[] names = ctx.getBeanDefinitionNames();
        for (String name : names) {
            System.out.println(name);
        }
    }
}
```