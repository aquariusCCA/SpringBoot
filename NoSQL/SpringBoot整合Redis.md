---
up:
  - "[[SpringBoot 課程描述]]"
  - "[[NoSQL]]"
---
在进行整合之前先梳理一下整合的思想，springboot整合任何技术其实就是在springboot中使用对应技术的API。如果两个技术没有交集，就不存在整合的概念了。所谓整合其实就是使用springboot技术去管理其他技术，几个问题是躲不掉的。

​第一，需要先导入对应技术的坐标，而整合之后，这些坐标都有了一些变化

​第二，任何技术通常都会有一些相关的设置信息，整合之后，这些信息如何写，写在哪是一个问题

​第三，没有整合之前操作如果是模式A的话，整合之后如果没有给开发者带来一些便捷操作，那整合将毫无意义，所以整合后操作肯定要简化一些，那对应的操作方式自然也有所不同

按照上面的三个问题去思考springboot整合所有技术是一种通用思想，在整合的过程中会逐步摸索出整合的套路，而且适用性非常强，经过若干种技术的整合后基本上可以总结出一套固定思维。

​下面就开始springboot整合redis，操作步骤如下：

**步骤①**：导入springboot整合redis的starter坐标

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

​上述坐标可以在创建模块的时候通过勾选的形式进行选择，归属NoSQL分类中

![[NoSQL/附件/image-20220224101142220.png]]

**步骤②**：进行基础配置

```yaml
spring:
  redis:
    host: localhost
    port: 6379
```

​操作redis，最基本的信息就是操作哪一台redis服务器，所以服务器地址属于基础配置信息，不可缺少。但是即便你不配置，目前也是可以用的。因为以上两组信息都有默认配置，刚好就是上述配置值。

**步骤③**：使用springboot整合redis的专用客户端接口操作，此处使用的是RedisTemplate

```java
@SpringBootTest
public class RedisTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void set() {
        ValueOperations ops = redisTemplate.opsForValue();
        ops.set("age",41);
    }
    @Test
    void get() {
        ValueOperations ops = redisTemplate.opsForValue();
        Object age = ops.get("age");
        System.out.println(age);
    }
    @Test
    void hset() {
        HashOperations ops = redisTemplate.opsForHash();
        ops.put("info","b","bb");
    }
    @Test
    void hget() {
        HashOperations ops = redisTemplate.opsForHash();
        Object val = ops.get("info", "b");
        System.out.println(val);
    }
}
```

在操作redis时，需要先确认操作何种数据，根据数据种类得到操作接口。例如使用 opsForValue()获取 string类型的数据操作接口，使用 opsForHash() 获取 hash 类型的数据操作接口，剩下的就是调用对应 api 操作了。各种类型的数据操作接口如下：

![[NoSQL/附件/image-20220224103104908.png]]

> 注：这里如果使用`RedisTemplate`而不使用`StringRedisTemplate`，去`redis`客户端里面查看会发现键值包含 `\xac\xed\x00\x05t\x00\` 特殊字符，这是由于`RedisTemplate<K, V>`模板类在操作`redis`时默认使用`JdkSerializationRedisSerializer`来进行序列化（看起來像亂碼）。

将序列化的方式改为 `org.springframework.data.redis.serializer.StringRedisSerializer` 会自动去掉 `\xac\xed\x00\x05t\x00` 前缀，所以有两种解决方法：

1. 直接使用`StringRedisTemplate`；
    
2. 方案2 修改默认的序列化方式：
    
```java
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory cf) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(cf);

        // Key 一律用 String，避免可讀性與相容性問題
        StringRedisSerializer stringSer = new StringRedisSerializer();
        template.setKeySerializer(stringSer);
        template.setHashKeySerializer(stringSer);
        template.setValueSerializer(stringSer);
        template.setHashValueSerializer(stringSer);

        return template;
    }
}
```

> [!NOTE] **总结：springboot整合redis步骤**
> 
> - 导入springboot整合redis的starter坐标
> - 进行基础配置
> - 使用springboot整合redis的专用客户端接口RedisTemplate操作
