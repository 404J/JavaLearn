# Base

## ArrayList

  底层是一个数组，数组初始化的时候长度是0，add 数据的时候默认变成10，每次扩容是之前的1.5倍

## LinkedList

  底层是一个双向链表

## HashMap

  1.7: 底层是数组 + 单链表
  1.8: 底层是数组 + 单链表或者红黑树，当单链表长度大于等于8，hash桶大于等于64的时候转换为红黑树。当红黑树的节点小于6的时候转换化为单链表

  hash 桶的默认数量是16，阈值是 0.75。当hash桶的数量大于12的时候会触发扩容，扩容为之前的2倍，老数据进行rehash

## ConcurrentHashMap

  1.7: 底层分段锁 segment 继承与 ReentrantLock
  1.8: 和 HashMap 同样的数据结构，放弃分段锁采用 sync + CAS
