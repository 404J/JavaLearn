# Day03

## 查询优化

### limit 分页优化

* 简单 limit 语句

原语句：会扫描 200 rows

```sql
explain select * from actor limit 190, 10;
```

优化后：会扫描 10 rows

```sql
explain select * from actor where actor_id > 190 limit 10;
```

* 包含 where 的 limit 语句

原语句：会扫描 156 rows

```sql
explain select * from actor where last_name > "d" limit 100, 10;
```

优化后：

```sql
explain select * from actor where actor_id in (select actor_id from actor where last_name > "d") limit 100, 10;
```

### 尽量不使用子查询，使用 join 代替

> 行转列？

### 自定义变量

* set @one = 1;

* set min_actor_id := (select min(actor_id) from actor);

* 作为行号使用：set @rownum := 0; select last_name, @rownum:=@rownum + 1 as row_num from actor limit 10;
