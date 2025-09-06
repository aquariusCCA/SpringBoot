---
up:
  - "[[SpringBoot 課程描述]]"
補充內容:
  - "[[MacOS 安裝 RabbitMQ]]"
---
> RabbitMQ 是 MQ 产品中的目前较为流行的产品之一，它遵从 AMQP 协议。RabbitMQ 的底层实现语言使用的是 Erlang，所以安装 RabbitMQ 需要先安装 Erlang。

---
# **Erlang 安装**

​windows版安装包下载地址：[https](https://www.erlang.org/downloads)[://www.erlang.org/downloads](https://www.erlang.org/downloads)

​下载完毕后得到exe安装文件，一键傻瓜式安装，安装完毕需要重启，需要重启，需要重启。

​安装的过程中可能会出现依赖Windows组件的提示，根据提示下载安装即可，都是自动执行的，如下：

![[消息/附件/image-20220228164851551.png]]

Erlang 安装后需要配置环境变量，否则 RabbitMQ将无法找到安装的Erlang。需要配置项如下，作用等同JDK配置环境变量的作用。

- ERLANG_HOME
- PATH

---

# 安装

​windows版安装包下载地址：[https://](https://rabbitmq.com/install-windows.html)[rabbitmq.com/install-windows.html](https://rabbitmq.com/install-windows.html)

​下载完毕后得到exe安装文件，一键傻瓜式安装，安装完毕后会得到如下文件

![[消息/附件/image-20220228165151524.png]]

### **启动服务器**

```shell
rabbitmq-service.bat start		# 启动服务
rabbitmq-service.bat stop		# 停止服务
rabbitmqctl status				# 查看服务状态
```

​运行 sbin 目录下的 `rabbitmq-service.bat` 命令即可
start 参数表示启动，stop参数表示退出，默认对外服务端口5672。

> **注意：** 启动rabbitmq的过程实际上是开启rabbitmq对应的系统服务，需要管理员权限方可执行。

>**说明：** 有没有感觉5672的服务端口很熟悉？activemq与rabbitmq有一个端口冲突问题，学习阶段无论操作哪一个？请确保另一个处于关闭状态。

>**说明：** 不喜欢命令行的小伙伴可以使用任务管理器中的服务页，找到RabbitMQ服务，使用鼠标右键菜单控制服务的启停。

![[消息/附件/image-20220228170147193.png]]

### **访问web管理服务**

​RabbitMQ 也提供有 web 控制台服务，但是此功能是一个插件，需要先启用才可以使用。

```shell
rabbitmq-plugins.bat list							# 查看当前所有插件的运行状态
rabbitmq-plugins.bat enable rabbitmq_management		# 启动rabbitmq_management插件
```

​启动插件后可以在插件运行状态中查看是否运行，运行后通过浏览器即可打开服务后台管理界面

```shell
http://localhost:15672
```

​web管理服务默认端口15672，访问后可以打开RabbitMQ的管理界面，如下：

![[消息/附件/image-20220228170504793.png]]

​首先输入访问用户名和密码，初始化用户名和密码相同，均为：guest，成功登录后进入管理后台界面，如下：

![[消息/附件/image-20220228170535261.png]]

---

# 整合(direct模型) 

​RabbitMQ满足AMQP协议，因此不同的消息模型对应的制作不同，先使用最简单的 direct 模型开发。

**步骤①**：导入springboot整合amqp的starter，amqp协议默认实现为rabbitmq方案

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

**步骤②**：配置RabbitMQ的服务器地址

```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
```

**步骤③**：初始化直连模式系统设置

​由于RabbitMQ不同模型要使用不同的交换机，因此需要先初始化RabbitMQ相关的对象，例如队列，交换机等

```java
@Configuration
@EnableRabbit // 加 `@EnableRabbit` 以啟用 `@RabbitListener`（不同 Spring Boot 版本行為略有差異，加上最保險）
public class RabbitConfigDirect {
    public static final String DIRECT_EXCHANGE = "directExchange";
    public static final String Q_DIRECT = "direct_queue";
    public static final String RK_DIRECT = "direct";

    @Bean
    public Queue directQueue() {
        // durable:是否持久化，默认false
        // exclusive:是否当前连接专用，默认False，连接关闭后队列即被删除
        // autoDelete:是否自动删除，当生产者或消费者不再使用此队列，自动删除
        // 一个参数的构造方法内部默认调用了4个参数的构造方法，后三个参数依次为true,false,false
        return new Queue(Q_DIRECT);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE);
    }

    @Bean
    public Binding bindingDirect() {
        return BindingBuilder.bind(directQueue()).to(directExchange()).with(RK_DIRECT);
    }
}
```

​队列Queue与直连交换机DirectExchange创建后，还需要绑定他们之间的关系Binding，这样就可以通过交换机操作对应队列。

**步骤④**：使用AmqpTemplate操作RabbitMQ

```java
@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public void sendMessage(String orderId) {
        System.out.println("待发送短信的订单已纳入处理队列，id：" + orderId);
        // 指定RabbitDirectConfig中的内容
        amqpTemplate.convertAndSend("directExchange", "direct", orderId);
    }

    @Override
    public String doMessage() {
        // 不與監聽器競爭，直接返回或移除此方法
        return "N/A";
    }
}
```

​amqp 协议中的操作 API 接口名称看上去和jms规范的操作API接口很相似，但是传递参数差异很大。

**步骤⑤**：使用消息监听器在服务器启动后，监听指定位置，当消息出现后，立即消费消息

```JAVA
@Component
public class MessageListener {
    @RabbitListener(queues = "direct_queue")
    public void receive(String id){
        System.out.println("已完成短信发送业务(rabbitmq direct)，id："+id);
    }
}
```

​使用注解 `@RabbitListener` 定义当前方法监听 RabbitMQ 中指定名称的消息队列。


---

# 整合(topic模型)

**步骤①**：导入springboot整合amqp的starter，amqp协议默认实现为rabbitmq方案

同上

**步骤②**：配置RabbitMQ的服务器地址

同上

**步骤③**：初始化主题模式系统设置

```JAVA
@Configuration
@EnableRabbit // 加 `@EnableRabbit` 以啟用 `@RabbitListener`（不同 Spring Boot 版本行為略有差異，加上最保險）
public class RabbitConfigTopic {

    public static final String TOPIC_EXCHANGE = "topicExchange";
    public static final String Q_TOPIC_1 = "topic_queue1";
    public static final String Q_TOPIC_2 = "topic_queue2";

    @Bean
    public Queue topicQueue() {
        // durable=true, exclusive=false, autoDelete=false (Spring 預設)
        return new Queue(Q_TOPIC_1);
    }

    @Bean
    public Queue topicQueue2() {
        return new Queue(Q_TOPIC_2);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    public Binding bindingTopic() {
        // 匹配如：topic.orders.id、topic.user.id
        return BindingBuilder.bind(topicQueue())
                .to(topicExchange())
                .with("topic.*.id");
    }

    @Bean
    public Binding bindingTopic2() {
        // 匹配如：topic.orders.id、topic.orders.created
        return BindingBuilder.bind(topicQueue2())
                .to(topicExchange())
                .with("topic.orders.*");
    }
}
```

​主题模式支持routingKey匹配模式，`*` 表示匹配一个单词，`#` 表示匹配任意内容，这样就可以通过主题交换机将消息分发到不同的队列中，详细内容请参看 RabbitMQ 系列课程。	

| **匹配键**        | **topic.\*.\*** | **topic.#** |
| ----------------- | --------------- | ----------- |
| topic.order.id    | true            | true        |
| order.topic.id    | false           | false       |
| topic.sm.order.id | false           | true        |
| topic.sm.id       | false           | true        |
| topic.id.order    | true            | true        |
| topic.id          | false           | true        |
| topic.order       | false           | true        |

**步骤④**：使用 AmqpTemplate 操作 RabbitMQ

```java
@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public void sendMessage(String orderId) {
        System.out.println("待发送短信的订单已纳入处理队列，id：" + orderId);
        // 兩個佇列都會收到（同時命中 topic.*.id 與 topic.orders.*）
        amqpTemplate.convertAndSend(
                RabbitConfigTopic.TOPIC_EXCHANGE, // ← 改成 topicExchange
                "topic.orders.id",                 // ← 確保命中你的綁定樣式
                orderId
        );
    }

    @Override
    public String doMessage() {
        // 不與監聽器競爭，直接返回或移除此方法
        return "N/A";
    }
}
```

​发送消息后，根据当前提供的 routingKey 与绑定交换机时设定的routingKey进行匹配，规则匹配成功消息才会进入到对应的队列中。

**步骤⑤**：使用消息监听器在服务器启动后，监听指定队列

```JAVA
@Component
public class MessageListener {
    @RabbitListener(queues = RabbitConfigTopic.Q_TOPIC_1)
    public void receive(String id){
        System.out.println("已完成短信发送业务(topic_queue1)，id："+id);
    }
}
```

```java
@Component
public class MessageListener2 {
    @RabbitListener(queues = RabbitConfigTopic.Q_TOPIC_2)
    public void receive(String id){
        System.out.println("已完成短信发送业务(topic_queue2)，id："+id);
    }
}
```

​使用注解 @RabbitListener 定义当前方法监听 RabbitMQ 中指定名称的消息队列。

> [!NOTE] **总结**
> 
> - springboot整合RabbitMQ提供了AmqpTemplate对象作为客户端操作消息队列
> - 操作ActiveMQ需要配置ActiveMQ服务器地址，默认端口5672
> - 企业开发时通常使用监听器来处理消息队列中的消息，设置监听器使用注解@RabbitListener
> - RabbitMQ有5种消息模型，使用的队列相同，但是交换机不同。交换机不同，对应的消息进入的策略也不同