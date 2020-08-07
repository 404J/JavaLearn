# Day08

## Executor --> ExecutorService

## Executors 线程池工厂，生产各种 ExecutorService

## ThreadPoolExecutor, 只有一个任务队列

- 线程池的定义 new ThreadPoolExecutor
  1. corePoolSize: 持久存在的线程
  2. maximumPoolSize: 最大扩展到的线程数
  3. keepAliveTime: 扩展线程的存活时间
  4. unit: 线程存活的时间单位
  5. workQueue: BlockingQueue, 用于存储任务
  6. threadFactory: 定义产生线程的方式
  7. handler: 线程数达到最大数，且任务队列中任务已满，执行的拒绝策略。JDK默认提供四种: CallerRunsPolicy, AbortPolicy, DiscardPolicy, DiscardOldestPolicy

  ```java
  code
  ```

### SingleThreadExecutor

- 线程池中只有一个线程，为什么不自己 new 一个线程？
  1. 不用自己维护任务队列
  2. 维护线程的生命周期

### CachedThreadPool

- 核心线程数为0，最大扩展线程数是 Integer.MAX_VALUE, 线程中的维护队列为 SynchronousQueue

### FixedThreadPool

- 线程数固定

### ScheduledThreadPool 定时任务线程池

## ForkJoinPool 每个线程有自己的任务队列

### WorkStealingPool

### paralleStreamAPI
