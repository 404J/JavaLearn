# Day02

## volatile

- 作用
    1. 保证线程可见性
    2. 防止指令重排序

- 例1 (保证线程可见性)
    一个线程中改变对象的属性，另一个线程中可以得知属性值的更新

    ```java
    public class Test {
      volatile boolean flag = true;
      void m() {
        System.out.println("m start");
        while (flag) {
        }
        System.out.println("m end");
      }

      public static void main(String[] args) throws InterruptedException {
        Test t = new Test();
        new Thread(t::m).start();
        Thread.sleep(100);
        t.flag = false;
      }
    }
    ```

- 例2 (防止指令重排序)
    DCL(Double Check Lock) 单例

    ```java
    public class Test {
      private static volatile Test instance;
      private Test() {
      }
      public static Test getInstance() {
        if(instance == null)  {
          synchronized(Test.class) {
            if(instance == null) {
              instance = new Test();
            }
          }
        }
        return instance;
      }

      public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
          new Thread(() -> {
            System.out.println(Test.getInstance().hashCode());
          }).start();
        }
      }
    }
    ```

- 例3
    volatile 不能保证原子性，不能够替代 synchronized

    ```java
    public class Test {
      private volatile int count = 0;
      void m() {
        for (int i = 0; i < 10000; i++) {
          count ++;
        }
      }

      public static void main(String[] args) throws InterruptedException {
        Test t = new Test();
        for (int i = 0; i < 10; i++) {
          new Thread(t::m).start();
        }
        Thread.sleep(100);
        System.out.println(t.count);
      }
    }
    ```

## synchronized 优化

- 锁的细化和粗化

- 锁的对象 synchronized(o) 不允许发生引用的改变 (final Object o)

## CAS(无锁优化，自旋， 乐观锁) 原子性操作

### AtomicInteger

- 例4
    使用 AtomicInteger

    ```java
    public class Test {
      private AtomicInteger count = new AtomicInteger(0);
      void m() {
        for (int i = 0; i < 10000; i++) {
          count.addAndGet(1);
        }
      }

      public static void main(String[] args) throws InterruptedException {
        Test t = new Test();
        for (int i = 0; i < 10; i++) {
          new Thread(t::m).start();
        }
        Thread.sleep(100);
        System.out.println(t.count);
      }
    }
    ```

- AtomicInteger 原理 (CAS)

- ABA 问题解决，加版本号
