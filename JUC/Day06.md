# Day06

## BlockingQueue 阻塞队列

- 例1
  实现生产者消费者, put / take 为阻塞方法

  ```java
  public class Test {
    public static void main(String[] args) throws InterruptedException {
      BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>();
      for (int i = 0; i < 2; i++) {
        new Thread(() -> {
          while (true) {
            try {
              blockingQueue.put("Item" + Math.random());
            } catch (InterruptedException e) {
            }
          }
        }).start();
      }

      for (int i = 0; i < 10; i++) {
        new Thread(() -> {
          while (true) {
            try {
              System.out.println(blockingQueue.take());
            } catch (InterruptedException e) {
            }
          }
        }).start();
      }
    }
  }
  ```

- Queue / List 的区别
  1. Queue 添加了一些对线程友好的api, offer/peek/poll/put/take 等

### DelayQueue 按时间进行任务调度

### SynchronusQueue 用于一对一个线程交换数据，SynchronusQueue 内部不维护存储空间，直接交付

- 例3

  ```java
  public class Test {
    public static void main(String[] args) throws InterruptedException {
      SynchronousQueue<Object> sQueue = new SynchronousQueue<>();
      Object o = new Object();
      new Thread(() -> {
        try {
          TimeUnit.sleep(2);
          sQueue.put(o);
          System.out.println("put");
        } catch (InterruptedException e) {
        }
      }).start();
      sQueue.take();
      System.out.println("take");
    }
  }
  ```

### TransferQueue 用于一对多个线程交换数据

- 例4

  ```java
  public class Test {
    public static void main(String[] args) throws InterruptedException {
      LinkedTransferQueue<Object> lQueue = new LinkedTransferQueue<>();
      Object o = new Object();
      new Thread(() -> {
        try {
          System.out.println(Thread.currentThread().getName() + lQueue.take());
        } catch (InterruptedException e) {
        }
      }, "t2").start();
      lQueue.transfer(o);
    }
  }
  ```
