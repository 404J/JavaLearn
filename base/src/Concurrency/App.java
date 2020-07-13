package Concurrency;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class App {
    public static void main(String[] args) throws Exception {
        MyRunable myRunable = new MyRunable();
        Thread thread = new Thread(myRunable);
        // thread.start();
        Thread myThread = new MyThread();
        // myThread.start();
        System.out.println("Main run");
        System.out.println("--------------------------------------------------------------------");
        ExecutorService executorService = Executors.newCachedThreadPool();
        SynchronizedExample synchronizedExample = new SynchronizedExample();
        executorService.execute(() -> {
            synchronizedExample.printNum();
        });
        executorService.execute(() -> {
            synchronizedExample.printNum();
        });

        Thread.sleep(50);

        System.out.println("--------------------------------------------------------------------");
        SynchronizedExample synchronizedExample1 = new SynchronizedExample();
        executorService.execute(() -> {
            synchronizedExample.printNum();
        });
        executorService.execute(() -> {
            synchronizedExample1.printNum();
        });

        Thread.sleep(50);
        System.out.println("--------------------------------------------------------------------");

        executorService.execute(() -> {
            printNumStatic();
        });
        executorService.execute(() -> {
            printNumStatic();
        });

        Thread.sleep(50);
        System.out.println("--------------------------------------------------------------------");

        LockExample lockExample = new LockExample();
        LockExample lockExample1 = new LockExample();
        executorService.execute(() -> {
            lockExample.printNumLock();
        });
        executorService.execute(() -> {
            lockExample.printNumLock();
        });

        Thread.sleep(50);
        System.out.println("--------------------------------------------------------------------");

        Thread threadA = new ThreadA();
        Thread threadB = new ThreadB(threadA);
        threadA.start();
        threadB.start();

        Thread.sleep(50);
        System.out.println("--------------------------------------------------------------------");

        WaitNotifyExample waitNotifyExample = new WaitNotifyExample();
        executorService.execute(() -> {
            waitNotifyExample.second();
        });
        // Thread.sleep(1000);
        executorService.execute(() -> {
            waitNotifyExample.first();
        });

        Thread.sleep(50);
        System.out.println("--------------------------------------------------------------------");

        AwaitSignalExample awaitSignalExample = new AwaitSignalExample();
        executorService.execute(() -> {
            awaitSignalExample.second();
        });
        executorService.execute(() -> {
            awaitSignalExample.first();
        });

        Thread.sleep(50);
        System.out.println("--------------------------------------------------------------------");

        int playerCount = 10;
        CountDownLatch begin = new CountDownLatch(1);
        CountDownLatch end = new CountDownLatch(playerCount);

        for (int i = 0; i < playerCount; i++) {
            int number = i;
            executorService.execute(() -> {
                try {
                    begin.await();
                    System.out.println("Player-" + number + " arrived!");
                    end.countDown();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
        }
        System.out.println("Game begin!");
        begin.countDown();
        end.await();
        System.out.println("Game over!");

        Thread.sleep(50);
        System.out.println("--------------------------------------------------------------------");

        int calculatorCount = 5;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(calculatorCount, () -> {
            System.out.println("Statistical calculation results!");
        });
        for (int i = 0; i < calculatorCount; i++) {
            int number = i;
            executorService.execute(() -> {
                try {
                    System.out.println("Calculator-" + number + ": Calculating...");
                    Thread.sleep(10);
                    System.out.println("Calculator-" + number + ": Calculate over !");
                    cyclicBarrier.await();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
        }

        Thread.sleep(50);
        System.out.println("--------------------------------------------------------------------");

        int permitCount = 3;
        int competitorCount = 10;
        Semaphore semaphore = new Semaphore(permitCount);
        for (int i = 0; i < competitorCount; i++) {
            int number = i;
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    System.out.println("Competitor-" + number + " get permit !");
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            });
        }

        Thread.sleep(50);
        System.out.println("--------------------------------------------------------------------");

        FutureTask<Integer> futureTask = new FutureTask<Integer>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                // TODO Auto-generated method stub
                System.out.println("Massive calculation...");
                Thread.sleep(1000);
                return 100;
            }
        });
        executorService.execute(futureTask);
        System.out.println("Main job running...");
        System.out.println(futureTask.get());

        Thread.sleep(50);
        System.out.println("--------------------------------------------------------------------");

        for (int i = 0; i < 3; i++) {
            executorService.execute(new BlockingQueueExample.Producer());
        }
        
        for (int i = 0; i < 5; i++) {
            executorService.execute(new BlockingQueueExample.Consumer());
        }
        
        for (int i = 0; i < 2; i++) {
            executorService.execute(new BlockingQueueExample.Producer());
        }

        Thread.sleep(50);
        System.out.println("--------------------------------------------------------------------");
        
        int threadCount = 1000;
        ThreadUnsafeExample example = new ThreadUnsafeExample();
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        System.out.println("Initial value: " + example.getCount());
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                example.add();
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        System.out.println("Final value: " + example.getCount());

        Thread.sleep(50);
        System.out.println("--------------------------------------------------------------------");

        for (int i = 0; i < 3; i++) {
            executorService.execute(() -> {
                System.out.println(Singleton.getInstance());
            });
        }

        Thread.sleep(50);
        System.out.println("--------------------------------------------------------------------");

        VolatileExample volatileExample = new VolatileExample();
        executorService.execute(() -> {
            volatileExample.run();
        });
        executorService.execute(() -> {
            volatileExample.stop();
        });
        executorService.shutdown();
    }

    public synchronized static void printNumStatic() {
        for (int i = 0; i < 10; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
    }
}

class MyRunable implements Runnable {

    @Override
    public void run() {
        // TODO Auto-generated method stub
        System.out.println("run MyRunable");
    }

}

class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("run MyThread");
    }
}

