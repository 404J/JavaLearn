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
  code
  ```

- 例5
  semaphore 实现

  ```java
  code
  ```