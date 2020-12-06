# Day01

## 启动

- 启动nameserver `./mqnamesrv`
- 启动Broker `./mqbroker -n localhost:9876`
- 启动控制台 `java -jar rocketmq-console-ng-1.0.1.jar --rocketmq.config.namesrvAddr=127.0.0.1:9876`

## 消息中间件的作用

- 服务应用解耦：服务之间不直接调用，通过消息中间件进行解耦，异步调用，不存在事务关系
- 流量削峰：高并发请求，首先放到消息中间件hold住请求，然后后面的服务进行逐步处理

## RocketMQ 中的角色

- Broker
  - 面向 Producer 和 Consumer，Producer 收集消息，放到 MQ 中，Consumer 进行消费消息
  - Broker 节点进行启动的时候件，都会遍历 NameServer, 建立长连接，注册自己的信息。
  - Broker 可以基于集群，读写分离
- NameServer
  - 底层由netty实现，提供了路由管理、服务注册、服务发现的功能，是一个无状态节点
  - 集群中各个角色（producer、broker、consumer等）都需要定时想nameserver上报自己的状态，以便互相发现彼此，超时不上报的话，nameserver会把它从列表中剔除
  - NameServer 可以基于集群，但是互相之间不进行通信
- Producer
  - 消息的生产者
  - 通过 NameServer 获取 Topic 的路由信息，然后与 master Broker 建立长连接
- Comsumer
  - 消息的消费者
  - 通过 NameServer 获取 Topic 的路由信息，然后与 Broker 建立长连接
