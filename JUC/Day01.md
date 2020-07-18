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

- Terminated

## synchronized 关键字

- synchronized(new Object())，可以锁任何对象

- synchronized(this) 代码块和 synchronized 方法等价

- static 方法中 synchronized 锁的是 MyClass.class 对象，不是 this

- 例1
    对同一个数值进行操作，得到与期望结果不同

    ```java
    public class Test {
        private int number = 0;

        public int getNumber() {
            return this.number;
        }

        public void addOne() {
            this.number++;
        }

        public static void main(String[] args) throws InterruptedException {
            int threadCount = 1000;
            ExecutorService executorService = Executors.newCachedThreadPool();
            CountDownLatch countDownLatch = new CountDownLatch(threadCount);
            Test t1 = new Test();
            for (int i = 0; i < threadCount; i ++) {
                executorService.execute(() -> {
                    t1.addOne();
                    countDownLatch.countDown();
                });
            }
            countDownLatch.await();
            System.out.println(t1.getNumber());
            executorService.shutdown();
        }
    }
    ```

- 例2
    一个同步方法执行的时候，另一个非同步方法可以执行。脏读例子实现。

    ```java
    public class Test {
        private int number = 0;

        public int getNumber() {
            return this.number;
        }

        public synchronized void addOne() {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.number ++;
        }
        public static void main(String[] args) {
            Test t1 = new Test();
            new Thread(() -> {
                t1.addOne();
            }).start();
            new Thread(() -> {
                System.out.println(t1.getNumber());
            }).start();
        }
    }
    ```

- 例3
    可重入锁。即同步方法F1可调用同步方法F2

    ```java
    public class Test {

        public synchronized void m1() {
            System.out.println("this is method m1");
            this.m2();
        }

        public synchronized void m2() {
            System.out.println("this is method m2");
        }
        public static void main(String[] args) {
            Test t1 = new Test();
            t1.m1();
        }
    }
    ```

- 例4
    锁与异常，同步方法中产生异常将会释放锁，其他等待获得锁的线程将会“乱入”

    ```java
    public class Test {
        private int count = 0;
        public synchronized void m() {
            while (true) {
                count ++;
                System.out.println(count);
                if(count == 10) {
                    System.out.println(Thread.currentThread().getName());
                    int i = 1 / 0;
                }
                if(count == 100) {
                    System.out.println(Thread.currentThread().getName());
                    break;
                }
            }
        }

        public static void main(String[] args) {
            Test t1 = new Test();
            for (int i = 0; i < 2; i++) {
                new Thread(() -> {
                    t1.m();
                }).start();
            }
        }
    }
    ```

- JVM中 synchronized 锁的实现优化
    1. 当只有一个线程时候，在 Object 的 markword 上记录线程 ID (偏向锁)
    2. 如果有多个线程争锁，升级为 自旋锁，自旋锁会占用 CPU。
    3. 自旋10次后，升级为 重量锁 (向操作系统申请锁)
