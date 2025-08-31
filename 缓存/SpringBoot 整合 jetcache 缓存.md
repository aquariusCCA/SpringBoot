---
up:
  - "[[SpringBoot 課程描述]]"
參考文章: https://juejin.cn/post/7247151913437937701#heading-5, https://github.com/alibaba/jetcache
---
目前我们使用的缓存都是要么A要么B，能不能AB一起用呢？这一节就解决这个问题。

springboot针对缓存的整合仅仅停留在用缓存上面，如果缓存自身不支持同时支持AB一起用，springboot也没办法，所以要想解决AB缓存一起用的问题，就必须找一款缓存能够支持AB两种缓存一起用，有这种缓存吗？还真有，阿里出品，jetcache。

​jetcache严格意义上来说，并不是一个缓存解决方案，只能说他算是一个缓存框架，然后把别的缓存放到jetcache中管理，这样就可以支持AB缓存一起用了。并且jetcache参考了springboot整合缓存的思想，整体技术使用方式和springboot的缓存解决方案思想非常类似。下面咱们就先把jetcache用起来，然后再说它里面的一些小的功能。

​做之前要先明确一下，jetcache并不是随便拿两个缓存都能拼到一起去的。目前jetcache支持的缓存方案本地缓存支持两种，远程缓存支持两种，分别如下：

- 本地缓存（Local）
	- LinkedHashMap
	- Caffeine
- 远程缓存（Remote）
	- Redis
	- Tair

​其实也有人问我，为什么jetcache只支持2+2这么4款缓存呢？阿里研发这个技术其实主要是为了满足自身的使用需要。最初肯定只有1+1种，逐步变化成2+2种。下面就以LinkedHashMap+Redis的方案实现本地与远程缓存方案同时使用。

---

# 纯远程方案

**步骤 1**：导入springboot整合jetcache对应的坐标starter，当前坐标默认使用的远程方案是redis

```xml
<dependency>  
    <groupId>com.alicp.jetcache</groupId>  
    <artifactId>jetcache-starter-redis</artifactId>  
    <version>2.7.8</version>  
</dependency>
```

**步骤 2**：远程方案基本配置

```yaml
jetcache:
  remote:
    default:
      type: redis
      host: localhost
      port: 6379
      poolConfig:
        maxTotal: 50
```

​其中poolConfig是必配项，否则会报错

**步骤 3**：启用缓存

```java
@Configuration  
//jetcache启用缓存的主开关  
@EnableMethodCache(basePackages = "com.test") // 必加  
public class JetcacheConfig {  
  
    @Autowired  
    private CacheManager cacheManager;  
    private Cache<String ,String> jetCache;  
  
    @PostConstruct  
    public void init(){  
        QuickConfig qc = QuickConfig.newBuilder("jetCache_")  
                .expire(Duration.ofSeconds(3600))  
                .cacheType(CacheType.REMOTE)  
                // 本地缓存更新后，将在所有的节点中删除缓存，以保持强一致性  
                .syncLocal(false)  
                .build();  
        jetCache = cacheManager.getOrCreateCache(qc);  
    }  
  
    @Bean  
    public Cache<String ,String> getJetCache(){  
        return jetCache;  
    }  
}
```

**步骤 5**：使用Cache对象的API操作缓存，put写缓存，get读缓存。

```java
@Service  
public class SMSCodeServiceImpl implements SMSCodeService {  
    @Autowired  
    private CodeUtils codeUtils;  
  
    @Autowired  
    private Cache<String ,String> jetCache;  
  
    @Override  
    public String sendCodeToSMS(String tele) {  
        String code = codeUtils.generateCode(tele);  
        jetCache.put(tele,code);  
        return code;  
    }  
  
    @Override  
    public boolean checkCode(SMSCode smsCode) {  
        String code = jetCache.get(smsCode.getPhone());  
        return smsCode.getCode().equals(code);  
    }  
}
```

​通过上述jetcache使用远程方案连接redis可以看出，jetcache操作缓存时的接口操作更符合开发者习惯，使用缓存就先获取缓存对象Cache，放数据进去就是put，取数据出来就是get，更加简单易懂。并且jetcache操作缓存时，可以为某个缓存对象设置过期时间，将同类型的数据放入缓存中，方便有效周期的管理。

​上述方案中使用的是配置中定义的default缓存，其实这个default是个名字，可以随便写，也可以随便加。例如再添加一种缓存解决方案，参照如下配置进行：

```yaml
jetcache:
  remote:
    default:
      type: redis
      host: localhost
      port: 6379
      poolConfig:
        maxTotal: 50
    sms:
      type: redis
      host: localhost
      port: 6379
      poolConfig:
        maxTotal: 50
```

