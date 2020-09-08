# Day08

## 定位 JVM 运行中的问题

1. top 找出占用系统资源多的 Java 进程
2. top -Hp [进程ID] 查看进程中的线程情况, 或者 jps
3. jstack [进程ID] 查看进程中的相关线程情况

    ```shell
    "Thread-1" #13 prio=5 os_prio=31 tid=0x00007f81b81aa800 nid=0xa403 waiting for monitor entry [0x0000700001d96000]
    java.lang.Thread.State: BLOCKED (on object monitor)
        at com.J404.Test.Test.m(Test.java:16)
        - waiting to lock <0x000000076ac2bf50> (a com.J404.Test.Test)
        at com.J404.Test.Test.lambda$main$1(Test.java:11)
        at com.J404.Test.Test$$Lambda$2/1607521710.run(Unknown Source)
        at java.lang.Thread.run(Thread.java:748)

    "Attach Listener" #12 daemon prio=9 os_prio=31 tid=0x00007f81b907c000 nid=0xa603 waiting on condition [0x0000000000000000]
    java.lang.Thread.State: RUNNABLE

    "Thread-0" #11 prio=5 os_prio=31 tid=0x00007f81b700c800 nid=0xa803 runnable [0x0000700001b90000]
    java.lang.Thread.State: RUNNABLE
        at com.J404.Test.Test.m(Test.java:17)
        - locked <0x000000076ac2bf50> (a com.J404.Test.Test)
        at com.J404.Test.Test.lambda$main$0(Test.java:7)
        at com.J404.Test.Test$$Lambda$1/1452126962.run(Unknown Source)
        at java.lang.Thread.run(Thread.java:748)
    ```

4. jmap -histo [进程ID] ｜ head -n 查看进程中 Java 对象的实例个数
