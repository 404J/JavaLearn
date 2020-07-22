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
    public class Test {
        public static void main(String[] args) throws InterruptedException {
            CyclicBarrier cyclicBarrier = new CyclicBarrier(10, () -> {
                System.out.println("人满，发车！");
            });
            for (int i = 0; i < 100; i++) {
                new Thread(() -> {
                    System.out.println(Thread.currentThread().getName());
                    try {
                        cyclicBarrier.await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }).start();;
            }
        }
    }
    ```

## Phaser

- 例6
    篮球比赛的淘汰赛阶段

    ```java
    public class Test {
        static Phaser phaser = new GamePhaser();

        static class GamePhaser extends Phaser {
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                switch (phase) {
                    case 0:
                        System.out.println("semifinals " + registeredParties);
                        System.out.println();
                        return false;
                    case 1:
                        System.out.println("finals " + registeredParties);
                        System.out.println();
                        return true;
                    default:
                        return true;
                }
            }
        }

        static class Gamer extends Thread {
            String gamerName;

            public Gamer(String gamerName) {
                this.gamerName = gamerName;
            }
            void playSemifinals() {
                System.out.println(gamerName + " is playing in semifinals");
                phaser.arriveAndAwaitAdvance();
            }

            void playFinals() {
                if(gamerName.equals("CHINA") || gamerName.equals("JAPAN")) {
                    System.out.println(gamerName + " is playing in finals");
                    phaser.arriveAndAwaitAdvance();
                } else {
                    phaser.arriveAndDeregister();
                }
                if (gamerName.equals("CHINA")) {
                    System.out.println("The winner is " + gamerName);
                }
            }

            @Override
            public void run() {
                playSemifinals();
                playFinals();
            }
        }

        public static void main(String[] args) throws InterruptedException {
            phaser.bulkRegister(4);
            String[] gamers = {"USA", "CHINA", "UK", "JAPAN"};
            for (String gamer : gamers) {
                new Gamer(gamer).start();
            }
        }
    }
    ```

## ReadWriteLock (共享锁，排他锁)

- 例7
    读锁之间为共享锁，写锁和写锁/读锁和写锁之间为排他锁

    ```java
    public class Test {
        static ReadWriteLock lock = new ReentrantReadWriteLock();
        static Lock writeLock = lock.writeLock();
        static Lock readLock = lock.readLock();

        static void write() {
            try {
                writeLock.lock();
                Thread.sleep(500);
                System.out.println("Thread " + Thread.currentThread().getName() + " is writing");
            } catch (InterruptedException e) {
            } finally {
                writeLock.unlock();
            }
        }

        static void read() {
            try {
                readLock.lock();
                Thread.sleep(500);
                System.out.println("Thread " + Thread.currentThread().getName() + " is reading");
            } catch (InterruptedException e) {
            } finally {
                readLock.unlock();
            }
        }

        public static void main(String[] args) {
            for (int i = 0; i < 5; i++) {
                new Thread(Test::write).start();;
                new Thread(Test::read).start();;
            }
        }
    }
    ```

## Semaphore

- 例7
    限流

    ```java
    public class Test {
        static Semaphore semaphore = new Semaphore(3);

        static void getPermit() {
            try {
                semaphore.acquire();
                Thread.sleep(100);
            } catch (InterruptedException e) {
            } finally {
                semaphore.release();
            }
            System.out.println("Competitor-" + Thread.currentThread().getName() + " get permit !");
        }
        public static void main(String[] args) {
            for (int i = 0; i < 10; i++) {
                new Thread(Test::getPermit).start();
            }
        }
    }
    ```
