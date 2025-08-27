---
up:
  - "[[SpringBoot 課程描述]]"
  - "[[多环境开发]]"
---
SpringBoot 最早期提供的配置文件格式是properties格式的，这种格式的多环境配置也了解一下吧。

**主配置文件**

```properties
spring.profiles.active=pro
```

**环境配置文件**

**application-pro.properties**

```properties
server.port=80
```

**application-dev.properties**

```properties
server.port=81
```

> 文件的命名规则为：`application-环境名.properties`。

> [!NOTE] **总结**
> 
> - properties文件多环境配置仅支持多文件格式

