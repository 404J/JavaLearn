# Day03

## ES 查询语法

### Query string

### DSL


GET /product/_search
GET /product/_doc/4
GET /product/_search?q=name:XIAOMI
GET /product/_search?from=1&size=2

GET /product/_search
{
  "query": {
    "match_all": {}
  }
}

GET /product/_search
{
  "query": {
    "match": {
      "name": "APPLE"
    }
  }
}

GET /product/_search
{
  "query": {
    "match": {
      "name": "APPLE"
    }
  },
  "sort": [
    {
      "_id":  "desc"
    }
  ]
}

GET /product/_search
{
  "query": {
    "multi_match": {
      "query": "APPLE",
      "fields": ["name", "contry"]
    }
  }
}

GET /product/_search
{
  "query": {
    "multi_match": {
      "query": "APPLE",
      "fields": ["name", "contry"]
    }
  },
  "_source": ["name"]
}
