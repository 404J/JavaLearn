# Day07

## 面试题：两个线程同时运行，一个打印 A ~ Z，一个打印 1 ~ 26，需实现字母数字交替打印

- 例1
  LockSupport 实现

  ```java
  public class Test {
    static Thread t1, t2;
    public static void main(String[] args) throws InterruptedException {
      char[] alphabets = "ABCDEF".toCharArray();
      char[] numbers = "123456".toCharArray();
      t1 = new Thread(() -> {
        for (char c : alphabets) {
          System.out.println(c);
          LockSupport.unpark(t2);
          LockSupport.park();
        }
      });
      t2 = new Thread(() -> {
        for (char c : numbers) {
          LockSupport.park();
          System.out.println(c);
          LockSupport.unpark(t1);
        }
      });
      t1.start();
      t2.start();
    }
  }
  ```

- 例2
  synchronized, wait, notify 实现

  ```java
  public class Test {
    static Thread t1, t2;

    public static void main(String[] args) throws InterruptedException {
      char[] alphabets = "ABCDEF".toCharArray();
      char[] numbers = "123456".toCharArray();
      Object o = new Object();
      t1 = new Thread(() -> {
        for (char c : alphabets) {
          synchronized(o) {
            System.out.println(c);
            try {
              o.wait();
              o.notify();
            } catch (InterruptedException e) {
            }
            o.notify();
          }
        }
      });
      t2 = new Thread(() -> {
        for (char c : numbers) {
          synchronized(o) {
            System.out.println(c);
            try {
              o.notify();
              o.wait();
            } catch (InterruptedException e) {
            }
            o.notify();
          }
        }
      });
      t1.start();
      t2.start();
    }
  }
  ```

- 例3
  lock, condition 实现

  ```java
  public class Test {
    static Thread t1, t2;

    public static void main(String[] args) throws InterruptedException {
      char[] alphabets = "ABCDEF".toCharArray();
      char[] numbers = "123456".toCharArray();
      Lock lock = new ReentrantLock();
      Condition c1 = lock.newCondition();
      Condition c2 = lock.newCondition();
      t1 = new Thread(() -> {
        for (char c : alphabets) {
          lock.lock();
          System.out.println(c);
          try {
            c2.signal();
            c1.await();
          } catch (InterruptedException e) {
          }
          lock.unlock();
        }
      });
      t2 = new Thread(() -> {
        for (char c : numbers) {
          lock.lock();
          System.out.println(c);
          try {
            c1.signal();
            c2.await();
          } catch (InterruptedException e) {
          }
          lock.unlock();
        }
      });
      t1.start();
      t2.start();
    }
  }
  ```

- 例4
  CAS 实现

  ```java
  public class Test {
    static Thread t1, t2;
    static volatile boolean flag = true;
    public static void main(String[] args) throws InterruptedException {
      char[] alphabets = "ABCDEF".toCharArray();
      char[] numbers = "123456".toCharArray();
      t1 = new Thread(() -> {
        for (char c : alphabets) {
          while(flag) {}
          System.out.println(c);
          flag = true;
        }
      });
      t2 = new Thread(() -> {
        for (char c : numbers) {
          while(!flag) {}
          System.out.println(c);
          flag = false;
        }
      });
      t1.start();
      t2.start();
    }
  }
  ```

- 例5
  BlockingQueue 实现

  ```java
  public class Test {
    static Thread t1, t2;

    public static void main(String[] args) throws InterruptedException {
      char[] alphabets = "ABCDEF".toCharArray();
      char[] numbers = "123456".toCharArray();
      BlockingQueue<Object> bQueue1 = new LinkedBlockingQueue<>(1);
      BlockingQueue<Object> bQueue2 = new LinkedBlockingQueue<>(1);
      Object o = new Object();

      t1 = new Thread(() -> {
        for (char c : alphabets) {
          try {
            System.out.println(c);
            bQueue1.put(o);
            bQueue2.take();
          } catch (InterruptedException e) {
          }
        }
      });
      t2 = new Thread(() -> {
        for (char c : numbers) {
          try {
            bQueue1.take();
            System.out.println(c);
            bQueue2.put(o);
          } catch (InterruptedException e) {
          }
        }
      });
      t1.start();
      t2.start();
    }
  }
  ```

- 例6
  TransferQueue 实现

  ```java
  public class Test {
    static Thread t1, t2;
    public static void main(String[] args) throws Exception {
    char[] alphabets = "ABCDEF".toCharArray();
      char[] numbers = "123456".toCharArray();
      LinkedTransferQueue<Object> lQueue = new LinkedTransferQueue<>();

      t1 = new Thread(() -> {
        for (char c : alphabets) {
          try {
            System.out.println(lQueue.take());
            lQueue.transfer(c);
          } catch (Exception e) {
          }
        }
      });
      t2 = new Thread(() -> {
        for (char c : numbers) {
          try {
            lQueue.transfer(c);
            System.out.println(lQueue.take());
          } catch (Exception e) {
          }
        }
      });
      t1.start();
      t2.start();
    }
  }
  ```

## Callable, 类似于 Runnable, 但是 call 方法有返回值

## Future, 接收 Callable 的返回

## FutureTask, Future + Runnable

- 例7

  ```java
  public class Test {
    public static void main(String[] args) throws Exception {
      ExecutorService eService = Executors.newCachedThreadPool();
      Future<?> futureRunnable = eService.submit(() -> {
        TimeUnit.sleep(1);
      });
      Future<?> futureCallable = eService.submit(() -> {
        TimeUnit.sleep(2);
        return 1;
      });
      FutureTask<Integer> futureTask = new FutureTask<>(()->{
        TimeUnit.sleep(3);
        return 2;
      });
      eService.submit(futureTask);
      System.out.println("Runnable:" + futureRunnable.get());
      System.out.println("Callable:" + futureCallable.get());
      System.out.println("FutureTask:" + futureTask.get());
    }
  }
  ```

## CompletableFuture, 任务管理器, 类似 js 中的 Promise

- 例7

  ```java
  public class Test {
    public static void main(String[] args) throws Exception {
      CompletableFuture<?> cFuture1 = CompletableFuture.supplyAsync(() -> {
        TimeUnit.sleep(1);
        System.out.println("1s later~");
        return "t1";
      });
      CompletableFuture<?> cFuture2 = CompletableFuture.supplyAsync(() -> {
        TimeUnit.sleep(2);
        System.out.println("2s later~");
        return "t2";
      });
      CompletableFuture<?> cFuture3 = CompletableFuture.supplyAsync(() -> {
        TimeUnit.sleep(3);
        System.out.println("3s later~");
        return "t3";
      });
      CompletableFuture.allOf(cFuture1, cFuture2, cFuture3).join();
    }
  }
  ```
