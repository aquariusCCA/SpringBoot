---
up:
  - "[[SpringBoot 課程描述]]"
---
**XML+注解方式声明bean**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">
    <context:component-scan base-package="com.test"/>
</beans>
```

**使用@Component及其衍生注解@Controller 、@Service、@Repository定义bean**

```java
public interface BookService {  }

@Service
public class BookServiceImpl implements BookService {  }
```

**使用@Bean定义第三方bean，并将所在类定义为配置类或Bean**

```java
@Component
public class DbConfig {
    @Bean
    public DruidDataSource getDataSource(){
        DruidDataSource ds = new DruidDataSource();
        return ds;
    }
}
```

**測試**

```java
@SpringBootApplication
public class TestSpringBootApplication {
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
        String[] names = ctx.getBeanDefinitionNames();
        for (String name : names) {
            System.out.println(name);
        }
    }
}
```