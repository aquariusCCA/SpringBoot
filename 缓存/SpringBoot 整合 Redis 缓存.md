---
up:
  - "[[SpringBoot 課程描述]]"
---
上节使用Ehcache替换了springboot内置的缓存技术，其实springboot支持的缓存技术还很多，下面使用redis技术作为缓存解决方案来实现手机验证码案例。

​比对使用 Ehcache 的过程，加坐标，改缓存实现类型为ehcache，做Ehcache的配置。如果还成redis做缓存呢？一模一样，加坐标，改缓存实现类型为redis，做redis的配置。差别之处只有一点，redis的配置可以在yml文件中直接进行配置，无需制作独立的配置文件。

**步骤①**：导入redis的坐标

```xml
<dependency>  
    <groupId>org.springframework.boot</groupId>  
    <artifactId>spring-boot-starter-cache</artifactId>  
</dependency>  
<dependency>  
    <groupId>org.springframework.boot</groupId>  
    <artifactId>spring-boot-starter-data-redis</artifactId>  
</dependency>
```

**步骤②**：配置缓存技术实现使用redis

```yaml
spring:
  redis:
    host: localhost
    port: 6379
  cache:
    type: redis
```

​如果需要对redis作为缓存进行配置，注意不是对原始的redis进行配置，而是配置redis作为缓存使用相关的配置，隶属于spring.cache.redis节点下，注意不要写错位置了。

```yaml
spring:
  redis:
    host: localhost
    port: 6379
  cache:
    type: redis
    redis:
      use-key-prefix: false
      key-prefix: sms_
      cache-null-values: false
      time-to-live: 10s
```