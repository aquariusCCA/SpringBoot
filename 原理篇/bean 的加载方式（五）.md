---
up:
  - "[[SpringBoot 課程描述]]"
---
**使用上下文对象在容器初始化完毕后注入bean**

```java
@SpringBootApplication
public class TestSpringBootApplication {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext(SpringConfig.class);
        ctx.register(Dog.class);
        String[] names = ctx.getBeanDefinitionNames();
        for (String name : names) {
            System.out.println(name);
        }
    }
}
```

```java
public class Dog {  }
```