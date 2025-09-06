---
up:
  - "[[SpringBoot 課程描述]]"
---
**导入实现了ImportBeanDefinitionRegistrar接口的类，通过BeanDefinition的注册器注册实名bean，实现对容器中bean的裁定，例如对现有bean的覆盖，进而达成不修改源代码的情况下更换实现的效果**

```java
public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    public void registerBeanDefinitions(AnnotationMetadata metadata,
                                        BeanDefinitionRegistry registry) {

        // 檢查當前類上是否有指定的註解
        boolean flag = metadata.hasAnnotation("org.springframework.context.annotation.ComponentScan");
        System.out.println(flag);

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
@Import(MyImportBeanDefinitionRegistrar.class)
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