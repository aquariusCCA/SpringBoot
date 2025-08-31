---
up:
  - "[[SpringBoot 課程描述]]"
---
# 前置作業

```xml
<dependencyManagement>
	<dependencies>
		<dependency>
			<groupId>org.springframework.ai</groupId>
			<artifactId>spring-ai-bom</artifactId>
			<version>${spring-ai.version}</version>
			<type>pom</type>
			<scope>import</scope>
		</dependency>

		<dependency>
			<groupId>com.baomidou</groupId>
			<artifactId>mybatis-plus-bom</artifactId>
			<version>3.5.12</version> <!-- 或 3.5.9+ 的穩定版 -->
			<type>pom</type>
			<scope>import</scope>
		</dependency>
	</dependencies>
</dependencyManagement>

<dependencies>
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-test</artifactId>
		<scope>test</scope>
	</dependency>
	<dependency>
		<groupId>org.projectlombok</groupId>
		<artifactId>lombok</artifactId>
		<scope>annotationProcessor</scope>
	</dependency>
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-cache</artifactId>
	</dependency>
	<dependency>
		<groupId>com.baomidou</groupId>
		<artifactId>mybatis-plus-spring-boot3-starter</artifactId>
		<version>3.5.12</version>
	</dependency>
	<dependency>
		<groupId>com.mysql</groupId>
		<artifactId>mysql-connector-j</artifactId>
		<scope>runtime</scope>
	</dependency>
	<dependency>
		<groupId>com.baomidou</groupId>
		<artifactId>mybatis-plus-jsqlparser</artifactId>
	</dependency>
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-web</artifactId>
	</dependency>
</dependencies>
```

```yml  
spring:  
  datasource:  
    driver-class-name: com.mysql.cj.jdbc.Driver  
    url: jdbc:mysql://localhost:3306/springboot  
    username: root  
    password: Lins860210SStar  
  
mybatis-plus:  
  global-config:  
    db-config:  
      table-prefix: tbl_
```

```java
@Configuration
@MapperScan("com.test.mapper") 
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {

        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();

        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));

        return mybatisPlusInterceptor;
    }
}
```

```java
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

```java
public interface BookMapper extends BaseMapper<Book> {  
}
```

```java
public interface IBookService extends IService<Book> {  
    Book getById(Serializable id);  
}
```

```java
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements IBookService {
    @Autowired
    private BookMapper bookMapper;

    private HashMap<Serializable, Book> cache = new HashMap<>();

    @Override
    public IPage<Book> getPage(int currentPage, int pageSize) {
        return null;
    }
}
```

```java
@Data  
@NoArgsConstructor  
@AllArgsConstructor  
public class R {  
    private Boolean flag;  
    private Object data;  
    private String msg;  
  
    public R(Boolean flag, Object data) {  
        this.flag = flag;  
        this.data = data;  
    }  
  
    public R(Boolean flag) {  
        this.flag = flag;  
    }  
  
    public R(Boolean flag, String msg) {  
        this.flag = flag;  
        this.msg = msg;  
    }  
  
    public R(String msg) {  
        this(false, msg);  
    }  
}
```

---

# 用`java`的`HashMap`模拟一个缓存

```java
@RestController  
@RequestMapping("/books")  
public class BookController {  
    @Autowired  
    private IBookService bookService;  
  
    @GetMapping("{id}")  
    // 模拟缓存  
    public R getById(@PathVariable Integer id) {  
        return new R(true, bookService.getById(id));  
    }  
}
```

```java
@Service  
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements IBookService {  
    @Autowired  
    private BookMapper bookMapper;  
  
    private HashMap<Serializable, Book> cache = new HashMap<>();  
  
    @Override  
    public Book getById(Serializable id) {  
        // 如果当前缓存中没有本次要查询的数据，则进行查询，否则直接从缓存中获取数据返回  
        Book book = cache.get(id);  
        if (book == null) {  
            book = super.getById(id);  
            cache.put(id, book);  
        }  
        return book;  
    }  
}
```

---