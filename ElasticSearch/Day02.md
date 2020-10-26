# Day02

## 集群健康值

* _cat/health
* _cluster/health

## 简单的CRUD

* 创建索引: PUT /product?pretty
* 查询索引: GET /_cat/indices?v
* 删除索引: DELETE /product

* 插入数据: PUT /product/_doc/1 { "name": "apple" }
* 查询数据:
  * 根据 id 查找: GET /product/_doc/1
  * 查找所有: GET /product/_doc/_search
* 修改:
  * 全量修改: PUT /product/_doc/1 { "name": "HUAWEI", "country": "CN" }
  * 指定字段更新: POST /product/_update/1 { "country": "US" }
* 删除数据: DELETE /product/_doc/1

## 容灾机制

1. Master 选举，Master 节点宕机。
2. 新 Master 节点中的相应的 Replica Shard 升级为 Primary Shard, 并尝试重启原 Master 节点
3. 数据同步，新 Master 节点中的 新的 Primary Shard 数据同步到原 Master 节点的新 Replica Shard

### Master 选举

[📃精选文章](https://blog.csdn.net/ailiandeziwei/article/details/87856210)
