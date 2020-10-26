# Day06

## painless script

* 更新
  POST product/_update/1
  {
    "script": {
      "source": "ctx._source.price+=1"
    }
  }

  POST product/_update/1
  {
    "script": {
      "source": "ctx._source.tags.add(params.tags_name)",
      "params": {
        "tags_name": "wuxianchongdian"
      }
    }
  }

* 删除
  POST /product/_update/6
  {
    "script": {
      "source": "ctx.op='delete'"
    }
  }

* upsert
  POST /product/_update/7
  {
    "script": {
      "source": "ctx._source.tags.add(params.tags_name)",
      "params": {
        "tags_name": "wuxianchongdian"
      }
    },
    "upsert": {
      "name": "test_upsert",
      "tags": ["test"]
    }
  }
