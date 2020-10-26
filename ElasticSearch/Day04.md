# Day04

## Mapping

### 查看 mapping

GET /product/_mapping

### Dynamic mapping

会根据 push 的数据自动 map 数据的字段类型

### 手动创建 mapping

PUT /product_map_test
{
  "mappings": {
    "properties": {
      "create_data": {
        "type": "date"
      }
    }
  }
}

GET /product_map_test/_mapping

### mapping params

* index: 是否对该字段建立倒排索引，如果为 false，则该字段不可以作为查询条件
* analyzer
* coerce: 是否可以进行类型转换，如果为 false，则插入的时候不可以进行类型转换
* copy_to: 可以使用该字段查询，但是该字段不存储内容
    PUT product_copy
    {
      "mappings": {
        "properties": {
          "name": {
            "type": "text",
            "copy_to": "name_copy"
          },
          "name_copy": {
            "type": "text"
          }
        }
      }
    }

    GET /product_copy/_search
    {
      "query": {
        "match": {
          "name_copy": "mars"
        }
      }
    }
* doc_values: 是否生成正排索引, 不可改变。 当 type 为 text 时候，不会创建正排索引，所以不能用 text 类型字段进行排序和聚合操作
* search_analyzer: 设置搜索时候的分词器
* fielddata: ????

## 聚合查询

GET /product/_search
{
  "aggs": {
    "NAME": {
      "terms": {
        "field": "tags.keyword"
      }
    }
  },
  "size": 0
}
....
