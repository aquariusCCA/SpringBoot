---
up:
  - "[[SpringBoot 課程描述]]"
---
> 上一节已经在测试用例中成功的模拟出了web环境，并成功的发送了web请求，本节就来解决发送请求后如何比对发送结果的问题。其实发完请求得到的信息只有一种，就是响应对象。至于响应对象中包含什么，就可以比对什么。常见的比对内容如下：

---

# 响应状态匹配

```JAVA
@Test
void testStatus(@Autowired MockMvc mvc) throws Exception {
  MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/books");
  ResultActions action = mvc.perform(builder);
  //设定预期值 与真实值进行比较，成功测试通过，失败测试失败
  //定义本次调用的预期值
  StatusResultMatchers status = MockMvcResultMatchers.status();
  //预计本次调用时成功的：状态200
  ResultMatcher ok = status.isOk();
  //添加预计值到本次调用过程中进行匹配
  action.andExpect(ok);
}
```

---

# 响应体匹配（非json数据格式）

```JAVA
@Test
void testBody(@Autowired MockMvc mvc) throws Exception {
  MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/books");
  ResultActions action = mvc.perform(builder);
  //设定预期值 与真实值进行比较，成功测试通过，失败测试失败
  //定义本次调用的预期值
  ContentResultMatchers content = MockMvcResultMatchers.content();
  ResultMatcher result = content.string("springboot2");
  //添加预计值到本次调用过程中进行匹配
  action.andExpect(result);
}
```

---

# 响应体匹配（json数据格式，开发中的主流使用方式）

```JAVA
@Test
void testJson(@Autowired MockMvc mvc) throws Exception {
  MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/books");
  ResultActions action = mvc.perform(builder);
  //设定预期值 与真实值进行比较，成功测试通过，失败测试失败
  //定义本次调用的预期值
  ContentResultMatchers content = MockMvcResultMatchers.content();
  ResultMatcher result = content.json("{\"id\":1,\"name\":\"springboot2\",\"type\":\"springboot\"}");
  //添加预计值到本次调用过程中进行匹配
  action.andExpect(result);
}
```

---

# 响应头信息匹配

```JAVA
@Test
void testContentType(@Autowired MockMvc mvc) throws Exception {
  MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/books");
  ResultActions action = mvc.perform(builder);
  //设定预期值 与真实值进行比较，成功测试通过，失败测试失败
  //定义本次调用的预期值
  HeaderResultMatchers header = MockMvcResultMatchers.header();
  ResultMatcher contentType = header.string("Content-Type", "application/json");
  //添加预计值到本次调用过程中进行匹配
  action.andExpect(contentType);
}
```

---
# **总结**

> web虚拟调用可以对本地虚拟请求的返回响应信息进行比对，分为响应头信息比对、响应体信息比对、响应状态信息比对

​基本上齐了，头信息，正文信息，状态信息都有了，就可以组合出一个完美的响应结果比对结果了。以下范例就是三种信息同时进行匹配校验，也是一个完整的信息匹配过程。

```JAVA
@Test
void testGetById(@Autowired MockMvc mvc) throws Exception {
    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/books");
    ResultActions action = mvc.perform(builder);

    StatusResultMatchers status = MockMvcResultMatchers.status();
    ResultMatcher ok = status.isOk();
    action.andExpect(ok);

    HeaderResultMatchers header = MockMvcResultMatchers.header();
    ResultMatcher contentType = header.string("Content-Type", "application/json");
    action.andExpect(contentType);

    ContentResultMatchers content = MockMvcResultMatchers.content();
    ResultMatcher result = content.json("{\"id\":1,\"name\":\"springboot\",\"type\":\"springboot\"}");
    action.andExpect(result);
}
```