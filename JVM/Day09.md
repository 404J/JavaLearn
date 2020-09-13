# Day09

## arthas 的使用

### 运行

java -jar [jar file]

### 关联 java 进程

### 运行检测命令

1. jvm 查看 Jvm 运行状态

2. thread 查看线程状态

3. heapdump dump 堆文件，分析堆中的 java 对象，使用 jhat 分析

4. jad 反编译类，作用：1）观察动态代理生成的类 2）查看版本是否是最新的版本

5. redefine 热替换，不用停掉服务

## JVM 调优案例

- lambda表达式导致方法区溢出问题(MethodArea / Perm Metaspace)
-XX:MaxMetaspaceSize=9M -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError
java.lang.OutOfMemoryError: Compressed class space

```java
public class Test {
    public static void main(String[] args) throws Exception {
        while (true) {
            I i = C::m;
        }
    }

    static class C {
        static void m () {

        }
    }

    static interface I {
        void n ();
    }

}
```

- tomcat http-header-size 问题，过大会导致并发量过高时候导致 OOM，过小会导致 Request header is too large

- 栈溢出问题 -Xss设定太小 -Xss2M

```java
public class Test {
    public static void main(String[] args) throws Exception {
        m();
    }
    static void m() {
        m();
    }
}
```

