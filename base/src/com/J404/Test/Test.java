package com.J404.Test;

import java.util.ArrayList;
import java.util.List;

public class Test {
    static Lock lock = new Lock();
    public static void main(String[] args) throws Exception {
        System.out.println(lock);
        List list = new ArrayList();
        new Thread(() -> n(), "T1").start();
        new Thread(() -> m(), "T2").start();
        new Thread(() -> {
            TimeUnit.sleep(20);
            while (true) {
                list.add(new Object());
            }
        }, "T3").start();
    }

    static void m() {
        synchronized (lock) {}
    }

    static void n() {
        synchronized (lock) {
            System.out.println("n");
            while (true) {

            }
        }
    }

    static class Lock {

    }
}

