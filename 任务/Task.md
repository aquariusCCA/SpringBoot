---
up:
  - "[[SpringBoot 課程描述]]"
  - "[[任务]]"
---
spring 根据定时任务的特征，将定时任务的开发简化到了极致。怎么说呢？要做定时任务总要告诉容器有这功能吧，然后定时执行什么任务直接告诉对应的bean什么时间执行就行了，就这么简单，一起来看怎么做

**步骤 1**：开启定时任务功能，在引导类上开启定时任务功能的开关，使用注解 @EnableScheduling

```java
@SpringBootApplication  
//开启定时任务功能  
@EnableScheduling  
public class TestSpringBootApplication {  
  
    public static void main(String[] args) {  
       SpringApplication.run(TestSpringBootApplication.class, args);  
    }  
}
```

**步骤 2**：定义Bean，在对应要定时执行的操作上方，使用注解@Scheduled定义执行的时间，执行时间的描述方式还是cron表达式

```java
@Component  
public class MyTask {  
    @Scheduled(cron = "0/3 * * * * ?")  
    public void print() {  
        System.out.println(Thread.currentThread().getName() + ": spring task run...");  
    }  
}
```

​完事，这就完成了定时任务的配置。总体感觉其实什么东西都没少，只不过没有将所有的信息都抽取成bean，而是直接使用注解绑定定时执行任务的事情而已。

**步驟 3: 直接启动`springboot`程序，任务便会自动执行**
### 如何想对定时任务进行相关配置，可以通过配置文件进行

```yaml
spring:
  task:
   	scheduling:
      pool:
       	size: 1							# 任务调度线程池大小 默认 1
      thread-name-prefix: ssm_      	# 调度线程名称前缀 默认 scheduling-      
        shutdown:
          await-termination: false		# 线程池关闭时等待所有任务完成
          await-termination-period: 10s	# 调度线程关闭前最大等待时间，确保最后一定关闭
```

> [!NOTE] **总结**
> 
> - spring task需要使用注解@EnableScheduling开启定时任务功能
> - 为定时执行的的任务设置执行周期，描述方式cron表达式