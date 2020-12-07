# Day03

## 事务消息

> 分布式事务的提交：1. 2PC: 两阶段提交；2. TCC：三阶段提交；rocketMQ 采用 2PC

1. 执行业务方法 a、b、c，这三个方法组成一个事务。方法 b 为向 MQ 中发送消息
2. 首先向 MQ 发送一个 half msg, 然后执行 a、c 方法（executeLocalTransaction）
3. 若此时执行 a、c 方法抛出异常，producer 向 MQ 发送 rollback 消息
4. 若此时执行 a、c 方法成功，producer 向 MQ 发送 commit 消息
5. 若 broker 长时间得不到 half msg 的确定消息，会定时主动询问 producer（checkLocalTransaction）

## 消息的重新投送

### 面向 producer

- setRetryTimesWhenSendFailed: 发送超时重新发送
- setRetryAnotherBrokerWhenNotStoreOK：某个 broker 保存失败，重新保存

### 面向 broker

- MessageModel.CLUSTERING 时候，当 消费者的 ack 超时时候，会进行重新投送

### 面向 consumer

- 接受到消息返回消费失败 RECONSUME_LATER，broker 会进行重新投送

## 避免消息的重复消费（保证幂等）

- 使用数据库检测
- 使用 redis

## 消费的顺序消费

> topic 是有 queue 组成，queue 数据结构可以保证数据的 FIFO

1. 消息发送到同一个 topic 中
2. 消息发送到同一个 topic 中的同一个 queue 中 (producer 中 MessageQueueSelector)
3. 消息发送的时候使用一个线程
4. 消息消费的是时候一个 queue 对应一个线程进行消费（consumer 中 MessageListenerOrderly）
