# Day01

## 线程启动方式

- 继承 Thread 类，实现 run 方法，调用 start 方法

- 实现 Runnable 接口，实现 run 方法，new Thread(new MyRunnale).start()

- 通过线程池启动

## 线程中基本的方法

- sleep
    让出 CPU 一段时间不进行执行，然后状态进入就绪状态
- yield
    让出 CPU, 但线程状态还是就绪状态，有可能立即继续执行
- join
    一个线程中调用其他线程，等待其执行完，在继续执行原来线程

## JVM中的线程的状态

- NEW

- Ready

- Running

- TimedWaiting

- Waiting

- Blocked

- Teminated

## synchronized 关键字

- synchronized(new Object())，可以锁任何对象

- synchronized(this) 代码块和 synchronized 方法等价

- static 方法中 synchronized 锁的是 MyClass.class 对象，不是 this

### 例1

    对同一个数值进行操作，得到与期望结果不同

### 例2

    一个同步方法执行的时候，另一个非同步方法可以执行。脏读例子实现。
