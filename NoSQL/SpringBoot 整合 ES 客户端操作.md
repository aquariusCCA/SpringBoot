---
up:
  - "[[SpringBoot 課程描述]]"
  - "[[NoSQL]]"
---
# 整合 ES 客户端

使用springboot整合ES该如何进行呢？老规矩，导入坐标，做配置，使用API接口操作。

整合Redis如此，整合MongoDB如此，整合ES依然如此。太没有新意了，其实不是没有新意，这就是springboot的强大之处，所有东西都做成相同规则，对开发者来说非常友好。

​下面就开始springboot整合ES，操作步骤如下：

**步骤 1**：导入springboot整合ES的starter坐标

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
</dependency>
```

**步骤 2**：进行基础配置

```yaml
spring:
  elasticsearch:
    uris: http://localhost:9200
    username: elastic
    password: dn2FccTLlONId71*p8B
```

​配置ES服务器地址，端口9200

**步骤 3**：使用springboot整合ES的专用客户端接口 ElasticsearchClient 来进行操作

```java
@SpringBootTest  
public class ElasticSearchTest {  
	@Autowired  
	private ElasticsearchClient es;
}
```

---

# 创建索引

```java
@Test
void createBooksIndex() throws Exception {
	boolean exists = es.indices().exists(e -> e.index("books")).value();
	if (exists) {
		es.indices().delete(d -> d.index("books"));
	}

	CreateIndexResponse resp = es.indices().create(c -> c
			.index("books")
			.mappings(m -> m
					.properties("id", p -> p.keyword(k -> k))
					.properties("name", p -> p.text(t -> t
							.analyzer("ik_max_word")
							.copyTo(List.of("all"))))   // copy_to
					.properties("type", p -> p.keyword(k -> k))
					.properties("description", p -> p.text(t -> t
							.analyzer("ik_max_word")
							.copyTo(List.of("all"))))
					.properties("all", p -> p.text(t -> t.analyzer("ik_max_word")))
			)
	);

	System.out.println(resp);
}
```

---

# **创建索引（IK分词器）**：

```java
@Test
void createBooksIndex() throws Exception {
	if (es.indices().exists(e -> e.index("books")).value()) {
		es.indices().delete(d -> d.index("books"));
	}

	CreateIndexResponse resp = es.indices().create(c -> c
			.index("books")
			// 可選：分片/副本
			// .settings(s -> s.numberOfShards("1").numberOfReplicas("0"))
			.mappings(m -> m
					.properties("id", p -> p.keyword(k -> k))
					.properties("name", p -> p.text(t -> t
							.analyzer("ik_max_word")
							.copyTo(List.of("all"))))
					.properties("type", p -> p.keyword(k -> k))
					.properties("description", p -> p.text(t -> t
							.analyzer("ik_max_word")
							.copyTo(List.of("all"))))
					.properties("all", p -> p.text(t -> t.analyzer("ik_max_word")))
			)
	);

	System.out.println(resp);
}
```

---

# **添加文档**：

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDoc {
    private String id;         // 注意：這是來源文件的字段，不等於 ES 的 _id
    private String name;
    private String type;
    private String description;
}
```

```java
@Test
void createDocument() throws Exception {
	String esId = "book_0001";  // ES 的 _id
	BookDoc doc = new BookDoc("book_0001", "springboot", "springboot", "springboot");

	IndexResponse resp = es.index(i -> i
			.index("books")
			.id(esId)               // 指定 _id
			.opType(OpType.Create)  // 僅允許新增；若 _id 已存在會拋 409
			.document(doc)
	);

	System.out.println("Index Response: " + resp);
}
```


---

# **按id查询文档**：

```java
@Test
void getById() throws Exception {
	String esId = "book_0001"; // 這是 ES 的 _id，不是來源字段 id

	GetResponse<BookDoc> resp = es.get(g -> g
					.index("books")
					.id(esId),
			BookDoc.class);

	if (!resp.found()) {
		System.out.println("Not found: _id=" + esId);
		return;
	}

	BookDoc doc = resp.source();
	System.out.println("Found: _id=" + esId + ", name=" + doc.getName() + ", type=" + doc.getType());
}
```

---

# **按条件查询文档**：

```java
@Test
void searchByKeywordAndType() throws Exception {
	String keyword = "springboot"; // 中文可直接放，例如 "分布式 事務"
	String type    = "springboot"; // mapping 裡的 type 是 keyword，適合做 term 過濾

	SearchResponse<BookDoc> resp = es.search(s -> s
					.index("books")
					// 查詢條件：must 使用 analyzed 欄位，filter 用 keyword 精確比對
					.query(q -> q.bool(b -> b
							.must(m -> m.match(mm -> mm.field("all").query(keyword)))
							.filter(f -> f.term(t -> t.field("type").value(type)))
					))
					// 分頁：第 1 頁（from=0），每頁 10 筆
					.from(0)
					.size(10)
					// 排序：先依 _score（預設即如此；此處顯式示範）
					.sort(so -> so.field(f -> f.field("_score").order(SortOrder.Desc)))
					// 為了得到精確的 total（非僅估算）
					.trackTotalHits(t -> t.enabled(true)),
			BookDoc.class);

	// 輸出結果
	long total = resp.hits().total() != null ? resp.hits().total().value() : 0L;
	System.out.println("Total hits = " + total);

	for (Hit<BookDoc> hit : resp.hits().hits()) {
		BookDoc d = hit.source();
		System.out.println(String.format("_id=%s, score=%.4f, name=%s, type=%s",
				hit.id(), hit.score() == null ? 0.0 : hit.score(), d.getName(), d.getType()));
	}
}
```

---


