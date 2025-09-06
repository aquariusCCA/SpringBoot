---
up:
---
> RocketMQ 由阿里研发，后捐赠给 apache 基金会，目前是 apache 基金会顶级项目之一，也是目前市面上的 MQ 产品中较为流行的产品之一，它遵从 AMQP 协议。

---
# 安装

​windows 版安装包下载地址：[https://rocketmq.apache.org](https://rocketmq.apache.org/)[/](https://rocketmq.apache.org/)

下载完毕后得到 zip 压缩文件，解压缩即可使用，解压后得到如下文件

![[消息/附件/image-20220228174453471.png]]

​RocketMQ 安装后需要配置环境变量，具体如下：

- ROCKETMQ_HOME
- PATH
- NAMESRV_ADDR （建议）： 127.0.0.1:9876

​关于 NAMESRV_ADDR 对于初学者来说建议配置此项，也可以通过命令设置对应值，操作略显繁琐，建议配置。系统学习 RocketMQ 知识后即可灵活控制该项。

---

# **RocketMQ工作模式**

​在 RocketMQ 中，处理业务的服务器称为 broker，生产者与消费者不是直接与 broker 联系的，而是通过命名服务器进行通信。broker 启动后会通知命名服务器自己已经上线，这样命名服务器中就保存有所有的 broker 信息。当生产者与消费者需要连接 broker 时，通过命名服务器找到对应的处理业务的 broker，因此命名服务器在整套结构中起到一个信息中心的作用。并且 broker 启动前必须保障命名服务器先启动。

![[消息/附件/image-20220228175123790.png]]

### **启动服务器**

```shell
mqnamesrv		# 启动命名服务器
mqbroker -n localhost:9876 --enable-proxy		# 启动broker
```

​运行 bin 目录下的 mqnamesrv 命令即可启动命名服务器，默认对外服务端口 9876。

​运行 bin 目录下的 mqbroker 命令即可启动 broker 服务器，如果环境变量中没有设置NAMESRV_ADDR 则需要在运行 mqbroker 指令前通过 set 指令设置 NAMESRV_ADDR 的值，并且每次开启均需要设置此项。

### **测试服务器启动状态**

​RocketMQ 提供有一套测试服务器功能的测试程序，运行 bin 目录下的 tools 命令即可使用。

```shell
tools org.apache.rocketmq.example.quickstart.Producer		# 生产消息
tools org.apache.rocketmq.example.quickstart.Consumer		# 消费消息
```

---

# 整合（异步消息）

**步骤①**：导入springboot整合RocketMQ的starter，此坐标不由springboot维护版本

```xml
<dependency>
	<groupId>org.apache.rocketmq</groupId>
	<artifactId>rocketmq-spring-boot-starter</artifactId>
	<version>2.3.0</version>
</dependency>

<dependency>
	<groupId>org.apache.rocketmq</groupId>
	<artifactId>rocketmq-client</artifactId>
	<version>5.3.3</version>
</dependency>
```

**步骤②**：配置RocketMQ的服务器地址

```yaml
# RocketMQ nameserver 的地址，如果有多個用逗號隔開
rocketmq:
  name-server: 127.0.0.1:9876
# RocketMQ 生產者的配置
  producer:
    group: my-producer-group

# RocketMQ 消費者的配置
  consumer:
    group: my-consumer-group
server:
  port: 8083
```

> 设置默认的生产者消费者所属组 group。 

**步骤③**：使用 RocketMQTemplate 操作 RocketMQ

```java
@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public void sendMessage(String id) {
        System.out.println("待发送短信的订单已纳入处理队列（rocketmq），id："+id);

        rocketMQTemplate.asyncSend("order_id", id, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("異步訊息發送成功：" + sendResult);
            }

            @Override
            public void onException(Throwable e) {
                System.err.println("異步訊息發送失敗：" + e.getMessage());
            }
        });
    }

    @Override
    public String doMessage() {
        // 不與監聽器競爭，直接返回或移除此方法
        return "N/A";
    }
}
```

​使用 asyncSend 方法发送异步消息。

**步骤④**：使用消息监听器在服务器启动后，监听指定位置，当消息出现后，立即消费消息

```JAVA
@Component
@RocketMQMessageListener(topic = "test-topic", consumerGroup = "my-consumer-group")
public class MessageListener implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        System.out.println("接收到訊息：" + message);
    }
}
```

​RocketMQ 的监听器必须按照标准格式开发，实现 RocketMQListener 接口，泛型为消息类型。

​使用注解 @RocketMQMessageListener 定义当前类监听 RabbitMQ 中指定组、指定名称的消息队列。

> [!NOTE] **总结**
> 
> - springboot整合RocketMQ使用RocketMQTemplate对象作为客户端操作消息队列
> - 操作RocketMQ需要配置RocketMQ服务器地址，默认端口9876
> - 企业开发时通常使用监听器来处理消息队列中的消息，设置监听器使用注解@RocketMQMessageListener

