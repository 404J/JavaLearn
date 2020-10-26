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

> 相关度是如何计算的

* match, 搜索条件分词，搜索的字段存储时候也会分词，只要有一个搜索分词存在与搜索的字段分词中，就会命中
GET /product/_search
{
  "query": {
    "match": {
      "name": "nfc phone"
    }
  }
}

* term, 搜索条件没有分词，搜索的字段存储时候也会分词，只有整个搜索存在与搜索的字段分词中，才会命中
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

* 描述：深度分页，查询数据较靠后，可能会涉及多个分页以及较多的数据

* 解决：使用 scroll search

### filter 会使用 cache

* 原理？？？
