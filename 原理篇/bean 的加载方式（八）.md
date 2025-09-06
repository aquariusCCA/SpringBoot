---
up:
  - "[[SpringBoot 課程描述]]"
---
**导入实现了 BeanDefinitionRegistryPostProcessor 接口的类，通过 BeanDefinition 的注册器注册实名 bean，实现对容器中 bean 的最终裁定**

```java
public class MyPostProcessor implements BeanDefinitionRegistryPostProcessor {
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        BeanDefinition beanDefinition = BeanDefinitionBuilder
                .rootBeanDefinition(BookServiceImpl.class)
                .getBeanDefinition();
        registry.registerBeanDefinition("bookService", beanDefinition);
    }
}
```

```java
@Configuration
@ComponentScan("com.test")
@Import(MyPostProcessor.class)
public class SpringConfig {  }
```

```java
public interface BookService {  }

public class BookServiceImpl implements BookService {  }
```

```java
@SpringBootApplication
public class TestSpringBootApplication {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext(SpringConfig.class);
        System.out.println(ctx.getBean("bookService"));
    }
}
```