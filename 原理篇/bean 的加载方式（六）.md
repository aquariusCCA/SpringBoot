---
up:
  - "[[SpringBoot 課程描述]]"
---
**导入实现了 ImportSelector 接口的类，实现对导入源的编程式处理**

```java
    public String[] selectImports(AnnotationMetadata metadata) {
        boolean flag = metadata.hasAnnotation("org.springframework.context.annotation.ComponentScan");
        if(flag){
            return new String[]{"com.test.pojo.Dog"};
        }
        return new String[]{"com.test.pojo.Cat"};
    }
}
```

```java
@Configuration
@ComponentScan("com.test")
@Import(MyImportSelector.class)
public class SpringConfig {  }
```

```java
@SpringBootApplication
public class TestSpringBootApplication {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext(SpringConfig.class);
        String[] names = ctx.getBeanDefinitionNames();
        for (String name : names) {
            System.out.println(name);
        }
    }
}
```