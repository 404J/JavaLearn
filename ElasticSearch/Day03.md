# Day03

## ES 查询语法

### Query String

* 查询所有
  GET /product/_search
* 根据 id 查询
  GET /product/_doc/1
* 字段匹配
  GET /product/_search?q=name:XIAOMI
* 分页
  GET /product/_search?from=1&size=2

### DSL

* 查询所有
GET /product/_search
{
  "query": {
    "match_all": {}
  }
}

* 单个字段匹配
GET /product/_search
{
  "query": {
    "match": {
      "name": "xiaomi"
    }
  }
}

* 排序
GET /product/_search
{
  "query": {
    "match": {
      "name": "xiaomi"
    }
  },
  "sort": [
    {
      "_id": {
        "order": "desc"
      }
    }
  ]
}

* 多个字段匹配
GET /product/_search
{
  "query": {
    "multi_match": {
      "query": "nfc",
      "fields": [
        "name", "desc"
      ]
    }
  }
}

* 查询部分字段
GET /product/_search
{
  "query": {
    "multi_match": {
      "query": "nfc",
      "fields": [
        "name", "desc"
      ]
    }
  },
  "_source": [
    "name"  
  ]
}

* 分页
GET /product/_search
{
  "query": {
    "match_all": {}
  },
  "sort": [
    {
      "_id": {
        "order": "asc"
      }
    }
  ],
  "from": 1,
  "size": 2
}

#### 全文检索

> 数据类型 text 和 keyword, text会被分词，keyword 不会被分词

* match, 模糊查询。搜索条件分词，只要有一个搜索分词命中，就会命中
GET /product/_search
{
  "query": {
    "match": {
      "name": "nfc phone"
    }
  }
}

* term, 精确查询。搜索条件没有分词，只有整个搜索字段命中，才会命中
GET /product/_search
{
  "query": {
    "term": {
      "name": "nfc phone"
    }
  }
}

* terms 等价与 match 方式，类似于 in
GET /product/_search
{
  "query": {
    "terms": {
      "name": ["nfc", "phone"]
    }
  }
}

#### 短语搜索

* 使用关键词 match_phrase, 且短语需要符合搜索中短语的顺序
GET /product/_search
{
  "query": {
    "match_phrase": {
      "name": "nfc phone"
    }
  }
}

#### 复合查询

* 使用 bool 字段进行条件的组合, 其中 must 表示为 and, must_not 表示为 not and, should 表示为 or, filter???

GET /product/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "desc": "shouji"
          }
        }
      ],
      "must_not": [
        {
          "match": {
            "name": "nfc"
          }
        }
      ],
      "should": [
        {
          "range": {
            "price": {
              "gte": 2999
            }
          }
        }
      ],
      "filter": [
        {
          "match": {
            "desc": "zhandouji"
          }
        }
      ]
    }
  }
}

* minimum_should_match, 至少满足的 should 条件个数
GET /product/_search
{
  "query": {
    "bool": {
      "should": [
        {
          "range": {
            "price": {
              "gte": 3000
            }
          }
        },
        {
          "range": {
            "price": {
              "lt": 999
            }
          }
        }
      ],
      "minimum_should_match": 1
    }
  }
}

* should 和 must 同时存在时候，minimum_should_match 默认为 0, 可以手动指定
GET /product/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "name": "phone"
          }
        }
      ],
      "should": [
        {
          "range": {
            "price": {
              "gte": 3000
            }
          }
        },
        {
          "range": {
            "price": {
              "lt": 999
            }
          }
        }
      ]
    }
  }
}

### Deep paging 问题

* 描述：使用 from + size 实现 深度分页，查询数据较靠后，可能会涉及多个分页以及较多的数据，效率较低

* 解决
  * scroll search: 保存一份当前索引的快照，第一次查询生成一个 scroll_id 用于下一次查询，且有有效时间，占用空间大，需要手动清除，查询的数据不是实时性的。
    GET /product/_search?scroll=1m
    {
      "query": {
        "match_all": {}
      },
      "sort": [
        "_doc"
      ],
      "size": 1
    }

    GET /_search/scroll
    {
      "scroll": "1m",
      "scroll_id": "FGluY2x1ZGVfY29udGV4dF91dWlkDnF1ZXJ5VGhlbkZldGNoAxRncG5DWW5VQl93V0c5a2dobXV0ZgAAAAAAAAOmFlpWTFdoemtvU0g2a3B2YWNjYzVQbWcUZzVuQ1luVUJfd1dHOWtnaG11dnkAAAAAAAADpxZaVkxXaHprb1NINmtwdmFjY2M1UG1nFEc0TENZblVCNW5HS3JfbGhtNDc3AAAAAAAAA2MWZzBDY0s4akVTaHVOV0tDQ3RYc0Rydw=="
    }
  * search_after: 适用于下一页查询，实时，效率高。但是不适用于跳页
    GET /product/_search
    {
      "sort": [
        {
          "price": {
            "order": "desc"
          }
        }
      ],
      "size": 1
    }

    GET /product/_search
    {
      "sort": [
        {
          "price": {
            "order": "desc"
          }
        }
      ],
      "search_after": [2999],
      "size": 1
    }

### filter 不会进行 score 计算，会被先执行。会使用 cache

* cache 原理: 多次匹配会进行关键词的 cache , 会根据倒排索引生成一个 bitmap (二进制数组[0, 1, 1, 0])，当元数据发生更新时候，缓存也会进行更新
