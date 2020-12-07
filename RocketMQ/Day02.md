# Day02

## 消息的生产过程

1. 设置 producerGroup
2. 设置 nameServer 地址
3. 向 topic send msg

## 发送消息的类型

1. 同步发送：阻塞
2. 异步发送：通过事件回调收到消息
3. send one way：不会收到消息发送的 ack，不会保证消息一定发送成功

## 消息的消费过程

1. 设置 consumerGroup
2. 设置 nameServer 地址
3. 订阅某个 topic
4. 注册消息监听器

## 消息的消费类型

1. CLUSTERING： 集群模式，只被集群消费一次，关心 ack
2. BROADCASTING：广播模式，会被所有订阅该 topic 的消费者推送，不关心 ack

> 一个 consumerGroup 中的消费者的配置应该是一致的，否则会导致混乱

## 消息的过滤

### Tag

1. 消息对象添加 tag
2. 消费者订阅消息时候添加 tag 字段的过滤信息

### Sql

1. broker 配置中开启：enablePropertyFilter=true
2. 消息对象 putUserProperty
3. 消费者订阅消息的时候添加 MessageSelector 通过 sql 过滤消息

## 事务消息

> 分布式事务的提交：1. 2PC: 两阶段提交；2. TCC：三阶段提交；rocketMQ 采用 2PC
