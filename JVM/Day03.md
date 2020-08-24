# Day03

## Java 内存模型

### 硬件层的并发优化: 数据一致性

- 总线锁 -> 保证每个线程的内存中变量的值和主内存一致

- MESI 协议

- 缓存行为不同线程 CPU 内存从 主内存读取数据的单位(8 字节)。如果两个变量位于同一个缓存行，不同线程间就需要维护其他未使用的变量，形成伪共享，会影响效率。缓存行对齐，可以提高效率

  ```java
  public class Test {
      static class T {
          public long p1, p2, p3, p4, p5, p6, p7; // padding
          public volatile long x;
      }
      static T[] arr = new T[2];
      static {
          // padding 使其不位于同一缓存行
          arr[0] = new T();
          arr[1] = new T();
      }
      public static void main(String[] args) throws Exception {
          long start = System.currentTimeMillis();
          CountDownLatch countDownLatch = new CountDownLatch(2);
          new Thread(() -> {
              for (int i = 0; i < 1000000000L; i++) {
                  arr[0].x = i;
              }
              countDownLatch.countDown();
          }).start();
          new Thread(() -> {
              for (int i = 0; i < 1000000000L; i++) {
                  arr[1].x = i;
              }
              countDownLatch.countDown();
          }).start();
          countDownLatch.await();
          long end = System.currentTimeMillis();
          System.out.println((end - start) / 1000.0);
      }
  }
  ```

- CPU 指令的乱序执行
  指令间如果没有依赖关系，指令不一定会顺序执行

  ```java
  public class Test {
      private static int x = 0, y = 0;
      private static int a = 0, b =0;
      public static void main(String[] args) throws Exception {
          int i = 0;
          for(;;) {
              i++;
              x = 0; y = 0;
              a = 0; b = 0;
              Thread one = new Thread(new Runnable() {
                  public void run() {
                      a = 1;
                      x = b;
                  }
              });

              Thread other = new Thread(new Runnable() {
                  public void run() {
                      b = 1;
                      y = a;
                  }
              });
              one.start();other.start();
              one.join();other.join();
              String result = "第" + i + "次 (" + x + "," + y + "）";
              if(x == 0 && y == 0) {
                  System.err.println(result);
                  break;
              }
          }
      }
  }
  ```

- CPU 合并写
  CPU 将同一个数据的多个计算结果合并成一次写到缓存的操作

- CPU 内存屏障
  使得指令通过一定顺序执行

### volatile 实现内存屏障

  1. 字节码层面：加了一个标记 ACC_VOLATILE

  2. JVM 层面：JVM 对于 volatile 修饰的变量的读写操作都加了内存屏障，不允许指令重排序

  3. OS 和硬件层面：省略

### synchronized 实现方法

  1. 字节码层面：monitorenter / monitorexit

  2. JVM 层面：使用 C 语言

  3. 硬件层面：lock 指令实现
