# Day04

## 分区表

### 概念

根据自定义规则，把一张表分成多个文件存储，类似表的水平切分

### 使用

```sql
CREATE TABLE employees (
    id INT NOT NULL,
    store_id INT NOT NULL
)
PARTITION BY RANGE (store_id) (
    PARTITION p0 VALUES LESS THAN (6),
    PARTITION p1 VALUES LESS THAN (11),
    PARTITION p2 VALUES LESS THAN (16),
    PARTITION p3 VALUES LESS THAN MAXVALUE
);
```

### 原理

增删改查之前加之判断所操作的分区

### 分区类型

* 范围分区 Range

* 列表分区 List

* 哈希分区 Hash

* 键值分区 Key 类似 Hash

* 子分区 Sub
