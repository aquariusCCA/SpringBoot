---
up:
  - "[[SpringBoot 課程描述]]"
---
**使用@Import注解导入配置类**

```java
@Configuration
@Import(DbConfig.class)
@ComponentScan("com.test")
public class SpringConfig {  }
```

```java
@Configuration
public class DbConfig {
    @Bean
    public DruidDataSource getDataSource(){
        DruidDataSource ds = new DruidDataSource();
        return ds;
    }
}
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