---
up:
  - "[[SpringBoot 課程描述]]"
---

> [!NOTE] **參考文章**
> 
> - [ObjectMapper使用详细介绍](https://www.cnblogs.com/javaxubo/p/16583980.html)
> - [ObjectMapper的readTree和treeToValue方法](https://blog.csdn.net/flagsjh/article/details/132876103)

# 简介

ObjectMapper 类是 Jackson 库的主要类。它提供一些功能将转换成 Java 对象匹配 JSON 结构，反之亦然。它使用JsonParser和JsonGenerator的实例实现JSON实际的读/写。它可以帮助我们快速的进行各个类型和Json类型的相互转换。可以使用ObjectMapper进行json和对象间的转换

Jackson ObjectMapper(com.fasterxml.jackson.databind.ObjectMapper)是使用Jackson解析JSON最简单的方法。  
Jackson ObjectMapper可以从字符串、流或文件解析JSON，并创建Java对象或对象图来表示已解析的JSON  
将JSON解析为Java对象也称为从JSON反序列化Java对象  
Jackson ObjectMapper也可以从Java对象创建JSON. 从Java对象生成JSON的过程也被称为序列化Java对象到JSON

---

# 在 Spring Boot 裡使用 ObjectMapper 

ObjectMapper 是由 Jackson library 所提供的一個功能，所以只要在 maven 中加入 `spring-boot-starter-web` 的 dependency 就可以了

```yml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

---

# ObjectMapper的常用配置

```java
@Configuration
public class JacksonConfig {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Bean
    Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            // 1) 你原本的功能開關
            // 美化輸出
            builder.featuresToEnable(SerializationFeature.INDENT_OUTPUT);

            //序列化的时候序列对象的那些属性
            //JsonInclude.Include.NON_DEFAULT 属性为默认值不序列化
            //JsonInclude.Include.ALWAYS      所有属性
            //JsonInclude.Include.NON_EMPTY   属性为 空（“”） 或者为 NULL 都不序列化
            //JsonInclude.Include.NON_NULL    属性为NULL 不序列化
            builder.serializationInclusion(JsonInclude.Include.ALWAYS);

            //反序列化忽略未知欄位
            //true - 遇到没有的属性就报错
            //false - 没有的属性不会管，不会报错
            builder.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            //如果是空对象的时候,不抛异常
            builder.featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

            // 忽略 transient 修饰的属性
            builder.featuresToEnable(MapperFeature.PROPAGATE_TRANSIENT_MARKER);

            //修改序列化后日期格式
            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            builder.simpleDateFormat(DATE_TIME_PATTERN);  // 作用於 java.util.Date / Calendar
            builder.timeZone(TimeZone.getTimeZone("Asia/Taipei"));

            // 3) Java 8+ 時間（LocalDateTime 等）採相同格式
            JavaTimeModule jsr310 = new JavaTimeModule();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
            jsr310.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dtf));
            jsr310.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dtf));
            builder.modules(jsr310);
        };
    }
}
```

---

# ObjectMapper的常用方法

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private Integer id;
    private String name;
    private String info;
}
```

### 字符串转实体类

```java
@Test  
public void stringToStudent() {  
    String jsonString = "{\"id\":1001,\"name\":\"Tom\",\"info\":\"一个小男孩\"}";  
  
    try {  
        Student student = mapper.readValue(jsonString, Student.class);  
        System.out.println("student = " + student);  
        //student = JacksonTest.Student(id=1001, name=Tom, info=一个小男孩)  
    } catch (Exception e) {  
        e.printStackTrace();  
    }  
}
```

### 实体类转字符串

```java
@Test  
public void studentToString() {  
    Student student = new Student();  
    student.setId(1002);  
    student.setName("李四");  
    student.setInfo("一个小女孩");  
    try {  
        String jsonString = mapper.writeValueAsString(student);  
        System.out.println("jsonString = " + jsonString);  
        //jsonString = {"id":1002,"name":"李四","info":"一个小女孩"}  
    } catch (Exception e) {  
        e.printStackTrace();  
    }  
}
```

### 字符串转 List<实体类>

```java
@Test
public void stringToStudentList() {
	String jsonString = "[{\"id\":1001,\"name\":\"Tom\",\"info\":\"一个小男孩\"},{\"id\":1002,\"name\":\"Jane\",\"info\":\"一个小女孩\"}]";
	try {
		Student[] stu = mapper.readValue(jsonString, Student[].class);
	   
		for (Student student : stu) {
			System.out.println("student = " + student);
			//student = JacksonTest.Student(id=1001, name=Tom, info=一个小男孩)
			//student = JacksonTest.Student(id=1002, name=Jane, info=一个小女孩)
		}

		String ss = mapper.writeValueAsString(stu);
		System.out.println("ss = " + ss);
		//ss = [{"id":1001,"name":"Tom","info":"一个小男孩"},{"id":1002,"name":"Jane","info":"一个小女孩"}]

	} catch (Exception e) {
		e.printStackTrace();
	}
}
```

### map 和 json 字符串之间转换

```java
@Test  
public void testMap(){  
    Map<String, Object> testMap = new HashMap<>();  
    testMap.put("name", "22");  
    testMap.put("age", 20);  
    testMap.put("date", new Date());  
    testMap.put("employee", new Employee("kevin", 27, new Date()));  
  
  
    try {  
        String jsonStr = mapper.writeValueAsString(testMap);  
        System.out.println(jsonStr);  
          
        Map<String, Object> testMapDes = mapper.readValue(jsonStr, Map.class);  
        System.out.println(testMapDes);  
    } catch (JsonProcessingException e) {  
        throw new RuntimeException(e);  
    }  
}
```

### 提取指定 Json 串

```java
@Test
public void testReadTree(){
	try {
		String json = "{\"name\":\"John\", \"age\":30}";
		JsonNode jsonNode = mapper.readTree(json);
		System.out.println(jsonNode.get("name").asText()); // 输出 John
		System.out.println(jsonNode.get("age").asInt()); // 输出 30
	} catch (JsonProcessingException e) {
		throw new RuntimeException(e);
	}
}
```

```java
@Test
public void testTreeToValue(){
	try {
		String json = "{\"name\":\"John\", \"age\":30}";
		JsonNode jsonNode = mapper.readTree(json);
		Person person = mapper.treeToValue(jsonNode, Person.class);
		System.out.println(person.getName()); // 输出 John
		System.out.println(person.getAge()); // 输出 30
	} catch (JsonProcessingException e) {
		throw new RuntimeException(e);
	}
}
```