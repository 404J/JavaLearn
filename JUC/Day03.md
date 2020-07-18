# Day03

## LongAdder

- 例1
    AtomicInteger\synchronized\LongAdder 实现多线程下累加的执行速度对比

    ```java
    code
    ```

- LongAdder 内部使用分段锁，高并发较快

## ReentrantLock (重入锁、CAS)

- 例2
    ReentrantLock 使用

    ```java
    code
    ```

- 例3
    可以设置锁为公平锁

    ```java
    code
    ```

## CountDownLatch
  
- 例4
    监控多个线程，并等待多个线程结束执行相关操作，赛跑比赛

    ```java
    code
    ```

## CyclicBarrier
  
- 例5
    公交车发车

    ```java
    code
    ```

## Phaser

- 例6
    篮球比赛的淘汰赛阶段

    ```java
    code
    ```

## ReadWriteLock (共享锁，排他锁)

- 例7
    读锁之间为共享锁，写锁和写锁/读锁和写锁之间为排他锁

    ```java
    code
    ```

## Semaphore

- 例7
    限流

    ```java
    code
    ```
