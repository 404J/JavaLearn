# Day04

## LockSupport

- 例1
  LockSupport 实现使某个线程阻塞/放行, unpark 可以在 park 之前运行，不同于 notify 和 wait

  ```java
  public class Test {
    public static void main(String[] args) {
      Thread t = new Thread(() -> {
        for (int i = 0; i < 10; i++) {
          if(i == 5) {
            LockSupport.park();
          }
          System.out.println(i);
          TimeUnit.sleep(1);
        }
      });
      t.start();
      TimeUnit.sleep(8);
      System.out.println("8 seconds later~");
      LockSupport.unpark(t);
    }
  }
  ```

## 面试题

- 例2
  实现一个容器，提供两个方法：add、size；写两个线程，线程1添加10个元素到容器中，线程2实现监控元素的个数，当个数到5个时，线程2给出提示并结束

  - volatile 实现, 但是线程2打印的时机有点问题,可能已经添加超过5个，才会打印

  ```java
  public class Test {
    List<Object> list = new ArrayList<Object>();
    volatile int count = 0;
    // int count = 0;

    public synchronized void add(Object o) {
      list.add(o);
      count ++;
    }

    public synchronized int getCount() {
      return count;
    }

    public static void main(String[] args) {
      Test test = new Test();
      new Thread(() -> {
        System.out.println(Thread.currentThread().getName() + " start");
        while(true) {
          if(test.count == 5) {
          // if(test.getCount() == 5) {
            System.out.println("Current count is " + test.count);
            // System.out.println("Current count is " + test.getCount());
            break;
          }
        }
        System.out.println(Thread.currentThread().getName() + " end");
      }).start();
      TimeUnit.sleep(1);
      new Thread(() -> {
        for (int i = 0; i < 10; i++) {
          test.add(i);
          System.out.println(Thread.currentThread().getName() + " add element " + i);
        }
      }).start();
    }
  }
  ```

  - wait/notify 实现

  ```java
  public class Test {
    List<Object> list = new ArrayList<Object>();
    public void add(Object o) {
      list.add(o);
    }

    public int getCount() {
      return list.size();
    }

    public static void main(String[] args) {
      Test test = new Test();
      Object lock = new Object();

      new Thread(() -> {
        synchronized(lock) {
          System.out.println(Thread.currentThread().getName() + " start");
          try {
            lock.wait();
            System.out.println("Current count is " + test.getCount());
          } catch (InterruptedException e) {
          }
          System.out.println(Thread.currentThread().getName() + " end");
          lock.notify();
        }
      }).start();
      TimeUnit.sleep(1);
      new Thread(() -> {
        synchronized(lock) {
          for (int i = 0; i < 10; i++) {
            test.add(i);
            System.out.println(Thread.currentThread().getName() + " add element " + i);
            if (test.getCount() == 5) {
              lock.notify();
              try {
                lock.wait();
              } catch (InterruptedException e) {
              }
            }
          }
        }
      }).start();
    }
  }
  ```

  - 使用 CountDownLatch 实现

  ```java
  public class Test {
    List<Object> list = new ArrayList<Object>();

    public void add(Object o) {
      list.add(o);
    }

    public int getCount() {
      return list.size();
    }

    public static void main(String[] args) {
      Test test = new Test();
      CountDownLatch latch1 = new CountDownLatch(1);
      CountDownLatch latch2 = new CountDownLatch(1);

      new Thread(() -> {
        System.out.println(Thread.currentThread().getName() + " start");
        try {
          latch1.await();
        } catch (InterruptedException e) {
        }
        System.out.println("Current count is " + test.getCount());
        System.out.println(Thread.currentThread().getName() + " end");
        latch2.countDown();
      }).start();
      TimeUnit.sleep(1);
      new Thread(() -> {
        for (int i = 0; i < 10; i++) {
          test.add(i);
          System.out.println(Thread.currentThread().getName() + " add element " + i);
          if (test.getCount() == 5) {
            latch1.countDown();
            try {
              latch2.await();
            } catch (InterruptedException e) {
            }
          }
        }
      }).start();
    }
  }
  ```

  - 使用 LockSupport 实现

  ```java
  public class Test {
    List<Object> list = new ArrayList<Object>();
    static Thread t1 = null, t2 = null;
    public void add(Object o) {
      list.add(o);
    }

    public int getCount() {
      return list.size();
    }

    public static void main(String[] args) {
      Test test = new Test();

      t2 = new Thread(() -> {
        System.out.println(Thread.currentThread().getName() + " start");
        LockSupport.park();
        System.out.println("Current count is " + test.getCount());
        System.out.println(Thread.currentThread().getName() + " end");
        LockSupport.unpark(t1);
      });

      t1 = new Thread(() -> {
        for (int i = 0; i < 10; i++) {
          test.add(i);
          System.out.println(Thread.currentThread().getName() + " add element " + i);
          if (test.getCount() == 5) {
            LockSupport.unpark(t2);
            LockSupport.park();
          }
        }
      });
      t2.start();
      TimeUnit.sleep(1);
      t1.start();
    }
  }
  ```

