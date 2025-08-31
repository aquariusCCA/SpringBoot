---
up:
  - "[[SpringBoot 課程描述]]"
---
手机验证码的案例已经完成了，下面就开始springboot整合各种各样的缓存技术，第一个整合Ehcache 技术。

Ehcache是一种缓存技术，使用springboot整合Ehcache其实就是变更一下缓存技术的实现方式，话不多说，直接开整

**步骤①**：导入Ehcache的坐标

```xml
<dependency>  
    <groupId>org.springframework.boot</groupId>  
    <artifactId>spring-boot-starter-cache</artifactId>  
</dependency>
<dependency>
	<groupId>org.ehcache</groupId>
	<artifactId>ehcache</artifactId>
	<classifier>jakarta</classifier>
</dependency>
```

​此处为什么不是导入Ehcache的starter，而是导入技术坐标呢？其实springboot整合缓存技术做的是通用格式，不管你整合哪种缓存技术，只是实现变化了，操作方式一样。这也体现出springboot技术的优点，统一同类技术的整合方式。

**步骤②**：配置缓存技术实现使用Ehcache

```yaml
spring:  
  cache:  
    type: jcache         # 明確指定（可選，但我建議）  
    jcache:  
      config: classpath:ehcache.xml   # 放在 src/main/resources 根目錄
```

​由于ehcache的配置有独立的配置文件格式，因此还需要指定ehcache的配置文件，以便于读取相应配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<config xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns="http://www.ehcache.org/v3"
        xmlns:jsr107="http://www.ehcache.org/v3/jsr107"
        xsi:schemaLocation="
          http://www.ehcache.org/v3 http://www.ehcache.org/schema/ehcache-core-3.0.xsd
          http://www.ehcache.org/v3/jsr107 http://www.ehcache.org/schema/ehcache-107-ext-3.0.xsd">

    <!-- 持久化磁碟目錄 -->
    <persistence directory="/Users/xiaoshilin/ehcache-data"/>

    <cache alias="smsCode">
        <key-type>java.lang.String</key-type>
        <value-type>java.lang.String</value-type>
        <expiry>
            <ttl unit="minutes">10</ttl>
        </expiry>
        <resources>
            <heap unit="entries">1000</heap>
            <offheap unit="MB">64</offheap>
            <disk unit="MB" persistent="true">256</disk>
        </resources>
    </cache>
</config>

```

​注意前面的案例中，设置了数据保存的位置是 smsCode

```java
@CachePut(value = "smsCode", key = "#tele")
public String sendCodeToSMS(String tele) {
    String code = codeUtils.generator(tele);
    return code;
}	
```
​
这个设定需要保障ehcache中有一个缓存空间名称叫做smsCode的配置，前后要统一。在企业开发过程中，通过设置不同名称的cache来设定不同的缓存策略，应用于不同的缓存数据。

​到这里springboot整合Ehcache就做完了，可以发现一点，原始代码没有任何修改，仅仅是加了一组配置就可以变更缓存供应商了，这也是springboot提供了统一的缓存操作接口的优势，变更实现并不影响原始代码的书写。
​		