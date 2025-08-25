---
up:
  - "[[SpringBoot 課程描述]]"
---
# 操作步驟

下面让我们开始做第一个SpringBoot程序吧，本课程基于 Idea2020.3 版本制作，使用的 Maven 版本为 3.6.1，JDK版本为 1.8。如果你的环境和上述环境不同，可能在操作界面和操作过程中略有不同，只要软件匹配兼容即可（说到这个 Idea 和 Maven，它们两个还真不是什么版本都能搭到一起的，说多了都是泪啊）。

​下面使用 SpringBoot 技术快速构建一个 SpringMVC 的程序，通过这个过程体会<font color="#ff0000"><b>简化</b></font>二字的含义。

**步骤①**：创建新模块，选择 Spring Initializr，并配置模块相关基础信息

![[入门案例/附件/image-20211116125259385.png]]

<font color="#ff0000"><b>特别关注</b></font>：第3步点击Next时，Idea需要联网状态才可以进入到后面那一页，如果不能正常联网，就无法正确到达右面那个设置页了，会一直<font color="#ff0000"><b>联网</b></font>转转转。

<font color="#ff0000"><b>特别关注</b></font>：第5步选择java版本和你计算机上安装的JDK版本匹配即可，但是最低要求为JDK8或以上版本，推荐使用8或11。

**步骤②**：选择当前模块需要使用的技术集

![[入门案例/附件/image-20211116125615728.png]]

按照要求，左侧选择web，然后在中间选择Spring Web即可，选完右侧就出现了新的内容项，这就表示勾选成功了。

​<font color="#ff0000"><b>关注</b></font>：此处选择的SpringBoot的版本使用默认的就可以了，需要说一点，SpringBoot的版本升级速度很快，可能昨天创建工程的时候默认版本是2.5.4，今天再创建工程默认版本就变成2.5.5了，差别不大，无需过于纠结，并且还可以到配置文件中修改对应的版本。

**步骤③**：开发控制器类

```JAVA
//Rest模式
@RestController
@RequestMapping("/books")
public class BookController {
    @GetMapping
    public String getById(){
        System.out.println("springboot is running...");
        return "springboot is running...";
    }
}
```

​入门案例制作的SpringMVC的控制器基于Rest风格开发，当然此处使用原始格式制作SpringMVC的程序也是没有问题的，上例中的@RestController与@GetMapping注解是基于Restful开发的典型注解。

<font color="#ff0000"><b>关注</b></font>：做到这里SpringBoot程序的最基础的开发已经做完了，现在就可以正常的运行Spring程序了。可能有些小伙伴会有疑惑，Tomcat服务器没有配置，Spring也没有配置，什么都没有配置这就能用吗？这就是SpringBoot技术的强大之处。关于内部工作流程后面再说，先专心学习开发过程。

**步骤④**：运行自动生成的Application类

![[入门案例/附件/image-20211116130152452.png]]


​使用带main方法的java程序的运行形式来运行程序，运行完毕后，控制台输出上述信息。

不难看出，运行的信息中包含了8080的端口，Tomcat这种熟悉的字样，难道这里启动了Tomcat服务器？是的，这里已经启动了。那服务器没有配置，哪里来的呢？后面再说。现在你就可以通过浏览器访问请求的路径，测试功能是否工作正常了。

```java
访问路径: http://localhost:8080/books
```

是不是感觉很神奇？当前效果其实依赖的底层逻辑还是很复杂的，但是从开发者角度来看，目前只有两个文件展现到了开发者面前。

---

# pom.xml

  这是 maven 的配置文件，描述了当前工程构建时相应的配置信息。

  ```XML
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	   xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <parent>
	  <groupId>org.springframework.boot</groupId>
	  <artifactId>spring-boot-starter-parent</artifactId>
	  <version>2.5.4</version>
  </parent>

  <groupId>com.itheima</groupId>
  <artifactId>springboot_01_01_quickstart</artifactId>
  <version>0.0.1-SNAPSHOT</version>

  <dependencies>
	  <dependency>
		  <groupId>org.springframework.boot</groupId>
		  <artifactId>spring-boot-starter-web</artifactId>
	  </dependency>

	  <dependency>
		  <groupId>org.springframework.boot</groupId>
		  <artifactId>spring-boot-starter-test</artifactId>
		  <scope>test</scope>
	  </dependency>
  </dependencies>
</project>
  ```

  配置中有两个信息需要关注，一个是 parent，也就是当前工程继承了另外一个工程，干什么用的后面再说，还有依赖坐标，干什么用的后面再说。

---

# Application类

  ```JAVA
@SpringBootApplication
public class Application {
  public static void main(String[] args) {
	  SpringApplication.run(Application.class, args);
  }
}
  ```

这个类功能很简单，就一句代码，前面运行程序就是运行的这个类。

到这里我们可以大胆推测一下，如果上面这两个文件没有的话，SpringBoot 肯定没法玩，看来核心就是这两个文件了。由于是制作第一个 SpringBoot程序，先不要关注这两个文件的功能，后面详细讲解内部工作流程。

​通过上面的制作，我们不难发现，SpringBoot程序简直太好写了，几乎什么都没写，功能就有了，这也是SpringBoot技术为什么现在这么火的原因，和Spring程序相比，SpringBoot程序在开发的过程中各个层面均具有优势。

| **类配置文件**         | **Spring**   | **SpringBoot** |
| ---------------------- | ------------ | -------------- |
| pom文件中的坐标        | **手工添加** | **勾选添加**   |
| web3.0配置类           | **手工制作** | **无**         |
| Spring/SpringMVC配置类 | **手工制作** | **无**         |
| 控制器                 | **手工制作** | **手工制作**   |

一句话总结一下就是<font color="#ff0000"><b>能少写就少写</b></font>，<font color="#ff0000"><b>能不写就不写</b></font>，这就是SpringBoot技术给我们带来的好处，行了，现在你就可以动手做一做SpringBoot程序了，看看效果如何，是否真的帮助你简化开发了。

---

# **总结**

1. 开发SpringBoot程序在Idea工具中基于联网的前提下可以根据向导快速制作
2. SpringBoot程序需要依赖JDK，版本要求最低为JDK8
3. SpringBoot程序中需要使用某种功能时可以通过勾选的形式选择技术，也可以手工添加对应的要使用的技术（后期讲解）
4. 运行SpringBoot程序通过运行Application程序进行

---

# **思考**

​前面制作的时候说过，这个过程必须联网才可以进行，但是有些时候你会遇到一些莫名其妙的问题，比如基于Idea开发时，你会发现你配置了一些坐标，然后Maven下载对应东西的时候死慢死慢的，甚至还会失败。其实这种现象和Idea这款IDE工具有关，万一Idea不能正常访问网络的话，我们是不是就无法制作SpringBoot程序了呢？咱们下一节再说。

---