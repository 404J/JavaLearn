# Day04

## Kafka 单机机制

### 生产者的ack

  消息的生产者生产一条消息，生产者确认消息发送成功的两种策略

- ack = 0: 消息发出即为成功
- ack = 1: 消息被broker写入到磁盘，返回成功

### 消息的写入持久化

  broker 接受到生产者的一条消息，首先写入到 kernel 的 pagecache，然后刷新到磁盘完成消息的持久化，写入磁盘的粒度是可以配置的，应该把单机的可靠性转移到集群可靠性

### offset 索引
  
  Kafka 的消息是依托磁盘进行持久化的，当消费者通过 offset 进行消息的拉取的时候，Kafka 需要依托索引进行快速定位。Kafka 内部是通过 offset 建立的索引。索引的内容为 offset: position。支持通过时间戳取数据，原理是通过时间戳索引取到 offset 索引，然后通过 offset 查询 position

  > kafka-dump-log.sh --files [indexFile]

### 消息的零拷贝消费

  当消费者消费消息的时候，Kafka 通过系统调用 sendfile，直接进行消息的发送，不需要将消息从磁盘 copy 到用户空间然后通过传统的 socket 进行发送

> 单机持久化的可靠性转向集群多机

## Kafka 集群机制

### 集群模式下，数据一致性的解决方案

- 强一致性，所有节点数据必须去全部同步，但是会破坏可用性
- 最终一致性，同步个数过半即可
- ISR（in-sync replicas）:连通性。OSR（outof-sync replicas）: 超时没有心跳连接。AR（assigned replicas）: 所有的副本数。

当 Kafka 集群的 ack 为 -1 时候：Leader 接受数据消息的时候，在心跳时间范围内，同步数据成功的 Follower 置为 ISR ，超时的 Follower 置为 OSR，Leader 返回 ack。可以保证 ISR 集合中的消息的进度是一致的。ISR 集合是弹性的、可收缩的。可以配置最少同步数量。此时 Consumer 可以消费全量数据

当 Kafka 集群的 ack 为 1 时候：可以保证 Leader 的数据写入成功，不保证 Follower 可以同步成功数据。此时 Leader 中消息的偏移量为 LEO （logEndOffset）。此时 Consumer 能够消费的偏移量为 HW （high watermark），这个偏移量为集群所有节点都有的消息偏移量。
