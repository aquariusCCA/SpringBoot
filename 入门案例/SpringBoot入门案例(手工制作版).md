---
up:
  - "[[SpringBoot 課程描述]]"
---
# 操作步驟

不能上网，还想创建SpringBoot工程，能不能做呢？能做，但是你要先问问自己联网和不联网到底差别是什么？这个差别找到以后，你就发现，你把联网要干的事情都提前准备好，就无需联网了。

​联网做什么呢？首先SpringBoot工程也是基于Maven构建的，而Maven工程中如果加载一些工程需要使用又不存在的东西时，就要联网去下载。其实SpringBoot工程创建的时候就是要去下载一些必要的组件。如果把这些东西提前准备好呢？是的，就是这样。

​下面就手工创建一个SpringBoot工程，如果需要使用的东西提前保障在maven仓库中存在，整个过程就可以不依赖联网环境了。不过咱们已经用3种方式创建了SprongBoot工程了，所以下面也没什么东西需要下载了。

**步骤①**：创建工程时，选择创建普通Maven工程。

![[入门案例/附件/image-20211122165341684.png]]

**步骤②**：参照标准SpringBoot工程的pom文件，书写自己的pom文件即可。

```XML
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.5.4</version>
    </parent>

    <groupId>com.itheima</groupId>
    <artifactId>springboot_01_04_quickstart</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>
</project>
```

​用什么写什么，不用的都可以不写。当然，现在小伙伴们可能还不知道用什么和不用什么，最简单的就是复制粘贴了，随着后面的学习，你就知道哪些可以省略了。此处我删减了一些目前不是必须的东西，一样能用。核心的内容有两条，一个是继承了一个父工程，另外添加了一个依赖。

**步骤③**：之前运行SpringBoot工程需要一个类，这个缺不了，自己手写一个就行了，建议按照之前的目录结构来创建，先别玩花样，先学走后学跑。类名可以自定义，关联的名称同步修改即可。

```JAVA
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
```

> [!NOTE] <font color="#ff0000"><b>关注</b></font>:
> 
> 类上面的注解@SpringBootApplication千万别丢了，这个是核心，后面再介绍。

> [!NOTE] <font color="#ff0000"><b>关注</b></font>：
> 
> 类名可以自定义，只要保障下面代码中使用的类名和你自己定义的名称一样即可，也就是run方法中的那个class对应的名称。

**步骤④**：下面就可以自己创建一个Controller测试一下是否能用了，和之前没有差别的。

> 看到这里其实应该能够想明白了，通过向导或者网站创建的SpringBoot工程其实就是帮你写了一些代码，而现在是自己手写，写的内容都一样，仅此而已。

---

# <font color="#f0f"><b>温馨提示</b></font>

​如果你的计算机上从来没有创建成功过SpringBoot工程，自然也就没有下载过SpringBoot对应的坐标相关的资源，那用手写创建的方式在不联网的情况下肯定该是不能用的。所谓手写，其实就是自己写别人帮你生成的东西，但是引用的坐标对应的资源必须保障maven仓库里面有才行，如果没有，还是要去下载的。

---

# **总结**

1. 创建普通Maven工程
2. 继承spring-boot-starter-parent
3. 添加依赖spring-boot-starter-web
4. 制作引导类Application

​到这里已经学习了4种创建SpringBoot工程的方式，其实本质是一样的，都是根据SpringBoot工程的文件格式要求，通过不同时方式生成或者手写得到对应的文件，效果完全一样。