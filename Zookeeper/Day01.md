# Day01

## Zookeeper 概述

zookeeper 是一个分布式的协调服务，基于主从复制的集群，数据存于内存，结构为目录树。目录树中存在 node 概念。
zookeeper 是二进制安全的，所有写操作由 leader 进行，follower 进行读操作

## 角色

* leader: 只有一个，负责读和写
* follower: 可以有多个，负责读数据和leader选举
* observer: 可以有多个，负责读数据

## 集群部署

[指导](https://blog.csdn.net/java_66666/article/details/81015302)

## node 属性

* cZxid: create 操作的 id，由 leader 维护，全局统一，前32位表示 leader 的纪元，后32位表示递增 id
* mZxid: modify 操作的 id
* pZxid: 节点下最新的节点的 cZxid

> 客户端连接 server 的时候会有一个 session id 的概念，session 的创建和消亡时候，leader 都会消耗一个事务 id(cZxid)去做统一视图。

* ephemeralOwner：临时节点的归属者， 使用 create -e 创建，归属者为该次连接的 session id，随着 session 的生命周期存在

> 当多个 follower 同时创建同一个节点的时候，会发生覆盖问题，可以使用 create -s 进行解决，此时会维护一个分布式的 id，且递增

## 功能以及如何实现

* 命名服务：通过 `create -e -s /id` 返回一个递增的、分布式唯一的 id
