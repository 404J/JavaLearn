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
