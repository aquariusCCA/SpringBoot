---
up:
  - "[[SpringBoot 課程描述]]"
  - "[[NoSQL]]"
---
springboot整合redis技术提供了多种客户端兼容模式，默认提供的是lettucs客户端技术，也可以根据需要切换成指定客户端技术，例如jedis客户端技术，切换成jedis客户端技术操作步骤如下：

**步骤①**：导入jedis坐标

```xml
<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
</dependency>
```

> jedis坐标受springboot管理，无需提供版本号

**步骤②**：配置客户端技术类型，设置为jedis

```yaml
spring:
  redis:
    host: localhost
    port: 6379
    client-type: jedis
```

**步骤③**：根据需要设置对应的配置

```yaml
spring:
  redis:
    host: localhost
    port: 6379
    client-type: jedis
    lettuce:
      pool:
        max-active: 16
    jedis:
      pool:
        max-active: 16
```

> [!NOTE] **lettcus 与 jedis 区别**
> 
> - jedis连接Redis服务器是直连模式，当多线程模式下使用jedis会存在线程安全问题，解决方案可以通过配置连接池使每个连接专用，这样整体性能就大受影响
> -  lettcus基于Netty框架进行与Redis服务器连接，底层设计中采用StatefulRedisConnection。 StatefulRedisConnection自身是线程安全的，可以保障并发访问安全问题，所以一个连接可以被多线程复用。当然lettcus也支持多连接实例一起工作

> [!NOTE] **总结**
> 
> - springboot整合redis提供了StringRedisTemplate对象，以字符串的数据格式操作redis
> - 如果需要切换redis客户端实现技术，可以通过配置的形式进行
