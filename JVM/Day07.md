# Day07

## JVM 垃圾回收器 PS + PO 调优

### 常见垃圾回收器组合参数设定：(1.8)

* -XX:+UseSerialGC = Serial New (DefNew) + Serial Old
  * 小型程序。默认情况下不会是这种选项，HotSpot会根据计算及配置和JDK版本自动选择收集器
* -XX:+UseConcMarkSweepGC = ParNew + CMS + Serial Old
* -XX:+UseParallelGC = Parallel Scavenge + Parallel Old (1.8默认) 【PS + SerialOld】
* -XX:+UseParallelOldGC = Parallel Scavenge + Parallel Old
* -XX:+UseG1GC = G1
* Linux中没找到默认GC的查看方法，而windows中会打印UseParallelGC
  * java +XX:+PrintCommandLineFlags -version
  * 通过GC的日志来分辨

> 查找 JVM 的常用参数 java -XX:+PrintFlagsFinal -version | grep searchName

### 参数设定

* 例子

```java
public class Test {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello GC");
        List list = new ArrayList();
        for (;;) {
            list.add(new byte[1024 * 1024]);
        }
    }
}
```

* -Xmn10M -Xms40M -Xmx60M -XX:+PrintCommandLineFlags -XX:+PrintGC
  新生代10M，最小堆内存40M，最大堆内存60M，打印JVM参数，打印GC日志

* 日志文件设置：
-Xloggc:/Users/a404/gc-%t.log -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=5 -XX:GCLogFileSize=1K -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause

### 调优前的基础概念

1. 吞吐量：用户代码时间 /（用户代码执行时间 + 垃圾回收时间）,选用 PS + PO
2. 响应时间：STW越短，响应时间越好, 选用 G1

### 简单场景

1. 系统内存升级之后，系统的相应速度反而变慢了，原因是啥，如何解决？
    内存升级，堆内存变大，但是使用 PS + PO 进行垃圾回收，垃圾回收频率变低，但是单次回收的时间变长。解决方法采用 PN + CMS 或者 G1
2. 系统 CPU 频繁飙高，如何分析？
    找出占用系统 CPU 最多的线程 top
    找出线程堆栈信息，方法调用信息 jstack
3. 系统内存频繁飙高，如何分析？
    导出堆内存 jmap
    分析堆内存 jhat
