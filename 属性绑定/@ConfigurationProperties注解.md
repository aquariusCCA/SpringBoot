---
up:
  - "[[SpringBoot 課程描述]]"
---
# 自定义 bean

在基础篇学习了 [[读取对象数据|@ConfigurationProperties注解]]，此注解的作用是用来为bean绑定属性的。开发者可以在yml配置文件中以对象的格式添加若干属性

```YML
servers:
  ip-address: 192.168.0.1 
  port: 2345
  timeout: -1
```

​然后再开发一个用来封装数据的实体类，注意要提供属性对应的setter方法

```JAVA
@Component
@Data
public class ServerConfig {
    private String ipAddress;
    private int port;
    private long timeout;
}
```

​使用@ConfigurationProperties注解就可以将配置中的属性值关联到开发的模型类上

```JAVA
@Component
@Data
@ConfigurationProperties(prefix = "servers")
public class ServerConfig {
    private String ipAddress;
    private int port;
    private long timeout;
}
```

​这样加载对应bean的时候就可以直接加载配置属性值了。但是目前我们学的都是给自定义的bean使用这种形式加载属性值，如果是第三方的bean呢？能不能用这种形式加载属性值呢？为什么会提出这个疑问？原因就在于当前@ConfigurationProperties注解是写在类定义的上方，而第三方开发的bean源代码不是你自己书写的，你也不可能到源代码中去添加@ConfigurationProperties注解，这种问题该怎么解决呢？下面就来说说这个问题。

​使用@ConfigurationProperties注解其实可以为第三方bean加载属性，格式特殊一点而已。

---

# 第三方 bean

配置类中写一个返回该 bean 的方法，并在方法上注解 @Bean 与 @ConfigurationProperties 即可

以 Druid 连接池中的 DruidDataSource 为例

注意导入 druid 依赖而非 druid-spring-boot-starter

yaml 配置如下：

```yml
datasource:
  driver-class-name: com.mysql.cj.jdbc.Driver
```

我们将启动类作为配置类来测试：

```java
@SpringBootApplication
public class TestSpringBootApplication {

    @Bean
    @ConfigurationProperties(prefix = "datasource")
    public DruidDataSource druidDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("noting");
        return dataSource;
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(TestSpringBootApplication.class, args);

        DruidDataSource dataSource = applicationContext.getBean(DruidDataSource.class);
        System.out.println(dataSource.getDriverClassName());
        // 输出：com.mysql.cj.jdbc.Driver
    }
}
```

注意 @Bean 方法里对属性 set 不会影响配置文件中的配置

如上例中，方法内调用了 setDriverClassName 方法，但输出的依然是 yaml 配置中的属性值

可以把 @Bean 方法中的 set 方法给定的值当作默认配置，而 yaml 文件中的是最终配置

---

# @EnableConfigurationProperties 注解

​做到这其实就出现了一个新的问题，目前我们定义bean不是通过类注解定义就是通过@Bean定义，使用@ConfigurationProperties注解可以为bean进行属性绑定，那在一个业务系统中，哪些bean通过注解@ConfigurationProperties去绑定属性了呢？因为这个注解不仅可以写在类上，还可以写在方法上，所以找起来就比较麻烦了。为了解决这个问题，spring给我们提供了一个全新的注解，专门标注使用@ConfigurationProperties注解绑定属性的bean是哪些。这个注解叫做@EnableConfigurationProperties。具体如何使用呢？

**步骤①**：在配置类上开启@EnableConfigurationProperties注解，并标注要使用@ConfigurationProperties注解绑定属性的类

```java
@SpringBootApplication
@EnableConfigurationProperties(ServerConfig.class)
public class Springboot13ConfigurationApplication {
}
```

**步骤②**：在对应的类上直接使用@ConfigurationProperties进行属性绑定

```JAVA
@Data
@ConfigurationProperties(prefix = "servers")
public class ServerConfig {
    private String ipAddress;
    private int port;
    private long timeout;
}
```

​有人感觉这没区别啊？注意观察，现在绑定属性的ServerConfig类并没有声明@Component注解。当使用@EnableConfigurationProperties注解时，spring会默认将其标注的类定义为bean，因此无需再次声明@Component注解了。

​最后再说一个小技巧，使用@ConfigurationProperties注解时，会出现一个提示信息

![[属性绑定/附件/image-20220222145535749.png]]

​出现这个提示后只需要添加一个坐标此提醒就消失了

```XML
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
</dependency>
```

> [!NOTE] **总结**
> 
> 1. 使用@ConfigurationProperties可以为使用@Bean声明的第三方bean绑定属性
> 2. 当使用@EnableConfigurationProperties声明进行属性绑定的bean后，无需使用@Component注解再次进行bean声明

---

# 參考文章

- [SpringBoot进阶-第三方bean属性绑定](https://blog.csdn.net/cey_tao/article/details/127584000)