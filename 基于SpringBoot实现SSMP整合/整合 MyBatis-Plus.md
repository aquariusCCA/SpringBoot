---
up:
  - "[[SpringBoot 課程描述]]"
  - "[[基于SpringBoot实现SSMP整合]]"
---
做完了两种技术的整合了，各位小伙伴要学会总结，我们做这个整合究竟哪些是核心？总结下来就两句话

- 导入对应技术的starter坐标

- 根据对应技术的要求做配置

虽然看起来有点虚，但是确实是这个理儿，下面趁热打铁，再换一个技术，看看是不是上面这两步。

​接下来在MyBatis的基础上再升级一下，整合MyBaitsPlus（简称MP），国人开发的技术，符合中国人开发习惯，谁用谁知道。来吧，一起做整合

**步骤①**：导入对应的starter

```XML
 <!-- MyBatis Plus -->
<dependency>
	<groupId>com.baomidou</groupId>
	<artifactId>mybatis-plus-spring-boot3-starter</artifactId>
	<version>3.5.12</version>
</dependency>

<!--JDBC Driver-->
<dependency>
	<groupId>com.mysql</groupId>
	<artifactId>mysql-connector-j</artifactId>
	<scope>runtime</scope>
</dependency>
```

​关于这个坐标，此处要说明一点，之前我们看的 starter 都是 spring-boot-starter-？？？，也就是说都是下面的格式

```shell
Spring-boot-start-***
```

​而 MyBatis 与 MyBatisPlus 这两个坐标的名字书写比较特殊，是第三方技术名称在前，boot 和starter在后。此处简单提一下命名规范，后期原理篇会再详细讲解

| starter所属 | 命名规则                                 | 示例                                                        |
| --------- | ------------------------------------ | --------------------------------------------------------- |
| 官方提供      | spring-boot-starter-技术名称             | spring-boot-starter-web <br/>spring-boot-starter-test     |
| 第三方提供     | 第三方技术名称-spring-boot-starter          | mybatis-spring-boot-starter<br/>druid-spring-boot-starter |
| 第三方提供     | 第三方技术名称-boot-starter（第三方技术名称过长，简化命名） | mybatis-plus-boot-starter                                 |

> [!NOTE] <font color="#f0f"><b>温馨提示</b></font>
> 
> 有些小伙伴在创建项目时想通过勾选的形式找到这个名字，别翻了，没有。截止目前，SpringBoot官网还未收录此坐标，而我们Idea创建模块时读取的是SpringBoot官网的Spring Initializr，所以也没有。如果换用阿里云的url创建项目可以找到对应的坐标。

**步骤②**：配置数据源相关信息

```yaml
#2.配置相关信息
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/springboot
    username: root
    password: Lins860210SStar
```

​没了，就这么多，剩下的就是写MyBaitsPlus的程序了

**映射接口**

```JAVA
public interface BookMapper extends BaseMapper<Book> {  
}
```


核心在于 Mapper 接口继承了一个BaseMapper的接口，这个接口中帮助开发者预定了若干个常用的API接口，简化了通用API接口的开发工作。

![[基于SpringBoot实现SSMP整合/附件/image-20211129100313919.png]]

**配置 MapperScan 注解**

```java
@SpringBootApplication
@MapperScan("com.test.mapper")
public class TestSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestSpringBootApplication.class, args);
    }
}
```


​下面就可以写一个测试类进行测试了

```java
@SpringBootTest
public class MapperTest {
    @Autowired
    private BookMapper bookMapper;

    @Test
    public void testSelectById() {
        System.out.println(bookMapper.selectById(1));
    }
}
```

> [!NOTE] <font color="#f0f"><b>温馨提示</b></font>
> 
> 目前数据库的表名定义规则是tbl_模块名称，为了能和实体类相对应，需要做一个配置，相关知识各位小伙伴可以到MyBatisPlus课程中去学习，此处仅给出解决方案。配置application.yml文件，添加如下配置即可，设置所有表名的通用前缀名

```yaml
mybatis-plus:
  global-config:
    db-config:
      table-prefix: tbl_		#设置所有表的通用前缀名称为tbl_
```

> [!NOTE] **总结**
> 
> 1. 手工添加MyBatis-Plus对应的starter
> 2. 数据层接口使用BaseMapper简化开发
> 3. 需要使用的第三方技术无法通过勾选确定时，需要手工添加坐标
 