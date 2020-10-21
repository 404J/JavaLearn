# Day05

## 服务器参数的设置

### 连接

* max_connections

* max_user_connections

### 日志

* redo: InnoDB级别, 保证持久性, 事物提交后，redo log 记录数据的更改

* undo: InnoDB级别, 保证原子性, 数据变更时候，undo log

* binlog: mysql server 层 log, 用于数据的恢复

> 事物的四大特性：⑴ 原子性（Atomicity）、⑵ 一致性（Consistency）、⑶ 隔离性(锁机制)（Isolation）、⑷ 持久性（Durability）

* general_log: 是否开启查询 log

* slow_query_log: 是否开启慢查询 log

### 缓存

* key_buffer_size: 索引缓存

* query_cache: 查询缓存， 可以运用于常量表

* sort_buffer_size: 排序缓存

* join_buffer_size: 关联查询缓存大小

* thread_cache_size: 连接线程数量大小

### INNODB

* innodb_buffer_pool_size: 缓冲数据和索引大小

* innodb_flush_log_at_trx_commit: 控制 log buffer 中的数据写入日志文件和磁盘的时间点，有三种策略

* innodb_log_buffer_size: 日志缓冲区

* innodb_log_file_size: 日志文件大小

## 锁机制

### MyISAM 表级锁（存储引擎自动加锁）

* 独占写锁: 其他会话不能写和读，当前会话可以写和读

* 共享读锁: 读是共享操作，但是不可读其他表。当前会话和其他会话都不可以写

### InnoDB 默认为行级锁，无锁引则为表级锁

* 共享锁

* 排他锁

* 自增锁
