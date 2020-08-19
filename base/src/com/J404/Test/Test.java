package com.J404.Test;

import java.lang.ref.WeakReference;

public class Test {
    public static void main(String[] args) throws Exception {
//        ThreadLocal<Object> threadLocal = new ThreadLocal<>();
//        new Thread(() -> {
//            threadLocal.set(1);
//            threadLocal.set(2);
//            System.out.println(threadLocal.get());
//            threadLocal.remove();
//        }).start();
//        System.out.println(threadLocal.get());
        TestObj t = new TestObj(new TestObjChild[]{new TestObjChild()});
        t = null;
        System.gc();
        System.in.read();
    }
}
