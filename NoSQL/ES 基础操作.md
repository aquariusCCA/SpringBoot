---
up:
  - "[[SpringBoot 課程描述]]"
  - "[[NoSQL]]"
---
ES中保存有我们要查询的数据，只不过格式和数据库存储数据格式不同而已。在ES中我们要先创建倒排索引，这个索引的功能又点类似于数据库的表，然后将数据添加到倒排索引中，添加的数据称为文档。所以要进行ES的操作要先创建索引，再添加文档，这样才能进行后续的查询操作。

​要操作ES可以通过Rest风格的请求来进行，也就是说发送一个请求就可以执行一个操作。比如新建索引，删除索引这些操作都可以使用发送请求的形式来进行。

---

# 创建索引

打开`postman`，发送一个`PUT`请求，新建一个`books`索引

books 是索引名称，下同

```shell
# PUT请求		
http://localhost:9200/books
```

请求参数

```json
{
    "mappings": {
        "properties": {
            "id": {
                "type": "keyword"
            },
            "name": {
                "type": "text",
                "analyzer": "ik_max_word",
                "copy_to": "all"
            },
            "type": {
                "type": "keyword"
            },
            "description": {
                "type": "text",
                "analyzer": "ik_max_word",
                "copy_to": "all"
            },
            "all": {
                "type": "text",
                "analyzer": "ik_max_word"
            }
        }
    }
}
```

发送请求后，看到如下信息即索引创建成功

```json
{
  "acknowledged": true,
  "shards_acknowledged": true,
  "index": "books"
}
```

重复创建已经存在的索引会出现错误信息，reason 属性中描述错误原因

```json
{
    "error": {
        "root_cause": [
            {
                "type": "resource_already_exists_exception",
                "reason": "index [books/HXWeMY63QSaHy_X2uYc6RQ] already exists",
                "index_uuid": "HXWeMY63QSaHy_X2uYc6RQ",
                "index": "books"
            }
        ],
        "type": "resource_already_exists_exception",
        # books索引已经存在
        "reason": "index [books/HXWeMY63QSaHy_X2uYc6RQ] already exists",
        "index_uuid": "HXWeMY63QSaHy_X2uYc6RQ",
        "index": "books"
    },
    "status": 400
}
```

---

# 查询索引

```shell
# GET请求		
http://localhost:9200/books
```

查询索引得到索引相关信息，如下

```json
{
  "book": {
	  "aliases": {},
	  "mappings": {},
	  "settings": {
		  "index": {
			  "routing": {
				  "allocation": {
					  "include": {
						  "_tier_preference": "data_content"
					  }
				  }
			  },
			  "number_of_shards": "1",
			  "provided_name": "books",
			  "creation_date": "1645768584849",
			  "number_of_replicas": "1",
			  "uuid": "VgC_XMVAQmedaiBNSgO2-w",
			  "version": {
				  "created": "7160299"
			  }
		  }
	  }
  }
}
```

如果查询了不存在的索引，会返回错误信息，例如查询名称为book的索引后信息如下

```json
{
  "error": {
	  "root_cause": [
		  {
			  "type": "index_not_found_exception",
			  "reason": "no such index [book]",
			  "resource.type": "index_or_alias",
			  "resource.id": "book",
			  "index_uuid": "_na_",
			  "index": "book"
		  }
	  ],
	  "type": "index_not_found_exception",
	  # 没有book索引
	  "reason": "no such index [book]",		
	  "resource.type": "index_or_alias",
	  "resource.id": "book",
	  "index_uuid": "_na_",
	  "index": "book"
  },
  "status": 404
}
  ```

---

# 删除索引

```shell
# DELETE请求	
http://localhost:9200/books
```

删除所有后，给出删除结果

```json
{
  "acknowledged": true
}
```

如果重复删除，会给出错误信息，同样在reason属性中描述具体的错误原因

