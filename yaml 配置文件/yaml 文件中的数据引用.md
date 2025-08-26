---
up:
  - "[[SpringBoot 課程描述]]"
  - "[[yaml 配置文件]]"
---
如果你在书写yaml数据时，经常出现如下现象，比如很多个文件都具有相同的目录前缀

```YAML
center:
	dataDir: /usr/local/fire/data
    tmpDir: /usr/local/fire/tmp
    logDir: /usr/local/fire/log
    msgDir: /usr/local/fire/msgDir
```

或者

```YAML
center:
	dataDir: D:/usr/local/fire/data
    tmpDir: D:/usr/local/fire/tmp
    logDir: D:/usr/local/fire/log
    msgDir: D:/usr/local/fire/msgDir
```

​这个时候你可以使用引用格式来定义数据，其实就是搞了个变量名，然后引用变量了，格式如下：

```YAML
baseDir: /usr/local/fire
center:
    dataDir: ${baseDir}/data
    tmpDir: ${baseDir}/tmp
    logDir: ${baseDir}/log
    msgDir: ${baseDir}/msgDir
```

​还有一个注意事项，在书写字符串时，如果需要使用转义字符，需要将数据字符串使用双引号包裹起来

```YAML
lesson: "Spring\tboot\nlesson"
```

> [!NOTE] **总结**
> 
> 1. 在配置文件中可以使用${属性名}
> 2. 如果属性中出现特殊字符，可以使用双引号包裹起来作为字符解析   

> 到这里有关yaml文件的基础使用就先告一段落，实用篇中再继续研究更深入的内容。

