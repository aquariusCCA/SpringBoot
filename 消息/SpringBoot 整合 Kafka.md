---
up:
  - "[[SpringBoot 課程描述]]"
補充內容:
  - "[[MacOS 安裝 Kafka]]"
---
# 安装

​windows版安装包下载地址：[https://](https://kafka.apache.org/downloads)[kafka.apache.org/downloads](https://kafka.apache.org/downloads)

​下载完毕后得到 tgz 压缩文件，使用解压缩软件解压缩即可使用，解压后得到如下文件

![[消息/附件/image-20220228181442155.png]]\
​
建议使用 windows 版 2.8.1 版本。

---

# **启动服务器**

​kafka 服务器的功能相当于 RocketMQ 中的 broker，kafka 运行还需要一个类似于命名服务器的服务。在 kafka 安装目录中自带一个类似于命名服务器的工具，叫做 zookeeper，它的作用是注册中心，相关知识请到对应课程中学习。

```shell
# 启动zookeeper
zookeeper-server-start.bat ..\..\config\zookeeper.properties		

# 启动kafka
kafka-server-start.bat ..\..\config\server.properties				
```

​运行 bin 目录下的 windows 目录下的 zookeeper-server-start 命令即可启动注册中心，默认对外服务端口 2181。

​运行 bin 目录下的windows目录下的 kafka-server-start 命令即可启动 kafka 服务器，默认对外服务端口 9092。

---

# **创建主题**

​和之前操作其他 MQ 产品相似，kakfa也是基于主题操作，操作之前需要先初始化 topic。

```shell
# 创建topic
kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic itheima

# 查询topic
kafka-topics.bat --zookeeper 127.0.0.1:2181 --list					

# 删除topic
kafka-topics.bat --delete --zookeeper localhost:2181 --topic itheima
```

**测试服务器启动状态**

​Kafka 提供有一套测试服务器功能的测试程序，运行 bin 目录下的 windows 目录下的命令即可使用。

```shell
# 测试生产消息
kafka-console-producer.bat --broker-list localhost:9092 --topic itheima			

# 测试消息消费
kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic itheima --from-beginning	
```

---

# 整合

**步骤①**：导入 springboot 整合 Kafka 的 starter，此坐标由 springboot 维护版本

```xml
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```

**步骤②**：配置 Kafka 的服务器地址

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: order
```
 
> 设置默认的生产者消费者所属组 id。

**步骤③**：使用KafkaTemplate操作Kafka

```java
@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Override
    public void sendMessage(String id) {
        System.out.println("待发送短信的订单已纳入处理队列（kafka），id："+id);
        kafkaTemplate.send("itheima2022",id);
    }

    @Override
    public String doMessage() {
        // 不與監聽器競爭，直接返回或移除此方法
        return "N/A";
    }
}
```

> 使用 send 方法发送消息，需要传入 topic 名称。

**步骤④**：使用消息监听器在服务器启动后，监听指定位置，当消息出现后，立即消费消息

```JAVA
@Component
public class MessageListener {
    @KafkaListener(topics = "itheima2022")
    public void onMessage(ConsumerRecord<String,String> record){
        System.out.println("已完成短信发送业务(kafka)，id："+record.value());
    }
}
```

> 使用注解 @KafkaListener 定义当前方法监听 Kafka 中指定 topic 的消息，接收到的消息封装在对象 ConsumerRecord 中，获取数据从 ConsumerRecord 对象中获取即可。

> [!NOTE] **总结**
> 
> - springboot整合Kafka使用KafkaTemplate对象作为客户端操作消息队列
> - 操作Kafka需要配置Kafka服务器地址，默认端口9092
> - 企业开发时通常使用监听器来处理消息队列中的消息，设置监听器使用注解@KafkaListener。接收消息保存在形参ConsumerRecord对象中