```JSON
{
  "error": {
	  "root_cause": [
		  {
			  "type": "index_not_found_exception",
			  "reason": "no such index [books]",
			  "resource.type": "index_or_alias",
			  "resource.id": "book",
			  "index_uuid": "_na_",
			  "index": "book"
		  }
	  ],
	  "type": "index_not_found_exception",
	  # 没有books索引
	  "reason": "no such index [books]",		
	  "resource.type": "index_or_alias",
	  "resource.id": "book",
	  "index_uuid": "_na_",
	  "index": "book"
  },
  "status": 404
}
```

---

# 创建索引并指定分词器

前面创建的索引是未指定分词器的，可以在创建索引时添加请求参数，设置分词器。目前国内较为流行的分词器是IK分词器，使用前先在下对应的分词器，然后使用。

IK分词器下载地址：[https://github.com/medcl/elasticsearch-analysis-ik/releases](https://gitee.com/link?target=https%3A%2F%2Fgithub.com%2Fmedcl%2Felasticsearch-analysis-ik%2Freleases)

分词器下载后解压到ES安装目录的 plugins 目录中即可，安装分词器后需要重新启动ES服务器。

在`es`的`plugin`目录下新建一个ik文件夹（建文件夹是为了方便管理），然后把下载好的`ik`分词器压缩包中的内容解压到 ik 目录下

![[image-20220125165532370.png]]

关闭当前`es`启动的黑窗口，去`es`的bin目录下，双击`elasticsearch.bat`，重新启动`es`

```shell
# PUT请求		
http://localhost:9200/books
```

请求参数如下

```json
{
	#定义mappings属性，替换创建索引时对应的mappings属性
	"mappings":{
		#定义索引中包含的属性设置
		"properties":{
			#设置索引中包含id属性
			"id":{
				#当前属性可以被直接搜索
				"type":"keyword"
			},
			#设置索引中包含name属性
			"name":{
				#当前属性是文本信息，参与分词
				"type":"text",
				#使用IK分词器进行分词
				"analyzer":"ik_max_word",
				#分词结果拷贝到all属性中
				"copy_to":"all"
			},
			"type":{
				"type":"keyword"
			},
			"description":{
				"type":"text",
				"analyzer":"ik_max_word",
				"copy_to":"all"
			},
			#定义属性，用来描述多个字段的分词结果集合，当前属性可以参与查询
			"all":{
				"type":"text",
				"analyzer":"ik_max_word"
			}
		}
	}
}
```

目前我们已经有了索引了，但是索引中还没有数据，所以要先添加数据，ES中称数据为文档，下面进行文档操作。

---

# 添加文档

**有三种方式**

**使用系统生成id**

```shell
#POST请求	
http://localhost:9200/books/_doc
```

**使用指定id**

```shell
#POST请求	
http://localhost:9200/books/_create/1
```

**使用指定id，不存在创建，存在更新（版本递增）**

```shell
#POST请求	
http://localhost:9200/books/_doc/1	
```

**文档通过请求参数传递，数据格式json**

```shell
{
  "name":"springboot",
  "type":"springboot",
  "description":"springboot"
}  
```

---

# 查询文档

```json
GET请求	http://localhost:9200/books/_doc/1		 #查询单个文档 		
GET请求	http://localhost:9200/books/_search		 #查询全部文档
```

--- 

# 条件查询

```json
# GET请求	
# q=查询属性名:查询属性值
http://localhost:9200/books/_search?q=name:springboot	
```

---

# 删除文档

```json
DELETE请求	
http://localhost:9200/books/_doc/1
```

---

# 修改文档（全量更新）

```shell
# PUT请求
http://localhost:9200/books/_doc/1
```

文档通过请求参数传递，数据格式json

```json
{
  "name":"springboot",
  "type":"springboot",
  "description":"springboot"
}
```

---

# 修改文档（部分更新）

```shell
# POST请求	
http://localhost:9200/books/_update/1
```

文档通过请求参数传递，数据格式json

```json
{		
  #部分更新并不是对原始文档进行更新，而是对原始文档对象中的doc属性中的指定属性更新	
  "doc":{						
	  #仅更新提供的属性值，未提供的属性值不参与更新操作
	  "name":"springboot"		
  }
}
```

---