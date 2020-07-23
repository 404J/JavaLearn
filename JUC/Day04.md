# Day04

## LockSupport

- 例1
  LockSupport 实现使某个线程阻塞/放行

  ```java
  code
  ```

## 面试题

- 例2
  实现一个容器，提供两个方法：add、size；写两个线程，线程1添加10个元素到容器中，线程2实现监控元素的个数，当个数到5个时，线程2给出提示并结束

  - wait/notify 实现

  ```java
  code
  ```

  - 使用 CountDownLatch 实现

  ```java
  code
  ```

  - 使用 LockSupport 实现

  ```java
  code
  ```
