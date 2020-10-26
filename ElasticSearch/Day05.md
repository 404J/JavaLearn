# Day05

## ES 底层原理

### 正排索引 vs 倒排索引

> 索引创建的时候（index time）都会生成

* 倒排索引
  * 数据的组织方式是以 term 为 key, 包含该 term 的文档 ids 为 value

* 正排索引
  * 数据的组织方式是以文档的 id 为 key, 该文档包含的 terms 为 value。用于聚合查询（不懂）

## 批量查询

GET _mget
{
  "docs": [
    {
      "_index": "product",
      "_id": 1
    }  
  ]
}

GET /product/_mget
{
  "docs": [
    {
      "_id": 1
    }  
  ]
}

GET /product/_mget
{
  "ids": [1]
}

## 批量更新

使用 _bulk...

## ES 并发问题

通过添加版本号使用乐观锁进行数据的更新。