```java
@Configuration  
//jetcache启用缓存的主开关  
@EnableMethodCache(basePackages = "com.test") // 必加 
public class JetcacheConfig {  
  
    @Autowired  
    private CacheManager cacheManager;  
  
    @Bean("jetCacheDefault")  
    @Primary  
    public Cache<String, String> jetCacheDefault() {  
        // 對應 jetcache.remote.default       
        QuickConfig qc = QuickConfig.newBuilder("jetCache_")  
                .expire(Duration.ofSeconds(3600))  
                .cacheType(CacheType.REMOTE)  
                .build();  
        return cacheManager.getOrCreateCache(qc);
    }  
  
    @Bean("jetCacheSms")  
    public Cache<String, String> jetCacheSms() {  
        // 對應 jetcache.remote.sms        
        QuickConfig qc = QuickConfig.newBuilder("sms_")  
                .expire(Duration.ofMinutes(10)) // 例如 SMS 只保 10 分鐘  
                .cacheType(CacheType.REMOTE)  
                .build();  
        return cacheManager.getOrCreateCache(qc);
    }  
}
```

​如果想使用名称是sms的缓存，需要再创建缓存时指定参数area，声明使用对应缓存即可

```JAVA
@Service  
public class SMSCodeServiceImpl implements SMSCodeService {  
    @Autowired  
    private CodeUtils codeUtils;  
  
    @Autowired  
    @Qualifier("jetCacheSms")  
    private Cache<String, String> smsCache;  
  
    @Override  
    public String sendCodeToSMS(String tele) {  
        String code = codeUtils.generateCode(tele);  
        smsCache.put(tele,code);  
        return code;  
    }  
  
    @Override  
    public boolean checkCode(SMSCode smsCode) {  
        String code = smsCache.get(smsCode.getPhone());  
        return smsCode.getCode().equals(code);  
    }  
}
```

---

# 纯本地方案

​远程方案中，配置中使用remote表示远程，换成local就是本地，只不过类型不一样而已。

**步骤 1**：导入springboot整合jetcache对应的坐标starter

```xml
<dependency>  
    <groupId>com.alicp.jetcache</groupId>  
    <artifactId>jetcache-starter-redis</artifactId>  
    <version>2.7.8</version>  
</dependency>
```

**步骤 2**：本地缓存基本配置

```yaml
jetcache:
  local:
    default:
      type: linkedhashmap
      keyConvertor: fastjson
```

​为了加速数据获取时key的匹配速度，jetcache要求指定key的类型转换器。简单说就是，如果你给了一个Object作为key的话，我先用key的类型转换器给转换成字符串，然后再保存。等到获取数据时，仍然是先使用给定的Object转换成字符串，然后根据字符串匹配。由于jetcache是阿里的技术，这里推荐key的类型转换器使用阿里的fastjson。

**步骤③**：启用缓存

```java
@Configuration  
//jetcache启用缓存的主开关  
@EnableMethodCache(basePackages = "com.test") // 必加  
public class JetcacheConfig {  
  
    @Autowired  
    private CacheManager cacheManager;  
  
    private Cache<String ,String> jetCache;  
  
    @PostConstruct  
    public void init() {  
        QuickConfig qc = QuickConfig.newBuilder("jetCache_")  
                .cacheType(CacheType.LOCAL)          // 只用本地  
                .expire(Duration.ofMinutes(10))      // TTL  
                .localLimit(1000)                    // LRU 上限  
                .build();  
        jetCache = cacheManager.getOrCreateCache(qc);  
    }  
  
    @Bean  
    public Cache<String ,String> getJetCache(){  
        return jetCache;  
    }  
}
```

**步骤④**：创建缓存对象Cache时，标注当前使用本地缓存

```java
@Service  
public class SMSCodeServiceImpl implements SMSCodeService {  
    @Autowired  
    private CodeUtils codeUtils;  
  
    @Autowired  
    private Cache<String, String> jetCache;  
  
    @Override  
    public String sendCodeToSMS(String tele) {  
        String code = codeUtils.generateCode(tele);  
        jetCache.put(tele,code);  
        return code;  
    }  
  
    @Override  
    public boolean checkCode(SMSCode smsCode) {  
        String code = jetCache.get(smsCode.getPhone());  
        return smsCode.getCode().equals(code);  
    }  
}
```

---

# 本地+远程方案

​本地和远程方法都有了，两种方案一起使用如何配置呢？其实就是将两种配置合并到一起就可以了。

```YAML
jetcache:  
  local:  
    default:  
      type: linkedhashmap  
      keyConvertor: fastjson  
  remote:  
    default:  
      type: redis  
      host: localhost  
      port: 6379  
      poolConfig:  
        maxTotal: 50
```

​在创建缓存的时候，配置cacheType为BOTH即则本地缓存与远程缓存同时使用。

