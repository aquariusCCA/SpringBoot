---
up:
  - "[[SpringBoot 課程描述]]"
---
> **使用proxyBeanMethods=true可以保障调用此方法得到的对象是从容器中获取的而不是重新创建的**

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private String name;
    private String author;
    private Double price;
}
```

```java
@Configuration(proxyBeanMethods = true)
@ComponentScan("com.test")
public class SpringConfig {
    @Bean
    public Book book(){
        System.out.println("book init ...");
        return new Book();
    }
}
```

```java
@SpringBootApplication
public class TestSpringBootApplication {
    public static void main(String[] args) {
        ApplicationContext ctx =
                new AnnotationConfigApplicationContext(SpringConfig.class);
        SpringConfig config = ctx.getBean("springConfig", SpringConfig.class);
        config.book();
        config.book();
    }
}
```