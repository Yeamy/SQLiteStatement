SQLite Statement Builder
===================================

English | [中文](README-CN.md)

This project is a SQLite statement builder, allow you write SQL in Java.  

It's not a fast way to build SQL statement, but it may make your codes more clear and more intuitive.

### 0. Example
The simple demo like this:

```
// DELETE FROM `fruit` WHERE `name` = "apple";
Column fruit_name = new Column("name");
SQLite.delete("fruit", Clause.equals(fruit_name, "apple"));

// SELECT * FROM `fruit` WHERE `name` = "apple";
SQLite.selectAll("fruit", Clause.equals("name", "apple"));
```

### 1. INSERT
By static method in SQLite:

```
String table = "table";                           // table name
Map<String, Object> cv = new HashMap<>();         // column-value mapping
cv.put("column", "value");                        // to add more
// INSERT INTO `table` SET `column` = 'value';
String sql = SQLite.insert(table, cv);
```
or using class Insert:

```
String sql = new Insert(table)
		.addAll(cv)
		.add("column2", "value2")                 // add one more
		.toString();
```


### 2. DELETE
```
SQLite.delete(String table, Clause where);
```

### 3. UPDATE
By static method in SQLite:

```
String table = ...;                               // table name
Map<String, Object> cv = ...;                     // a map with <column, value>

String sql = SQLite.update(table, cv);
```
or using class Update:

```
String sql = new Update(table)
		.addAll(cv)
		.add(column, value)        // add one
		.toString();
```


### 4. SELECT
Simple way to select all colunm in one table.

```
SQLite.selectAll(String table, Clause where, int limit);
```

Most of the time, you may use the Select Builder for complex query.  

```
String sql = new Select("fruit")
		.add("*")                                  // list column in table
		.add(new Now("today"))                     // also include function: NOW() AS `today`
		.add(new Virtual("ok", "alias"))           // add virtual column: 'ok' AS `alias`
		.join(new InnerJoin("price", "p")          // join table: INNER JOIN `price` AS `p`
				.add("column1"))                   // add column in the join-table
				.add("column2", "alias0"))         // add column with alias
		.on("_id", "fruitId")                      // on
		.add("price", "box")                       // allow to add column in join-table(not suggest)
		.where(Clause.like("type", "apple")        // WHERE `type` LIKE 'apple'
				.and(Clause.in("fruitId", 1, 2)    // AND (`fruitId` IN {1, 2}
				    .or(Clause.equals("a", "b")))) // OR `a` = 'b')
		.groupBy(...)
		.orderBy(new Asc("_id").desc("type"))      // ORDER BY `_id` ASC, `type` DESC
		.limit(2)
		.toString();
```

### 5. WHERE
```
// Sigle clause:
//      in, between, isNull, isNotNull, like,
//      equals(=), lessThan(<), lessEquals(<=), moreThan(>), moreEquals(>=)
Clause.equals(column, pattern)

// Multi-clause
MultiClause clause = new MultiClause(clause1)
		.and(clause2.or(clause3));                // or() & and() may return MultiClause
```
### 6. Column
```
Column(String name);                              // no table
Column(String table, String name);                // with table
Column(table, name).as(String alias);             // with column alias
Column(table, name).as(tableAlias, nameAlias);    // with table alias
new Sum(name);                                    // function
new Virtual(name, alias);                         // virtual column
```
### 7. CreateTable
```
List<ColumnInfo<?>> cols = new ArrayList<>();     // column of table
cols.add(new IntColumn("_id").autoIncrement());
String sql = SQLite.createTable("table", cols);

// or using CreateTable
String sql = new CreateTable("table")             // table name
    .add(new IntColumn("_id")                     // add column
        .autoIncrement()                          // AI
        .primaryKey())                            // PK
    .add(new TextColumn("txt")
        .notNull()                                // NOT NULL
        .setUnique(true)                          // UNIQUE
        .defaultValue(""))                        // default value
    .toString();
```

### 8. Others
```
// There are more methods in the class SQLite:
SQLite.selectInto()
SQLite.dropTable()
SQLite.addColumn()
SQLite.renameColumn()
SQLite.createIndex()
```
