---
up:
  - "[[SpringBoot 課程描述]]"
  - "[[NoSQL]]"
---
使用springboot整合MongDB该如何进行呢？其实springboot为什么使用的开发者这么多，就是因为他的套路几乎完全一样。导入坐标，做配置，使用API接口操作。整合Redis如此，整合MongoDB同样如此。

​		第一，先导入对应技术的整合starter坐标

​		第二，配置必要信息

​		第三，使用提供的API操作即可

​下面就开始springboot整合MongoDB，操作步骤如下：

**步骤①**：导入springboot整合MongoDB的starter坐标

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```

​上述坐标也可以在创建模块的时候通过勾选的形式进行选择，同样归属NoSQL分类中

![[NoSQL/附件/image-20220224120721626.png]]

**步骤②**：进行基础配置

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost/itheima
```

​操作MongoDB需要的配置与操作redis一样，最基本的信息都是操作哪一台服务器，区别就是连接的服务器IP地址和端口不同，书写格式不同而已。

**步骤③**：使用springboot整合MongoDB的专用客户端接口MongoTemplate来进行操作

```java
@SpringBootTest
class Springboot17MongodbApplicationTests {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Test
    void contextLoads() {
        Book book = new Book();
        book.setId(2);
        book.setName("springboot2");
        book.setType("springboot2");
        book.setDescription("springboot2");
        mongoTemplate.save(book);
    }
    @Test
    void find(){
        List<Book> all = mongoTemplate.findAll(Book.class);
        System.out.println(all);
    }
}
```

​整合工作到这里就做完了，感觉既熟悉也陌生。熟悉的是这个套路，三板斧，就这三招，导坐标做配置用API操作，陌生的是这个技术，里面具体的操作API可能会不熟悉，有关springboot整合MongoDB我们就讲到这里。有兴趣可以继续学习MongoDB的操作，然后再来这里通过编程的形式操作MongoDB。

> [!NOTE] **总结：springboot整合MongoDB步骤**
> 
> - 导入springboot整合MongoDB的starter坐标
> - 进行基础配置
> - 使用springboot整合MongoDB的专用客户端接口MongoTemplate操作
