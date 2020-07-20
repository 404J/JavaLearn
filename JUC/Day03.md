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
    public class Test {
        int count = 0;
        CountDownLatch countDownLatch = new CountDownLatch(10);
        Lock lock = new ReentrantLock();

        void m() {
            for (int i = 0; i < 1000; i++) {
                try {
                    lock.lock();
                    count++;
                } finally {
                    lock.unlock();
                }
            }
            countDownLatch.countDown();
        }

        public static void main(String[] args) throws InterruptedException {
            Test t = new Test();
            for (int i = 0; i < 10; i++) {
                new Thread(t::m).start();
            }
            t.countDownLatch.await();
            System.out.println(t.count);
        }
    }
    ```

- 例3
    可以设置锁为公平锁

    ```java
    public class Test {
        Lock lock = new ReentrantLock(true);

        void m() {
            while(true) {
                try {
                    lock.lock();
                    System.out.println(Thread.currentThread().getName());
                } finally {
                    lock.unlock();
                }
            }
        }

        public static void main(String[] args) throws InterruptedException {
            Test t = new Test();
            new Thread(t::m).start();
            new Thread(t::m).start();
        }
    }
    ```

## CountDownLatch
  
- 例4
    监控多个线程，并等待多个线程结束执行相关操作，赛跑比赛

    ```java
    public class Test {
        public static void main(String[] args) throws InterruptedException {
            int playerCount = 10;
            CountDownLatch begin = new CountDownLatch(1);
            CountDownLatch end = new CountDownLatch(playerCount);

            for (int i = 0; i < playerCount; i++) {
                int number = i;
                new Thread(() -> {
                    try {
                        begin.await();
                        System.out.println("Player-" + number + " arrived!");
                        end.countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
            System.out.println("Game begin!");
            begin.countDown();
            end.await();
            System.out.println("Game over!");
        }
    }
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
