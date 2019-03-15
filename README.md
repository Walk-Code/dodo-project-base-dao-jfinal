# 基于jfinal ORM层二次封装并引入Druid
> 使用方式（并未在中央仓库中存放，需要存放在私服）
```java
<dependency>
   <groupId>com.dodo.project.base.dao.jfinal</groupId>
       <artifactId>dodo-project-base-dao-jfinal</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```  
---
## 主要实现功能
1. 通过自定义注解的方式自定义方法级别事务，基于jfinal事务，默认事务级别是4
> 使用（伪代码）
```java
@JFinalORMTransaction(dbname = xxx)
public void test() {
	// TODO
}

```
2. dao层封装了基础的ORM操作，封装的方法：

method        | description
------        | ---  
save(model)   | 保存[自动插入数据和更新数据]
delete(model) | 删除数据