- 例3
  写一个固定容量同步容器，拥有put和get方法，能够支持2个生产者线程以及10个消费者线程的阻塞调用

  - wait/notify 的实现

  ```java
  public class Test<T> {
    LinkedList<T> list = new LinkedList<T>();
    int MAX = 10;
    int count = 0;

    public synchronized void produce(T t) throws InterruptedException {
      while (MAX == list.size()) {
        wait();
      }
      list.add(t);
      count++;
      notifyAll();
    }

    public synchronized T consume() throws InterruptedException {
      while (0 == list.size()) {
        wait();
      }
      T t = list.removeFirst();
      count--;
      notifyAll();
      return t;
    }

    public static void main(String[] args) {
      Test<String> t = new Test<String>();
      for (int i = 0; i < 2; i++) {
        new Thread(() -> {
          while (true) {
            try {
              t.produce("Item" + Math.random());
            } catch (InterruptedException e) {
            }
            TimeUnit.sleep(1);
          }
        }, "Producer").start();
      }

      for (int i = 0; i < 10; i++) {
        new Thread(() -> {
          while (true) {
            try {
              System.out.println(t.consume() + " is consumed");
            } catch (InterruptedException e) {
            }
            TimeUnit.sleep(1);
          }
        }, "Consumer").start();
      }
    }
  }
  ```

  - ReentrantLock / Condition 的实现，一个Condition为一个等待队列

  ```java
  public class Test<T> {
    LinkedList<T> list = new LinkedList<T>();
    Lock lock = new ReentrantLock();
    Condition producer = lock.newCondition();
    Condition consumer = lock.newCondition();
    int MAX = 10;
    int count = 0;

    public void produce(T t) throws InterruptedException {
      lock.lock();
      while (MAX == list.size()) {
        producer.await();
      }
      list.add(t);
      count++;
      consumer.signalAll();
      lock.unlock();
    }

    public T consume() throws InterruptedException {
      lock.lock();
      while (0 == list.size()) {
        consumer.await();
      }
      T t = list.removeFirst();
      count--;
      producer.signalAll();
      lock.unlock();
      return t;

    }

    public static void main(String[] args) {
      Test<String> t = new Test<String>();
      for (int i = 0; i < 2; i++) {
        new Thread(() -> {
          while (true) {
            try {
              t.produce("Item" + Math.random());
            } catch (InterruptedException e) {
            }
            TimeUnit.sleep(1);
          }
        }, "Producer").start();
      }

      for (int i = 0; i < 10; i++) {
        new Thread(() -> {
          while (true) {
            try {
              System.out.println(t.consume() + " is consumed");
            } catch (InterruptedException e) {
            }
            TimeUnit.sleep(1);
          }
        }, "Consumer").start();
      }
    }
  }
  ```
