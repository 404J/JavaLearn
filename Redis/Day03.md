# Day03

## list: help @list

> 双向链表，可以重复，有插入顺序

* LLEN [key]: 返回链表长度
* LPUSH [key] [value] [value]: 从左边添加元素
* RPUSH [key] [value] [value]: 从右边添加元素
* LPOP [key]: 弹出链表左边一个元素
* RPOP [key]: 弹出链表右边一个元素

> LPUSH + LPOP: 栈，LPUSH + RPOP: 队列

* RANGE [key] [start] [stop]: 获取一定范围的链表元素

> LRANGE [key] 0 -1: 使用反向索引获取整个链表

* LINDEX [key] [index]: 根据 index 获取链表元素
* LSET [key] [index] [element]: 更新 index 位置的元素
* LREM [key] [count] [element]: 删除 count 个元素，count 的正负决定删除方向
* LINSERT [key] [BEFORE|AFTER] [pivot] [element]: 在某个值的前面或者后面加入某个元素
* BLPOP [key] [timeout]: 阻塞弹出元素，FIFO
* LTRIM [key] [start] [stop]: 清空两边数据

## hash: help @hash

* HSET [key] [field] [value] = HMSET [key] [field] [value]
* HGET [key] [field]
* HMGET [key] [field]
* HGETALL [key]: 获取整个 hash
* HKEYS [key]: 获取整个 hash 的 keys
* HVALS [key]: 获取整个 hash 的 values
* HLEN [key]: 获取 hash 键值对的个数
* HINCRBY [key] [field] [increment]: 增加
* HINCRBYFLOAT [key] [field] [increment]

> 使用场景:

1. 详情页数据存储

## set: help @set

> 去重，无序

* SADD [key] [member]
* SCARD [key]: 获取集中的成员数
* SISMEMBER [key] [member]: 判断是否是成员
* SMEMBERS [key]: 返回所有成员
* SINTER [key] [key]: 取交集
* SUNION [key] [key]: 取并集
* SDIFF [key] [key]: 取差集
* SINTERSTORE [destination] [key] [key]: 取交集并存储
* SUNIONSTORE [destination] [key] [key]: 取并集并存储
* SDIFFSTORE [destination] [key] [key]: 取差集并存储

> 随机事件

* SRANDMEMBER [key] [count]: 随机返回，count 为正，返回去重随机数据，count 为负，返回可以重复的随机数据
* SPOP [key] [count]: 随机去除返回一个

## sorted_set: help @sorted_set

> 有序去重集合

* ZADD [key] [NX|XX] [CH] [INCR] [score] [member] [score member ...]: 添加元素，并根据分值排序
* ZRANGE [key] [start] [stop] [WITHSCORES]: 根据排名取出
* ZCARD [key]: 返回元素个数
* ZCOUNT [key] [min] [max]: 取出一定范围分值的元素个数
* ZRANK [key] [member]: 返回某个元素的排名
* ZUNIONSTORE [destination] [numkeys] [key] [key ...] [WEIGHTS weight] [AGGREGATE SUM|MIN|MAX]: 多个集合根据权重和聚合方法取并集

> 实现原理：跳表
