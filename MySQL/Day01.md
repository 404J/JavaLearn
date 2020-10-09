# Day01

## 开启 profile

* SET profiling = 1;

* SHOW PROFILE [type [, type] ... ] [FOR QUERY n] [LIMIT row_count [OFFSET offset]]

## 查看当前连接数

show processlist;

## schema 和数据类型的优化

### 数据类型的优化

* 数据更小更好

* 数据类型更符合更好

* 尽量少设置为 NULL，NULL 不利于后期索引等的优化

* 字符串：varchar / char 区别

* 日期：timestamp / datetime

* 枚举：enum

### 合理使用范式和反范式

> 第一范式：表中的每一列都是原子性的
> 第二范式：表中的每一列都和主键相关
> 第三范式：表中的每一列（除外键）都和主键直接相关（多张表关联）
> 反范式：不满足范式化的数据库设计，存在数据冗余

需要在范式和反范式之间进行权衡

### 主键的选择

* 代理主键：id （推荐使用）

* 自然主键：身份证号码

### 字符集

* 推荐使用 utf8mb4

### 存储引擎（数据文件的组织形式）

* InnoDB

* MyISAM
