SQLite Statement Builder
===================================
[English](README.md) | 中文

这个项目只是一个Java编写的SQLite执行语句生成器。   

它并不能让提高你的代码执行效率，但是可以让你的Java代码更直观，更纯粹。

### 0. 快速上手
这个一个简单的例子，像这样:

```
// DELETE FROM `fruit` WHERE `name` = "apple";
Column fruit_name = new Column("name");
SQLite.delete("fruit", Clause.equals(fruit_name, "apple"));

// SELECT * FROM `fruit` WHERE `name` = "apple";
SQLite.selectAll("fruit", Clause.equals("name", "apple"));
```

### 1. 插入（INSERT）
使用工具类：

```
String table = "table";                           // 表名
Map<String, Object> cv = new HashMap<>();         // map<列, 值>批量全部
cv.put("column", "value");                        // 添加更多
// INSERT INTO `table` SET `column` = 'value';
String sql = SQLite.insert(table, cv);
```
或者使用构造类Insert:

```
String sql = new Insert(table)
		.addAll(cv)
		.add("column2", "value2")                  // 再单独添加一个
		.toString();
```

### 2. 删除（DELETE）
```
SQLite.delete(String table, Clause where);
```

### 3. 修改/更新（UPDATE）
使用工具类：

```
String table = ...;                               // 表名
Map<String, Object> cv = ...;                     // map<列名, 值>批量添加

String sql = SQLite.update(table, cv);
```
或者使用构造类Update:

```
String sql = new Update(table)
		.addAll(cv)
		.add(column, value)                      // 单独添加
		.toString();
```

### 4. 查询（SELECT）
简单地搜索全表：

```
SQLite.selectAll(String table, Clause where, int limit);
```

大多数情况，我们需要使用 select builder 来帮助我们事先复杂搜索。  

```
String sql = new Select("fruit")
		.add("*")                                  // 添加搜索列
		.add(new Virtual("ok", "alias"))           // 添加虚拟列: 'ok' AS `alias`
		.add(new Now("today"))                     // 添加函数列: NOW() AS `today`
		.join(new InnerJoin("price", "p")          // 添加外链表: INNER JOIN `price` AS `p`
				.add("column1"))                   // 添加外表列
				.add("column2", "alias0"))         // 添加带带别名的外表列
		.on("_id", "fruitId")                      // 关联条件
		.add(new Column("price", "box"))           // 允许添加外链表的列（不建议）
		.where(Clause.like("type", "apple")        // WHERE `type` LIKE 'apple'
				.and(Clause.in("fruitId", 1, 2)    // AND (`fruitId` IN {1, 2}
				    .or(Clause.equals("a", "b")))) // OR `a` = 'b')
		.groupBy(...)
		.orderBy(new Asc("_id").desc("type"))      // ORDER BY `_id` ASC, `type` DESC
		.having(...)
		.limit(2)
		.toString();
```

### 5. 约束（WHERE）
```
// 单独条件：
//      in, between, isNull, isNotNull, like,
//      equals(=), lessThan(<), lessEquals(<=), moreThan(>), moreEquals(>=)
Clause.equals(column, pattern)

// 多条件
MultiClause clause = new MultiClause(clause1)
		.and(clause2.or(clause3));                // or() 和 and() 会返回MultiClause
```

### 6. 列（Column）
```
Column(String name);                              // 不带表名
Column(String table, String name);                // 带表名
Column(table, name).as(String alias);             // 带列的“别名”
Column(table, name).as(tableAlias, nameAlias);    // 带表的“别名”
new Sum(name);                                    // 函数
new Virtual(name, alias);                         // 虚拟列
```
### 7. 创建表(CreateTable)
```
List<ColumnInfo<?>> cols = new ArrayList<>();     // 表的列
cols.add(new IntColumn("_id").autoIncrement());
String sql = SQLite.createTable("table", cols);

// 或者这样
String sql = new CreateTable("table")             // 表名
    .add(new IntColumn("_id")                     // 添加列
        .autoIncrement()                          // 自增
        .primaryKey())                            // 主键
    .add(new TextColumn("txt")
        .notNull()                                // 不为空
        .setUnique(true)                          // 唯一约束
        .defaultValue(""))                        // 默认值
    .toString();
```

### 8. 其他
```
// 工具类SQLite还提供了如下方法:
SQLite.selectInto()
SQLite.dropTable()
SQLite.addColumn() 
SQLite.renameColumn() 
SQLite.createIndex()
```

