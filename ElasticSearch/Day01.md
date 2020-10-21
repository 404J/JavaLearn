# Day01

> ES 解决模糊查询全表扫描的问题，采用倒排索引

## Lucene / ElasticSearch 区别

  Lucene：jar 包，用于创建倒排索引，提供复杂的 API。单点，集群麻烦。
  ElasticSearch：集群自动发现，面向开发者友好，基于 Lucene 实现，自动做负载均衡

