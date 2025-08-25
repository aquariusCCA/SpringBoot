---
up:
  - "[[SpringBoot 課程描述]]"
  - "[[入门案例解析]]"
---
那根据上面的操作我们思考是否可以换个服务器呢？必须的嘛。根据SpringBoot的工作机制，用什么技术，加入什么依赖就行了。SpringBoot提供了3款内置的服务器：

- tomcat(默认)：apache出品，粉丝多，应用面广，负载了若干较重的组件

- jetty：更轻量级，负载性能远不及tomcat

- undertow：负载性能勉强跑赢tomcat

想用哪个，加个坐标就OK。前提是把tomcat排除掉，因为tomcat是默认加载的。

```XML
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        <exclusions>
            <exclusion>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-tomcat</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-jetty</artifactId>
    </dependency>
</dependencies>
```

​现在就已经成功替换了web服务器，核心思想就是用什么加入对应坐标就可以了。如果有starter，优先使用starter。

> [!NOTE] **总结**
> 
> 1. 内嵌Tomcat服务器是SpringBoot辅助功能之一
> 2. 内嵌Tomcat工作原理是将Tomcat服务器作为对象运行，并将该对象交给Spring容器管理
> 3. 变更内嵌服务器思想是去除现有服务器，添加全新的服务器

---

到这里第一章快速上手SpringBoot就结束了，这一章我们学习了两大块知识

1. 使用了4种方式制作了SpringBoot的入门程序，不管是哪一种，其实内部都是一模一样的

2. 学习了入门程序的工作流程，知道什么是parent，什么是starter，这两个东西是怎么配合工作的，以及我们的程序为什么启动起来是一个tomcat服务器等等

第一章到这里就结束了，再往下学习就要去基于会创建SpringBoot工程的基础上，研究SpringBoot工程的具体细节了。
