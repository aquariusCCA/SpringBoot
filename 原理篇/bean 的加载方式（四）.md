---
up:
  - "[[SpringBoot 課程描述]]"
---
**使用@Import注解导入要注入的bean对应的字节码**

```java
@Configuration  
@Import(Dog.class)  
public class SpringConfig {  }
```

**被导入的bean无需使用注解声明为bean**

```java
public class Dog {  }
```

```java
@SpringBootApplication
public class TestSpringBootApplication {
    public static void main(String[] args) {
        ApplicationContext ctx =
                new AnnotationConfigApplicationContext(SpringConfig.class);
        System.out.println(ctx.getBean(Dog.class));
    }
}
```