```java
//jetcache启用缓存的主开关  
@EnableMethodCache(basePackages = "com.test") // 必加  
public class JetcacheConfig {  
  
    @Autowired  
    private CacheManager cacheManager;  
    private Cache<String ,String> jetCache;  
  
    @PostConstruct  
    public void init(){  
        QuickConfig qc = QuickConfig.newBuilder("jetCache_")  
                .expire(Duration.ofSeconds(3600))  
                .cacheType(CacheType.BOTH)  
                // 本地缓存更新后，将在所有的节点中删除缓存，以保持强一致性  
                .syncLocal(false)  
                .build();  
        jetCache = cacheManager.getOrCreateCache(qc);  
    }  
  
    @Bean  
    public Cache<String ,String> getJetCache(){  
        return jetCache;  
    }  
}
```

---

# **方法缓存**

> 以上方案仅支持手工控制缓存，但是springcache方案中的方法缓存特别好用，给一个方法添加一个注解，方法就会自动使用缓存。jetcache也提供了对应的功能，即方法缓存。

​jetcache提供了方法缓存方案，只不过名称变更了而已。在对应的操作接口上方使用注解@Cached即可

**步骤①**：导入springboot整合jetcache对应的坐标starter

```xml
<dependency>  
    <groupId>com.alicp.jetcache</groupId>  
    <artifactId>jetcache-starter-redis</artifactId>  
    <version>2.7.8</version>  
</dependency>
```

**步骤②**：配置缓存

```yaml
jetcache:
  local:
    default:
      type: linkedhashmap
      keyConvertor: fastjson
  remote:
    default:
      type: redis
      host: localhost
      port: 6379
      keyConvertor: fastjson
      valueEncode: java
      valueDecode: java
      poolConfig:
        maxTotal: 50
```

​由于redis缓存中不支持保存对象，因此需要对redis设置当Object类型数据进入到redis中时如何进行类型转换。需要配置keyConvertor表示key的类型转换方式，同时标注value的转换类型方式，值进入redis时是java类型，标注valueEncode为java，值从redis中读取时转换成java，标注valueDecode为java。

> [!NOTE] **注意**​
> - 为了实现Object类型的值进出redis，需要保障进出redis的Object类型的数据必须实现序列化接口。 

```JAVA
@Data  
@NoArgsConstructor  
@AllArgsConstructor  
public class Book implements Serializable {  
    private Integer id;  
    private String type;  
    private String name;  
    private String description;  
}
```

**步骤③**：启用缓存时开启方法缓存功能，并配置basePackages，说明在哪些包中开启方法缓存

```java
@Configuration  
//jetcache启用缓存的主开关  
@EnableMethodCache(basePackages = "com.test")  
public class JetcacheConfig {  
}
```

**步骤④**：使用注解@Cached标注当前方法使用缓存

```java
@Service  
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements IBookService {  
    @Override  
    @Cached(name="book_",key="#id",expire = 3600,cacheType = CacheType.REMOTE)  
    public Book getById(Serializable id) {  
        System.out.println("id = " + id);  
        return super.getById(id);  
    }  
}
```

---

# 远程方案的数据同步

​由于远程方案中redis保存的数据可以被多个客户端共享，这就存在了数据同步问题。jetcache提供了3个注解解决此问题，分别在更新、删除操作时同步缓存数据，和读取缓存时定时刷新数据

**更新缓存：写库后同步更新缓存**

```JAVA
@CacheUpdate(name="book_",key="#book.id",value="#book")  
public boolean update(Book book) {  
    return bookMapper.updateById(book) > 0;  
}
```

> `@CacheUpdate` 在方法执行后以指定 `key` 与 `value` 直接覆盖缓存条目，实现“写后读一致”的快速可见。

**删除缓存：写库后失效缓存**

```JAVA
@CacheInvalidate(name="book_",key = "#id")  
public boolean delete(Serializable id) {  
    return bookMapper.deleteById(id) > 0;  
}
```

**定时刷新缓存**

```JAVA
@Cached(name="book_",key="#id",expire = 3600,cacheType = CacheType.REMOTE)  
@CacheRefresh(refresh = 5)  
public Book getById(Serializable id) {  
    return bookMapper.selectById(id);  
}
```

---

# 数据报表

​jetcache还提供有简单的数据报表功能，帮助开发者快速查看缓存命中信息，只需要添加一个配置即可

```yaml
jetcache:
  statIntervalMinutes: 1
```

​设置后，每1分钟在控制台输出缓存数据命中信息

```shell
[DefaultExecutor] c.alicp.jetcache.support.StatInfoLogger  : jetcache stat from 2022-02-28 09:32:15,892 to 2022-02-28 09:33:00,003
cache    |    qps|   rate|   get|    hit|   fail|   expire|   avgLoadTime|   maxLoadTime
---------+-------+-------+------+-------+-------+---------+--------------+--------------
book_    |   0.66| 75.86%|    29|     22|      0|        0|          28.0|           188
---------+-------+-------+------+-------+-------+---------+--------------+--------------
```

---