# Day01

> ES 解决模糊查询全表扫描的问题，采用倒排索引

## Lucene / ElasticSearch 区别

  Lucene：jar 包，用于创建倒排索引，提供复杂的 API。单点，集群麻烦。
  ElasticSearch：集群自动发现，面向开发者友好，基于 Lucene 实现，自动做负载均衡

## ES 主要概念

### 数据

* Index: 索引，类比于关系型数据库中的 database

* Document: 文档，数据存储的单元，类比于关系型数据库中的 row

* Field: 字段，组成 Document，类比于关系型数据库中的 colum

* Mapping:

### 集群

* Node: 节点，一个机器上可以有多个节点。基于集群自动发现，当有一个节点启动，会自动加入集群中，且会对 Shard 进行 rebalance

* Shard: 分片，位于节点上，节点的分布由 ES 自动分配，Index 创建后，Shard 数量不可变，一个节点代表一个 Lucene 实例

* Replica Shard: 副本分片，且不能和主分片存在于同一个节点上，相同的副本也不能放在同一个节点，提高可用性
