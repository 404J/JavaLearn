# Day01

## 简介

Kafka 为一个消息中间件，可用于服务之间的解耦。可靠，可扩展，高性能

## Kafka 的 AKF

- 通过副本保证可用（多节点），主节点负责读写，副本不对外提供服务，保证数据一致（X）
  > 复制集群有两种提供服务方式：1. 主从复制，读写分离，写操作只发生在主节点，保证吞吐量，牺牲一致性。2. 主备复制，主节点负责读写，副本不对外提供服务，保证数据一致，牺牲吞吐量
- 通过不同的 topic 进行逻辑业务划分（Y）
- topic 中通过 partition 在进行细分，无关的数据打散到不同的 partition 中，以追求并发，并行。有关的数据放到同一个 partition 中以保证顺序性（Z）

## 内部角色划分

### Zookeeper

  Kafka 依赖 Zookeeper 对 Broker 进行注册管理

### Broker

  Broker 是 Kafka 中的物理划分，可以存在多个 Broker，不同的 Broker 可以接收同一个 Topic 的消息

### Topic

  Topic 是 Kafka 中的逻辑划分，一个 Topic 中包含多个 Partition

### Partition

  Partition 是物理划分，多个 Partition 存储同一个 Topic 的内容
