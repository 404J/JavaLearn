# Day07

## 面试题：两个线程同时运行，一个打印 A ~ Z，一个打印 1 ~ 26，需实现字母数字交替打印

- 例1
  LockSupport 实现

  ```java
  public class Test {
    static Thread t1, t2;
    public static void main(String[] args) throws InterruptedException {
      char[] alphabets = "ABCDEF".toCharArray();
      char[] numbers = "12345".toCharArray();
      t1 = new Thread(() -> {
        for (char c : alphabets) {
          System.out.println(c);
          LockSupport.unpark(t2);
          LockSupport.park();
        }
      });
      t2 = new Thread(() -> {
        for (char c : numbers) {
          LockSupport.park();
          System.out.println(c);
          LockSupport.unpark(t1);
        }
      });
      t1.start();
      t2.start();
    }
  }
  ```

- 例2
  synchronized, wait, notify 实现

  ```java
  code
  ```