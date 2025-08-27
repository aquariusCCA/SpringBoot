---
up:
  - "[[SpringBoot 課程描述]]"
  - "[[基于SpringBoot实现SSMP整合]]"
---
SpringBoot技术的定位用于简化开发，再具体点是简化Spring程序的开发。所以在整合任意技术的时候，如果你想直观感触到简化的效果，你必须先知道使用非SpringBoot技术时对应的整合是如何做的，然后再看基于SpringBoot的整合是如何做的，才能比对出来简化在了哪里。

​我们先来看一下不使用SpringBoot技术时，Spring整合JUnit的制作方式

```JAVA
//加载spring整合junit专用的类运行器
@RunWith(SpringJUnit4ClassRunner.class)
//指定对应的配置信息
@ContextConfiguration(classes = SpringConfig.class)
public class AccountServiceTestCase {
    //注入你要测试的对象
    @Autowired
    private AccountService accountService;
    @Test
    public void testGetById(){
        //执行要测试的对象对应的方法
        System.out.println(accountService.findById(2));
    }
}

```

​其中核心代码是前两个注解，第一个注解@RunWith是设置Spring专用的测试类运行器，简单说就是Spring程序执行程序有自己的一套独立的运行程序的方式，不能使用JUnit提供的类运行方式了，必须指定一下，但是格式是固定的，琢磨一下，<font color="#ff0000"><b>每次都指定一样的东西，这个东西写起来没有技术含量啊</b></font>，第二个注解@ContextConfiguration是用来设置Spring核心配置文件或配置类的，简单说就是加载Spring的环境你要告诉Spring具体的环境配置是在哪里写的，虽然每次加载的文件都有可能不同，但是仔细想想，如果文件名是固定的，这个貌似也是一个固定格式。既然<font color="#ff0000"><b>有可能是固定格式，那就有可能每次都写一样的东西，也是一个没有技术含量的内容书写</b></font>

​SpringBoot就抓住上述两条没有技术含量的内容书写进行开发简化，能走默认值的走默认值，能不写的就不写，具体格式如下

```JAVA
@SpringBootTest
class Springboot04JunitApplicationTests {
    //注入你要测试的对象
    @Autowired
    private BookDao bookDao;
    @Test
    void contextLoads() {
        //执行要测试的对象对应的方法
        bookDao.save();
        System.out.println("two...");
    }
}
```

​看看这次简化成什么样了，一个注解就搞定了，而且还没有参数，再体会SpringBoot整合其他技术的优势在哪里，就两个字——<font color="#ff0000"><b>简化</b></font>。使用一个注解@SpringBootTest替换了前面两个注解。至于内部是怎么回事？和之前一样，只不过都走默认值。

​这个时候有人就问了，你加载的配置类或者配置文件是哪一个？就是我们前面启动程序使用的引导类。如果想手工指定引导类有两种方式，第一种方式使用属性的形式进行，在注解@SpringBootTest中添加classes属性指定配置类

```JAVA
@SpringBootTest(classes = Springboot04JunitApplication.class)
class Springboot04JunitApplicationTests {
    //注入你要测试的对象
    @Autowired
    private BookDao bookDao;
    @Test
    void contextLoads() {
        //执行要测试的对象对应的方法
        bookDao.save();
        System.out.println("two...");
    }
}
```

​第二种方式回归原始配置方式，仍然使用@ContextConfiguration注解进行，效果是一样的

```JAVA
@SpringBootTest
@ContextConfiguration(classes = Springboot04JunitApplication.class)
class Springboot04JunitApplicationTests {
    //注入你要测试的对象
    @Autowired
    private BookDao bookDao;
    @Test
    void contextLoads() {
        //执行要测试的对象对应的方法
        bookDao.save();
        System.out.println("two...");
    }
}
```

> [!NOTE] <font color="#f0f"><b>温馨提示</b></font>
> 
> 使用SpringBoot整合JUnit需要保障导入test对应的starter，由于初始化项目时此项是默认导入的，所以此处没有提及，其实和之前学习的内容一样，用什么技术导入对应的starter即可。

> [!NOTE] **总结**
> 
> 1. 导入测试对应的starter
> 2. 测试类使用@SpringBootTest修饰
> 3. 使用自动装配的形式添加要测试的对象
> 4. 测试类如果存在于引导类所在包或子包中无需指定引导类
> 5. 测试类如果不存在于引导类所在的包或子包中需要通过classes属性指定引导类
