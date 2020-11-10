# Day02

## 扩展性

* 多个角色 leader、follower、observer
* 读写分离

## 可靠性

* 快速恢复 leader
* 数据一致性：最终一致性

> 最终一致性的过程中，节点是否对外提供服务？

## paxos 协议: 分布式一致性算法

[指导](https://www.douban.com/note/208430424/)

## ZAB 协议：zk 对于 paxos 的简单实现

* 含义：zookeeper 原子广播协议
* 条件：作用在可用状态，有 leader
* 作用：保证最终数据的最终一致性
* 流程：
  1. Client 发送 write 请求到一个 Follower
  2. Follower 转发 write 请求到 Leader
  3. Leader 生成一个 Zxid, 然后向所有的 Follower 广播该写请求
  4. Follower 收到请求，记录日志 log，并返回 OK
  5. Leader 判断 OK 数过半，然后向所有 Follower 广播该写请求生效，Follower 异步进行写
  6. Leader 返回，Client 收到写成功

> 关键点：投票数过半，Leader 对于写进行两阶段提交。当 Follower 收到写请求前，如果收到读请求，提供了一个 sync 接口保证数据最终一致性。整个流程中，Leader 对于每个 Follower 的操作都会有个队列进行维护

## 选举过程（快）

1. 两两通信
2. 每个 Follower 都有自己的 myid和Zxid
3. 成为 Leader 的必要条件是首先经验最丰富（Zxid最高），如果 Zxid 一致，需要比较 myid。然后投票过半即可当选
4. 同步数据

## watch

> 场景：Client1 需要得知 Client2 是否正常，通过心跳检测实现，但是心态检测需要 Client 自己实现且时效性差，如何使用 zookeeper 实现？

Client2 连接到 zookeeper ，建立一个 临时节点，同时 Client1 监听这个临时节点，当 Client2 宕机的时候，临时节点的相关 session 消失，zookeeper 回进行对 Client1 的回调，及时通知 Client1
