# Day02

## 使用

* help: 帮助
* auth: 认证
  auth "password"
* select: 选择库，每个库中的数据的隔离的
  select num

### string: help @string

* SET [key] [value]
* GET [key]
* SET [key] [value] NX: 不存在时，允许 set。用于多个线程竞争场景
* SET [key] [value] XX: 存在时，允许 set
* GETSET [key] [value]: get old and set
* MSET [key] [value] [key] [value]: 批量添加
* MSETNX [key] [value] [key] [value]
* MGET [key] [key]: 批量 get
* APPEND [key] [value]: 追加
* GETRANGE [key] [start] [end]: 获取范围值
* SETRANGE [key] [offset] [value]: set 替换范围
* STRLEN [key]: 获取长度

#### number string

字符串数值 OBJECT encoding [key] --> int

* INCR [key]: +1
* INCRBY [key] [increment]
* DECR [key]: -1
* DECRBY [key] [decrement]
* INCRBYFLOAT [key] [increment]: incr float

> 使用场景:

1. 秒杀，抢购

> redis 字节流，二进制安全，不将数据进行任何转换，以二进制的方式来什么存什么。所以，如果有两个服务分别开启一个客户端去连一个 Redis 时，一定要注意编码格式统一的问题。

#### bitmap

> 一个字节8位

* SETBIT [key] [offset] [value]: 默认开辟八位，长度单位为字节
* BITPOS [key] [bit] [start] [end]: 找第一个 bit 在 bitmap 的位置，start end 的单位是字节
* BITCOUNT [key] [start] [end]: 找 bit 为 1 的个数，start end 的单位是字节
* BITOP [and / or] [destkey] [key] [key]: 二进制按位操作

> 使用场景:

1. 统计随机时间段用户登录天数
  用户标识为 key，以 bitmap 作为天
2. 某段时间内的活跃用户数
  日期为 key，以 bitmap 作为登录的用户
