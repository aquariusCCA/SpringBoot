---
up:
  - "[[SpringBoot 課程描述]]"
---
**初始化实现FactoryBean接口的类，实现对bean加载到容器之前的批处理操作**

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
public class BookFactoryBean implements FactoryBean<Book> {
    public Book getObject() throws Exception {
        // 进行book对象相关的初始化工作
        Book book = new Book("西游记", "吴承恩", 99.9);
        return book;
    }
    public Class<?> getObjectType() {
        return Book.class;
    }
}
```

```java
@Configuration
@ComponentScan("com.test")
public class SpringConfig {
    @Bean
    public BookFactoryBean book(){
        return new BookFactoryBean();
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