# Day05

## AQS 源码

- 源码
  lock unlock 略...

- VarHandle
  1. 普通属性可以进行原子性操作
  2. 比反射快

## ThreadLocal

- 概念
  保证线程之间不受影响，当前线程 set 到 ThreadLocal 的属性，只有当前线程可以读取到

  ```java
  code
  ```

- 原理
  ThreadLocal 的 set 是放到当前线程中的 map 中，get 的时候从当前线程的 map 中 get，所以线程之间不可见

## java 中的引用

- 强引用
  普通的引用属于 new 属于强引用，只有变量指向空，GC 会进行回收

  ```java
  code
  ```

- 软引用 SoftReference
  当堆内存不够用时候，软引用会被 GC 回收，用于缓存

  ```java
  code
  ```

- 弱引用 WeakReference
  遇到 GC 就会被回收，例 WeakHashMap；用于容器里，变量 m 强引用指向 WeakReference，WeakReference 弱引用指向 M，于此同时，变量 X 强引用指向 M，若 X 为null, M 会被回收

  ```java
  code
  ```
