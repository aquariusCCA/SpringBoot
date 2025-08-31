---
up:
  - "[[SpringBoot 課程描述]]"
參考文章: https://blog.csdn.net/mng123/article/details/145849618
---
目前我们已经掌握了3种缓存解决方案的配置形式，分别是springboot内置缓存，ehcache和redis，本节研究一下国内比较流行的一款缓存memcached。

​按照之前的套路，其实变更缓存并不繁琐，但是springboot并没有支持使用memcached作为其缓存解决方案，也就是说在type属性中没有memcached的配置选项，这里就需要更变一下处理方式了。在整合之前先安装memcached。

---

# **安装**

​windows 版安装包下载地址：https://www.runoob.com/memcached/window-install-memcached.html

​下载的安装包是解压缩就能使用的zip文件，解压缩完毕后会得到如下文件

![[缓存/附件/image-20220226174957040.png]]

​可执行文件只有一个 memcached.exe，使用该文件可以将 memcached 作为系统服务启动，执行此文件时会出现报错信息，如下：

![[缓存/附件/image-20220226175141986.png]]

​此处出现问题的原因是注册系统服务时需要使用管理员权限，当前账号权限不足导致安装服务失败，切换管理员账号权限启动命令行

![[缓存/附件/image-20220226175302903.png]]

​然后再次执行安装服务的命令即可，如下：

```CMD
memcached.exe -d install
```

​服务安装完毕后可以使用命令启动和停止服务，如下：

```cmd
memcached.exe -d start		# 启动服务
memcached.exe -d stop		# 停止服务
```

​也可以在任务管理器中进行服务状态的切换

![[缓存/附件/image-20220226175441675.png]]

---

# **变更缓存为Memcached**

由于memcached未被springboot收录为缓存解决方案，因此使用memcached需要通过手工硬编码的方式来使用，于是前面的套路都不适用了，需要自己写了。

​memcached 目前提供有三种客户端技术，分别是 Memcached Client for Java、SpyMemcached 和 Xmemcached，其中性能指标各方面最好的客户端是 Xmemcached，本次整合就使用这个作为客户端实现技术了。下面开始使用 Xmemcached

**步骤 1**：导入xmemcached的坐标

```xml
<dependency>
    <groupId>com.googlecode.xmemcached</groupId>
    <artifactId>xmemcached</artifactId>
    <version>2.4.7</version>
</dependency>
```

**步骤 2**：配置 memcached，制作 memcached 的配置类

```java
@Configuration
public class XMemcachedConfig {
    @Bean
    public MemcachedClient getMemcachedClient() throws IOException {
        // 使用 XMemcachedClientBuilder 构建客户端
        MemcachedClientBuilder memcachedClientBuilder = new XMemcachedClientBuilder("localhost:11211");
        MemcachedClient memcachedClient = memcachedClientBuilder.build();
        return memcachedClient;
    }
}
```

> memcached 默认对外服务端口 11211。

**步骤 3**：使用 xmemcached 客户端操作缓存，注入 MemcachedClient 对象

```java
@Service
public class SMSCodeServiceImpl implements SMSCodeService {
    @Autowired
    private CodeUtils codeUtils;

    @Autowired
    private MemcachedClient memcachedClient;

    @Override
    public String sendCodeToSMS(String phone) {
        System.out.println("Sending code to phone: " + phone);
        String code = codeUtils.generateCode(phone);
        try {
            memcachedClient.set(phone, 60, code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }

    @Override
    public boolean checkCode(SMSCode smsCode) {
        String code = null;
        try {
            code = memcachedClient.get(smsCode.getPhone()).toString();
            System.out.println("Retrieved code from cache: " + code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return smsCode.getCode().equals(code);
    }
}
```

设置值到缓存中使用set操作，取值使用get操作，其实更符合我们开发者的习惯。

​上述代码中对于服务器的配置使用硬编码写死到了代码中，将此数据提取出来，做成独立的配置属性。

---

# **定义配置属性**

​上述代码中对于服务器的配置使用硬编码写死到了代码中，将此数据提取出来，做成独立的配置属性。

以下过程采用前期学习的属性配置方式进行，当前操作有助于理解原理篇中的很多知识。

定义配置类，加载必要的配置属性，读取配置文件中 memcached 节点信息

```java
@Component
@ConfigurationProperties(prefix = "memcached")
@Data
public class XMemcachedProperties {
  private String servers;
  private int poolSize;
  private long opTimeout;
}
```

定义memcached节点信息

```yaml
# XMemcached 配置  
memcached:  
  servers: localhost:11211  
  poolSize: 10  
  opTimeout: 3000
```

在 memcached 配置类中加载信息

```java
@Configuration
public class XMemcachedConfig {
    @Autowired
    private XMemcachedProperties props;

    @Bean
    public MemcachedClient getMemcachedClient() throws IOException {
        MemcachedClientBuilder memcachedClientBuilder = new XMemcachedClientBuilder(props.getServers());
        memcachedClientBuilder.setConnectionPoolSize(props.getPoolSize());
        memcachedClientBuilder.setOpTimeout(props.getOpTimeout());
        MemcachedClient memcachedClient = memcachedClientBuilder.build();
        return memcachedClient;
    }
}
```

> [!NOTE] **总结**
> 
> - memcached安装后需要启动对应服务才可以对外提供缓存功能，安装memcached服务需要基于windows系统管理员权限
> - 由于springboot没有提供对memcached的缓存整合方案，需要采用手工编码的形式创建xmemcached客户端操作缓存
> - 导入xmemcached坐标后，创建memcached配置类，注册MemcachedClient对应的bean，用于操作缓存
> - 初始化MemcachedClient对象所需要使用的属性可以通过自定义配置属性类的形式加载

> [!NOTE] **思考**
> 
> 到这里已经完成了三种缓存的整合，其中redis和mongodb需要安装独立的服务器，连接时需要输入对应的服务器地址，这种是远程缓存，Ehcache是一个典型的内存级缓存，因为它什么也不用安装，启动后导入jar包就有缓存功能了。这个时候就要问了，能不能这两种缓存一起用呢？咱们下节再说。

​		