class SynchronizedExample {

    // sync methods and sync code block all work on same instance

    // public synchronized void printNum() {
    public void printNum() {
        synchronized (this) {
            // synchronized (SynchronizedExample.class) { // work on same class
            for (int i = 0; i < 10; i++) {
                System.out.print(i + " ");
            }
            System.out.println();
        }
    }
}

class LockExample { // work on same instance
    private Lock lock = new ReentrantLock();

    public void printNumLock() {
        lock.lock();
        try {
            for (int i = 0; i < 10; i++) {
                System.out.print(i + " ");
            }
        } finally {
            System.out.println();
            lock.unlock();
        }
    }
}

class ThreadA extends Thread {
    @Override
    public void run() {
        // TODO Auto-generated method stub
        System.out.println("A run");
        System.out.println("A finish");
    }
}

class ThreadB extends Thread {

    private Thread a;

    public ThreadB(Thread a) {
        this.a = a;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        System.out.println("B run");
        try {
            a.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("B finish");
    }
}

class WaitNotifyExample {
    public synchronized void first() {
        System.out.println("first");
        notify(); // After other threads get the lock, wake up the waiting thread
    }

    public synchronized void second() {
        try {
            wait(); // Release lock
            // wait(500); // Release the lock, if it is not awakened in more than 500
            // milliseconds, it will automatically wake up and runnable
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("second");
    }
}

class AwaitSignalExample {
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void second() {
        lock.lock();
        try {
            condition.await();
            System.out.println("second");
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void first() {
        lock.lock();
        try {
            System.out.println("first");
            condition.signal();
        } finally {
            lock.unlock();
        }
    }
}
class BlockingQueueExample {
    private static BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<String>(5);

    public static class Producer extends Thread {
        @Override
        public void run() {
            try {
                blockingQueue.put("product");
                System.out.println("produce...");
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            try {
                blockingQueue.take();
                System.out.println("consume...");
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}

class ThreadUnsafeExample  {
    private int count = 0;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    
    // public synchronized void add() {
    public void add() {
        this.count ++;
    }
}

class Singleton {
    private static volatile Singleton singleton; // Prevent instruction reordering
    private Singleton() {};
    public static Singleton getInstance() {
        if (singleton == null) {
            synchronized(Singleton.class) {
                if(singleton == null) {
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }
}

class VolatileExample {
    private boolean flag = true;
    // private volatile boolean flag = true;
    
    public void run() {
        while(flag) {
            System.out.print(".");
        }
    }

    public void stop() {
        this.flag = false;
    }
}
