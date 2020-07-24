import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

public class Test {
  List<Object> list = new ArrayList<Object>();
  static Thread t1 = null, t2 = null;
  public void add(Object o) {
    list.add(o);
  }

  public int getCount() {
    return list.size();
  }

  public static void main(String[] args) {
    Test test = new Test();

    t2 = new Thread(() -> {
      System.out.println(Thread.currentThread().getName() + " start");
      LockSupport.park();
      System.out.println("Current count is " + test.getCount());
      System.out.println(Thread.currentThread().getName() + " end");
      LockSupport.unpark(t1);
    });

    t1 = new Thread(() -> {
      for (int i = 0; i < 10; i++) {
        test.add(i);
        System.out.println(Thread.currentThread().getName() + " add element " + i);
        if (test.getCount() == 5) {
          LockSupport.unpark(t2);
          LockSupport.park();
        }
      }
    });
    
    t2.start();
    TimeUnit.sleep(1);
    t1.start();
  }
}