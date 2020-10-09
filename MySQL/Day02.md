# Day02

## 执行计划

### 显示执行计划

* explain [sql]

### 看文档

[文档](https://github.com/bjmashibing/InternetArchitect/blob/master/13mysql%E8%B0%83%E4%BC%98/mysql%E6%89%A7%E8%A1%8C%E8%AE%A1%E5%88%92.md)

### 主要参数解释

* id

select查询的序列号，包含一组数字，表示查询中执行select子句或者操作表的顺序

* type

访问类型，访问类型表示我是以何种方式去访问我们的数据，最容易想的是全表扫描，直接暴力的遍历一张表去寻找需要的数据，效率非常低下，访问的类型有很多，效率从最好到最坏依次是：
system > const > eq_ref > ref > fulltext > ref_or_null > index_merge > unique_subquery > index_subquery > range > index > ALL
一般情况下，得保证查询至少达到range级别，最好能达到ref

## 索引优化

* 存储结构：InnoDB \ MyISAM 使用 B+ 树进行索引的存储，B+ 树只在叶子节点存储数据，其他节点只存储指针和索引值

* 类型：主键索引（存数据），唯一索引，普通索引（存主键），全文索引，组合索引

* 面试名词：
  * 回表：通过 name（普通索引）查询到 id（主键），然后通过 id (主键) 主键索引查询到数据「 select * from people where name="mars" 」
  * 覆盖索引：通过 name（普通索引）查询 id（主键）「 select id from people where name="mars" 」
  * 最左匹配：通过 name （普通索引）查询数据时候，组合索引（age, name）不起作用。通过 age （普通索引）查询数据时候，组合索引（age, name）起作用。
  * 索引下推：


