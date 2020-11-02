# Day05

## 持久化

### RDB: 定时保存快照副本文件

> 如何实现 service 非阻塞的持久化数据？

知识储备：

1. Linux 分为父子进程，且父子进程间的数据互不影响。
2. 使用 fork(系统调用) 创建子进程的时候，子进程中只是拷贝数据的引用，创建子进程速度很快，且占用空间小。当父进程修改数据时候，子进程会 copy on write，保证进程间的数据隔离

redis 实现内存数据副本持久化：
某时刻，redis 使用 fork + copy on write 创建子进程，落地的数据为该时刻的数据，且父进程的数据发生更改不会影响子进程

* redis-cli 中使用 SAVE 窗口阻塞持久化
* redis-cli 中使用 BGSAVE 后台使用 fork 持久化
* 配置文件配置

#### 弊端

* 只有一个 RDB 文件，需要人为定时 copy
* 不是实时落地，间隔时间数据可能丢失

### AOF: 实时记录写操作日志

AOF, append only file。AOF 恢复数据较慢。

#### 弊端和解决

* 体量大
* 恢复慢

> 解决: 重写 BGREWRITEAOF

1. 4.0 之前，抵消和合并一些命令
2. 4.0 之后，将老的数据以 RDB 方式写到 AOF 中，只记录增量数据

### 持久化的代价

RDB 和 AOF 都会先将数据写到操作系统内核的 buffer 中，然后内核 flash 到磁盘。持久化都会触发 IO，会降低 redis 的性能，所以 redis 提供了三种 flash 频率

* no: 是否 flash 完全由操作系统决定
* alwways: 每次都 flash 到磁盘
* everysec: 每秒 flash 到磁盘
