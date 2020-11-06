# Day07

## 解决容量有限问题

* 方案一(数据可以分类)：多个 redis 节点，存放根据业务拆分的数据。client 根据拆分逻辑访问不同的 redis
* 方案二(数据无法分类)：根据某些算法(hash + 取模 = modula) 数据存放多个 redis。后期无法更改取模数据，扩展性差,可以取模后的值比节点多，一个节点可以 mapping 多个取模的值（槽位）。当节点增加的时候，相应槽位的数据需要迁移
* 方案三(数据无法分类)：数据随机(random)存放多个 redis。可以放进去但是无法准确取出，但是可以用于消息队列
* 方案四(数据无法分类)：一致性 hash 算法，data 和 node 做 hash, 详见课件图示。通过虚拟环形，解决数据的分布，但是新加节点的时候，会造成部分数据收到影响，无法命中，但是可以取最近的两个点，但是提升了复杂度

以上方案都是客户端直连 redis, 成本高。中间可以添加代理 proxy（nginx）,中间代理层是无状态的，做数据分发（modula/random）逻辑。但是保证中间件的 HA 可以使用 keepalived。可以使用 LVS vip 实现多个 proxy 透明，只对外暴露一个地址

> github: twemproxy， predixy [功能对比文章](https://blog.csdn.net/rebaic/article/details/76384028)

## redis cluster 的数据分片

> 数据分治带来的问题：
聚合操作很难操作，事务也很难实现。使用 hash tag 人为让数据放在一起

## twemproxy

* https://github.com/twitter/twemproxy 编译
* cp src/nutcracker /usr/bin
* vim conf/nutcracker.yml

  ```conf
  alpha:
    listen: 127.0.0.1:22121
    hash: fnv1a_64
    distribution: ketama
    auto_eject_hosts: true
    redis: true
    server_retry_timeout: 2000
    server_failure_limit: 1
    servers:
    - 127.0.0.1:6379:1
    - 127.0.0.1:6380:1
  ```

* redis-server --port 6379
* redis-server --port 6380
* redis-cli -p 22121

## predixy

* 下载编译后的包 wget https://github.com/joyieldInc/predixy/releases/download/1.0.5/predixy-1.0.5-bin-amd64-linux.tar.gz

* 更改 predixy.conf
* 更改 sentinel.conf

```conf
SentinelServerPool {
    Databases 16
    Hash crc16
    HashTag "{}"
    Distribution modula
    MasterReadPriority 60
    StaticSlaveReadPriority 50
    DynamicSlaveReadPriority 50
    RefreshInterval 1
    ServerTimeout 1
    ServerFailureLimit 10
    ServerRetryTimeout 1
    KeepAlive 120
    Sentinels {
        + 127.0.0.1:26379
        + 127.0.0.1:26380
        + 127.0.0.1:26381
    }
    Group xxoo {
    }
    Group ooxx {
    }
}
```

* 更新 sentinel 配置

``` conf
port 26379
sentinel monitor xxoo 127.0.0.1 36379 2
sentinel monitor ooxx 127.0.0.1 46379 2
```

``` conf
port 26380
sentinel monitor xxoo 127.0.0.1 36379 2
sentinel monitor ooxx 127.0.0.1 46379 2
```

``` conf
port 26381
sentinel monitor xxoo 127.0.0.1 36379 2
sentinel monitor ooxx 127.0.0.1 46379 2
```

* 启动哨兵

```bash
redis-server s6379.conf --sentinel
redis-server s6380.conf --sentinel
redis-server s6381.conf --sentinel
```

* 启动 redis

```bash
redis-server --port 36379
redis-server --port 36380 --replicaof 127.0.0.1 36379
redis-server --port 46379
redis-server --port 46380 --replicaof 127.0.0.1 46379
```

* 启动 predixy ./predixy  ../conf/predixy.conf

* 连接代理 redis-cli -p 7617

## redis cluster

> redis-cli --cluster help 可以查看相应功能

* 启动多个 redis 实例： ./create-cluster start
* 创建 redis 集群：redis-cli --cluster create 127.0.0.1:30001  127.0.0.1:30002 127.0.0.1:30003 127.0.0.1:30004 127.0.0.1:30005 127 .0.0.1:30006 --cluster-replicas 1
* 连接客户端：redis-cli -c -p 30001
