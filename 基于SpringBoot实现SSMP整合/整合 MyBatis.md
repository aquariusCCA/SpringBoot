---
up:
  - "[[SpringBoot 課程描述]]"
  - "[[基于SpringBoot实现SSMP整合]]"
---
整合完JUnit下面再来说一下整合 MyBatis，这个技术是大部分公司都要使用的技术，务必掌握。如果对 Spring 整合 MyBatis 不熟悉的小伙伴好好复习一下，下面列举出原始整合的全部内容，以配置类的形式为例进行

导入坐标，MyBatis 坐标不能少，Spring 整合 MyBatis 还有自己专用的坐标，此外 Spring 进行数据库操作的 jdbc 坐标是必须的，剩下还有 mysql 驱动坐标，本例中使用了 Druid 数据源，这个倒是可以不要

```XML
<dependencies>
  <dependency>
	  <groupId>com.alibaba</groupId>
	  <artifactId>druid</artifactId>
	  <version>1.1.16</version>
  </dependency>
  <dependency>
	  <groupId>org.mybatis</groupId>
	  <artifactId>mybatis</artifactId>
	  <version>3.5.6</version>
  </dependency>
  <dependency>
	  <groupId>mysql</groupId>
	  <artifactId>mysql-connector-java</artifactId>
	  <version>5.1.47</version>
  </dependency>
  <!--1.导入mybatis与spring整合的jar包-->
  <dependency>
	  <groupId>org.mybatis</groupId>
	  <artifactId>mybatis-spring</artifactId>
	  <version>1.3.0</version>
  </dependency>
  <!--导入spring操作数据库必选的包-->
  <dependency>
	  <groupId>org.springframework</groupId>
	  <artifactId>spring-jdbc</artifactId>
	  <version>5.2.10.RELEASE</version>
  </dependency>
</dependencies>
```

Spring核心配置

```JAVA
@Configuration
@ComponentScan("com.itheima")
@PropertySource("jdbc.properties")
public class SpringConfig {
}
```

MyBatis 要交给 Spring 接管的 bean

```JAVA
//定义mybatis专用的配置类
@Configuration
public class MyBatisConfig {
  //定义创建SqlSessionFactory对应的bean
  @Bean
  public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource){
	  //SqlSessionFactoryBean是由mybatis-spring包提供的，专用于整合用的对象
	  SqlSessionFactoryBean sfb = new SqlSessionFactoryBean();
	  //设置数据源替代原始配置中的environments的配置
	  sfb.setDataSource(dataSource);
	  //设置类型别名替代原始配置中的typeAliases的配置
	  sfb.setTypeAliasesPackage("com.itheima.domain");
	  return sfb;
  }
  //定义加载所有的映射配置
  @Bean
  public MapperScannerConfigurer mapperScannerConfigurer(){
	  MapperScannerConfigurer msc = new MapperScannerConfigurer();
	  msc.setBasePackage("com.itheima.dao");
	  return msc;
  }
}
```

数据源对应的bean，此处使用Druid数据源

```JAVA
@Configuration
public class JdbcConfig {
  @Value("${jdbc.driver}")
  private String driver;
  @Value("${jdbc.url}")
  private String url;
  @Value("${jdbc.username}")
  private String userName;
  @Value("${jdbc.password}")
  private String password;

  @Bean("dataSource")
  public DataSource dataSource(){
	  DruidDataSource ds = new DruidDataSource();
	  ds.setDriverClassName(driver);
	  ds.setUrl(url);
	  ds.setUsername(userName);
	  ds.setPassword(password);
	  return ds;
  }
}
```

数据库连接信息（properties格式）

```properties
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/spring_db?useSSL=false
jdbc.username=root
jdbc.password=root
```

上述格式基本上是最简格式了，要写的东西还真不少。下面看看SpringBoot整合MyBaits格式

**步骤①**：创建模块

![[基于SpringBoot实现SSMP整合/附件/image-20211129092156020.png]]

**步骤②**：勾选要使用的技术，MyBatis，由于要操作数据库，还要勾选对应数据库

![[基于SpringBoot实现SSMP整合/附件/image-20211129092210993.png]]

或者手工导入对应技术的starter，和对应数据库的坐标

```XML
<dependencies>
	<!-- MyBatis 與 Spring Boot 自動配置 -->
	<dependency>
		<groupId>org.mybatis.spring.boot</groupId>
		<artifactId>mybatis-spring-boot-starter</artifactId>
		<version>3.0.5</version>
	</dependency>

	<!--JDBC Driver-->
	<dependency>
		<groupId>com.mysql</groupId>
		<artifactId>mysql-connector-j</artifactId>
		<scope>runtime</scope>
	</dependency>
</dependencies>
```

**步骤③**：配置数据源相关信息，没有这个信息你连接哪个数据库都不知道

课程中使用到的数据库脚本

