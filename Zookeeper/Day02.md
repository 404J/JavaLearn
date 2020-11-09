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
  * Client 发送 write 请求到一个 Follower
  * Follower 转发 write 请求到 Leader
  * Leader 生成一个 Zxid, 然后向所有的 Follower 广播该写请求
  * Follower 收到请求，记录日志 log，并返回 OK
  * Leader 判断 OK 数过半，然后向所有 Follower 广播该写请求生效，Follower 异步进行写
  * Leader 返回，Client 收到写成功

> 关键点：投票数过半，Leader 对于写进行两阶段提交。当 Follower 收到写请求前，如果收到读请求，提供了一个 sync 接口保证数据最终一致性

## watch
