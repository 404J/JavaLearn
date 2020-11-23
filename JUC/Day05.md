# Day05

## AQS 源码

- 源码
  lock(unfair lock)
    1. CAS state, 如果成功，获得锁成功
    2. acquire
    3. tryAcquire -> CAS state -> 判断是否为重入锁
    4. addWaiter -> CAS set tail -> 进队尾失败，则自旋 + CAS
    5. acquireQueued -> 如果自己是第二个节点，自旋 tryAcquire -> shouldParkAfterFailedAcquire 清除自己前面无效的节点，然后把前面节点的 waitStatus 设置为 SIGNAL -> parkAndCheckInterrupt park 当前线程
    6. selfInterrupt -> 如果 5 中期间线程被打断，则执行

  unlock(unfair lock)
    1. release -> tryRelease state 减 1 后如果 state 为 0 ，exclusiveOwnerThread 设置为 null
    2. unparkSuccessor 找到自己后面有效的节点线程，unpark 它

- VarHandle
  1. 普通属性可以进行原子性操作
  2. 比反射快

## java 中的引用

- 强引用
  普通的引用属于 new 属于强引用，只有变量指向空，GC 会进行回收

  ```java
  public class Test {
    public static void main(String[] args) throws IOException {
      TestObj testObj = new TestObj();
      testObj = null;
      System.gc();
      System.in.read();
    }
  }
  ```

- 软引用 SoftReference
  当堆内存不够用时候，软引用会被 GC 回收，用于缓存

  ```java
  public class Test {
    public static void main(String[] args) throws IOException {
      SoftReference<TestObj> softReference = new SoftReference<>(new TestObj());
      System.gc();
      System.out.println(softReference.get());
      System.in.read();
    }
  }
  ```

- 弱引用 WeakReference
  遇到 GC 就会被回收，例 WeakHashMap；用于容器里，变量 m 强引用指向 WeakReference，WeakReference 弱引用指向 M，于此同时，变量 X 强引用指向 M，若 X 为null, M 会被回收

  ```java
  public class Test {
    public static void main(String[] args) throws IOException {
      WeakReference<TestObj> weakReference = new WeakReference<>(new TestObj());
      System.gc();
      System.out.println(weakReference.get());
      System.in.read();
    }
  }
  ```

- 虚引用 PhantomReference
  遇到 GC 就会被回收，当虚引用被回收时，回向 Queue 中放入元素；用于不受 JVM 管理的堆外内存管理

## ThreadLocal

- 概念
  保证线程之间不受影响，当前线程 set 到 ThreadLocal 的属性，只有当前线程可以读取到

  ```java
  public class Test<T> {
    public static void main(String[] args) {
      ThreadLocal<Object> threadLocal = new ThreadLocal<>();
      new Thread(() -> {
        threadLocal.set(1);
        System.out.println(threadLocal.get());
        threadLocal.remove();
      }).start();
      System.out.println(threadLocal.get());
    }
  }
  ```

- 原理
  ThreadLocal 的 set 是放到当前线程中的 map 中，且 key 弱引用指向 ThreadLocal 。get 的时候从当前线程的 map 中 get，所以线程之间不可见

- ThreadLocal 避免内存泄漏，内部实现使用弱引用和 remove 方法的必要性

  ```java
  public class Test {
    public static void main(String[] args) throws IOException {
      ThreadLocal<TestObj> threadLocal = new MyThreadLocal<>();
      threadLocal.set(new TestObj());
      threadLocal.remove();
      threadLocal = null;
      System.gc();
      System.in.read();
    }
  }
  class MyThreadLocal<T> extends ThreadLocal<T> {
    @Override
    protected void finalize() throws Throwable {
      System.out.println("MyThreadLocal finalize!");
    }
  }
  ```