```sql
/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80023
 Source Host           : localhost:3306
 Source Schema         : springboot_db

 Target Server Type    : MySQL
 Target Server Version : 80023
 File Encoding         : 65001

 Date: 20/01/2022 11:50:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tbl_book
-- ----------------------------
DROP TABLE IF EXISTS `tbl_book`;
CREATE TABLE `tbl_book`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tbl_book
-- ----------------------------
INSERT INTO `tbl_book` VALUES (1, '三体', '科幻', '大刘的巅峰之作，将中国科幻推向世界舞台。总共分为三部曲：《地球往事》、《黑暗森林》、《死神永生》。');
INSERT INTO `tbl_book` VALUES (2, '格林童话', '童话', '睡前故事。');
INSERT INTO `tbl_book` VALUES (3, 'Spring 5设计模式', '计算机理论', '深入Spring源码剖析Spring源码中蕴含的10大设计模式');
INSERT INTO `tbl_book` VALUES (4, 'Spring MVC+ MyBatis开发从入门到项目实战', '计算机理论', '全方位解析面向Web应用的轻量级框架,带你成为Spring MVC开发高手');
INSERT INTO `tbl_book` VALUES (5, '轻量级Java Web企业应用实战', '计算机理论', '源码级剖析Spring框架,适合已掌握Java基础的读者');
INSERT INTO `tbl_book` VALUES (6, 'Java核心技术卷|基础知识(原书第11版)', '计算机理论', 'Core Java第11版，Jolt大奖获奖作品，针对Java SE9、10、 11全面更新');
INSERT INTO `tbl_book` VALUES (7, '深入理解Java虚拟机', '计算机理论', '5个维度全面剖析JVM,面试知识点全覆盖');
INSERT INTO `tbl_book` VALUES (8, 'Java编程思想(第4版)', '计算机理论', 'Java学习必读经典殿堂级著作!赢得了全球程序员的广泛赞誉');
INSERT INTO `tbl_book` VALUES (9, '零基础学Java (全彩版)', '计算机理论', '零基础自学编程的入门]图书，由浅入深，详解Java语言的编程思想和核心技术');
INSERT INTO `tbl_book` VALUES (10, '直播就该这么做:主播高效沟通实战指南', '市场营销', '李子柒、李佳琦、薇娅成长为网红的秘密都在书中');
INSERT INTO `tbl_book` VALUES (11, '直播销讲实战一本通', '市场营销', '和秋叶一起学系列网络营销书籍');
INSERT INTO `tbl_book` VALUES (12, '直播带货:淘宝、天猫直播从新手到高手', '市场营销', '一本教你如何玩转直播的书， 10堂课轻松实现带货月入3W+');
INSERT INTO `tbl_book` VALUES (13, 'Spring实战第5版', '计算机理论', 'Spring入门经典教程,深入理解Spring原理技术内幕');
INSERT INTO `tbl_book` VALUES (14, 'Spring 5核心原理与30个类手写实战', '计算机理论', '十年沉淀之作，写Spring精华思想');

SET FOREIGN_KEY_CHECKS = 1;
```

```yaml
#2.配置相关信息  
spring:  
  datasource:  
    driver-class-name: com.mysql.cj.jdbc.Driver  
    url: jdbc:mysql://localhost:3306/springboot  
    username: root  
    password: Lins860210SStar
```

​结束了，就这么多，没了。有人就很纳闷，这就结束了？对，这就结束了，SpringBoot把配置中所有可能出现的通用配置都简化了。下面写一个 MyBatis 程序运行需要的Dao（或者Mapper）就可以运行了

**实体类**

> 需要額外導入 lombok 依賴

```JAVA
@Data  
@NoArgsConstructor  
@AllArgsConstructor  
public class Book {  
    private Integer id;  
    private String type;  
    private String name;  
    private String description;  
}
```

**映射接口（Dao）**

```JAVA
@Mapper
public interface BookDao {
    @Select("select * from tbl_book where id = #{id}")
    public Book getById(Integer id);
}
```

**测试类**

```JAVA
@SpringBootTest
class Springboot05MybatisApplicationTests {
    @Autowired
    private BookDao bookDao;
    @Test
    void contextLoads() {
        System.out.println(bookDao.getById(1));
    }
}
```

​完美，开发从此变的就这么简单。再体会一下SpringBoot如何进行第三方技术整合的，是不是很优秀？具体内部的原理到原理篇再展开讲解

​<font color="#ff0000"><b>注意</b></font>：当前使用的SpringBoot版本是2.5.4，对应的坐标设置中Mysql驱动使用的是8x版本。使用SpringBoot2.4.3（不含）之前版本会出现一个小BUG，就是MySQL驱动升级到8以后要求强制配置时区，如果不设置会出问题。解决方案很简单，驱动url上面添加上对应设置就行了

```YAML
#2.配置相关信息
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/ssm_db?serverTimezone=UTC
    username: root
    password: root
```

这里设置的UTC是全球标准时间，你也可以理解为是英国时间，中国处在东八区，需要在这个基础上加上8小时，这样才能和中国地区的时间对应的，也可以修改配置为 Asia/Shanghai，同样可以解决这个问题。

```YAML
#2.配置相关信息
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/ssm_db?serverTimezone=Asia/Shanghai
    username: root
    password: root
```

​如果不想每次都设置这个东西，也可以去修改mysql中的配置文件mysql.ini，在mysqld项中添加default-time-zone=+8:00也可以解决这个问题。其实方式方法很多，这里就说这么多吧。

​此外在运行程序时还会给出一个提示，说数据库驱动过时的警告，根据提示修改配置即可，弃用**com.mysql.jdbc.Driver**，换用 <font color="#ff0000"><b>com.mysql.cj.jdbc.Driver</b></font>。前面的例子中已经更换了驱动了，在此说明一下。

```tex
Loading class `com.mysql.jdbc.Driver'. This is deprecated. The new driver class is `com.mysql.cj.jdbc.Driver'. The driver is automatically registered via the SPI and manual loading of the driver class is generally unnecessary.
```

> [!NOTE] **总结**
>
> 1. 整合操作需要勾选MyBatis技术，也就是导入MyBatis对应的starter
> 2. 数据库连接相关信息转换成配置   
> 3. 数据库SQL映射需要添加@Mapper被容器识别到
> 4. MySQL 8.X驱动强制要求设置时区
> 	- 修改url，添加serverTimezone设定
> 	- 修改MySQL数据库配置
> 5. 驱动类过时，提醒更换为com.mysql.cj.jdbc.Driver
