import java.util.concurrent.locks.LockSupport;

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
