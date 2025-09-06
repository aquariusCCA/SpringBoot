---
up:
  - "[[SpringBoot 課程描述]]"
---
**注解方式声明配置类**

```java
@Configuration
@ComponentScan("com.test")
public class SpringConfig {
    @Bean
    public DruidDataSource getDataSource(){
        DruidDataSource ds = new DruidDataSource();
        return ds;
    }
}
```

```java
public interface BookService {  }

@Service  
public class BookServiceImpl implements BookService {  }
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