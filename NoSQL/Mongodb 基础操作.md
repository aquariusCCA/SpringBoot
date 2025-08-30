---
up:
  - "[[SpringBoot 課程描述]]"
  - "[[NoSQL]]"
---
MongoDB虽然是一款数据库，但是它的操作并不是使用SQL语句进行的，因此操作方式各位小伙伴可能比较陌生，好在有一些类似于Navicat的数据库客户端软件，能够便捷的操作MongoDB，先安装一个客户端，再来操作MongoDB。

​同类型的软件较多，本次安装的软件时Robo3t，Robot3t是一款绿色软件，无需安装，解压缩即可。解压缩完毕后进入安装目录双击robot3t.exe即可使用。

![[NoSQL/附件/image-20220224114911573.png]]
​
打开软件首先要连接MongoDB服务器，选择【File】菜单，选择【Connect...】

![[NoSQL/附件/image-20220224115202422.png]]

​进入连接管理界面后，选择左上角的【Create】链接，创建新的连接设置

![[NoSQL/附件/image-20220224115254200.png]]

如果输入设置值即可连接（默认不修改即可连接本机27017端口）

![[NoSQL/附件/image-20220224115300266.png]]

​连接成功后在命令输入区域输入命令即可操作MongoDB。

---

​创建数据库：在左侧菜单中使用右键创建，输入数据库名称即可

​创建集合：在 Collections 上使用右键创建，输入集合名称即可，集合等同于数据库中的表的作用

​新增文档：（文档是一种类似json格式的数据，初学者可以先把数据理解为就是json数据）	

```shell
db.集合名称.insert/save/insertOne(文档)
```

​删除文档：

```shell
db.集合名称.remove(条件)
```

​修改文档：

```shell
db.集合名称.update(条件，{操作种类:{文档}})
```

​查询文档：

```shell
基础查询
查询全部：		   db.集合.find();
查第一条：		   db.集合.findOne()
查询指定数量文档：	db.集合.find().limit(10)					//查10条文档
跳过指定数量文档：	db.集合.find().skip(20)					//跳过20条文档
统计：			  	db.集合.count()
排序：				db.集合.sort({age:1})						//按age升序排序
投影：				db.集合名称.find(条件,{name:1,age:1})		 //仅保留name与age域

条件查询
基本格式：			db.集合.find({条件})
模糊查询：			db.集合.find({域名:/正则表达式/})		  //等同SQL中的like，比like强大，可以执行正则所有规则
条件比较运算：		   db.集合.find({域名:{$gt:值}})				//等同SQL中的数值比较操作，例如：name>18
包含查询：			db.集合.find({域名:{$in:[值1，值2]}})		//等同于SQL中的in
条件连接查询：		   db.集合.find({$and:[{条件1},{条件2}]})	   //等同于SQL中的and、or
```

範例：

```shell
// 查询所有
db.getCollection('book').find({})
// 可以简写为
db.book.find();
// 条件查询
db.book.find({type: "springboot"})

// 保存文档
db.book.save({"name": "springboot", type: "springboot"})

// 删除操作
db.book.remove({type: "springboot"});

// 修改操作
// 修改满足条件的第一条数据
db.book.update({name: "springboot"}, {$set:{name: "springboot2"}});
// 修改满足条件的所有数据
db.book.updateMany({name: "springboot"}, {$set:{name: "springboot2"}});
```

> 有关MongoDB的基础操作就普及到这里，需要全面掌握MongoDB技术，请参看相关教程学习。 