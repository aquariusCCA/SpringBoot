---
up:
  - "[[SpringBoot 課程描述]]"
---
**SSMP** 通常指的是 **Spring（多半以 Spring Boot 為基座）+ Spring MVC + MyBatis-Plus**。  
也就是在傳統 **SSM（Spring + Spring MVC + MyBatis）** 的基礎上，把 **MyBatis** 換成了 **MyBatis-Plus**，並且實務上幾乎都用 **Spring Boot** 來驅動整個專案。

- **S（Spring / Spring Boot）**：容器與自動配置，管理 Bean、事務、AOP，現代專案多直接用 _Spring Boot_ 做啟動與依賴管理。
    
- **S（Spring MVC）**：Web 層的 MVC 框架，負責 Controller、路由、參數綁定、返回值處理等。
    
- **MP（MyBatis-Plus）**：在 MyBatis 之上的增強工具，提供 CRUD 通用 Mapper、Wrapper 條件構造器、分頁、代碼生成器等，降低樣板代碼量。
    

> 常見混淆：少數教材會把 **P** 說成 PageHelper，但主流語境下 **P** 指的是 **MyBatis-Plus**。若課程標題寫「SpringBoot 實現 SSMP 整合」，意思就是：以 **Spring Boot** 為基座，整合 **Spring MVC** 與 **MyBatis-Plus**。

这一章咱们学习如下技术的整合方式

- [[整合Junit]]
- [[整合 MyBatis]]
- [[整合 MyBatis-Plus]]
- [[整合 Druid]]