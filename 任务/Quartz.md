---
up:
  - "[[SpringBoot 課程描述]]"
  - "[[任务]]"
---
Quartz 技术是一个比较成熟的定时任务框架，怎么说呢？有点繁琐，用过的都知道，配置略微复杂。springboot对其进行整合后，简化了一系列的配置，将很多配置采用默认设置，这样开发阶段就简化了很多。再学习springboot整合Quartz前先普及几个Quartz的概念。

- 工作（Job）：用于定义具体执行的工作
- 工作明细（JobDetail）：用于描述定时工作相关的信息
- 触发器（Trigger）：描述了工作明细与调度器的对应关系
- 调度器（Scheduler）：用于描述触发工作的执行规则，通常使用cron表达式定义规则

​简单说就是你定时干什么事情，这就是工作，工作不可能就是一个简单的方法，还要设置一些明细信息。工作啥时候执行，设置一个调度器，可以简单理解成设置一个工作执行的时间。工作和调度都是独立定义的，它们两个怎么配合到一起呢？用触发器。完了，就这么多。下面开始springboot整合Quartz。

**步骤 1**：导入springboot整合Quartz的starter

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-quartz</artifactId>
</dependency>
```

**步骤 2**：定义任务Bean，按照Quartz的开发规范制作，继承QuartzJobBean

```java
public class MyQuartz extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        System.out.println("quartz task run...");
    }
}
```

**步骤 3**：创建Quartz配置类，定义工作明细（JobDetail）与触发器的（Trigger）bean

```java
@Configuration
public class QuartzConfig {
    @Bean
    public JobDetail printJobDetail(){
        //绑定具体的工作
        return JobBuilder.newJob(MyQuartz.class).storeDurably().build();
    }
    @Bean
    public Trigger printJobTrigger(){
        ScheduleBuilder schedBuilder = CronScheduleBuilder.cronSchedule("0/5 * * * * ?");
        //绑定对应的工作明细
        return TriggerBuilder.newTrigger().forJob(printJobDetail()).withSchedule(schedBuilder).build();
    }
}
```

​工作明细中要设置对应的具体工作，使用newJob()操作传入对应的工作任务类型即可。

​触发器需要绑定任务，使用forJob()操作传入绑定的工作明细对象。此处可以为工作明细设置名称然后使用名称绑定，也可以直接调用对应方法绑定。触发器中最核心的规则是执行时间，此处使用调度器定义执行时间，执行时间描述方式使用的是cron表达式。有关cron表达式的规则，各位小伙伴可以去参看相关课程学习，略微复杂，而且格式不能乱设置，不是写个格式就能用的，写不好就会出现冲突问题。

**步驟 4: 直接启动`springboot`程序，任务便会自动执行**

> [!NOTE] **总结**
> 
> - springboot整合Quartz就是将Quartz对应的核心对象交给spring容器管理，包含两个对象，JobDetail和Trigger对象
> - JobDetail对象描述的是工作的执行信息，需要绑定一个QuartzJobBean类型的对象
> - Trigger对象定义了一个触发器，需要为其指定绑定的JobDetail是哪个，同时要设置执行周期调度器

> [!NOTE] **思考**
> 
> - 上面的操作看上去不多，但是Quartz将其中的对象划分粒度过细，导致开发的时候有点繁琐，spring针对上述规则进行了简化，开发了自己的任务管理组件——Task，如何用呢？咱们下节再说。

​		