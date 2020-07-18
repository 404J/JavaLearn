import java.util.concurrent.atomic.AtomicInteger;

public class Test {
  private AtomicInteger count = new AtomicInteger(0);
  void m() {
    for (int i = 0; i < 10000; i++) {
      count.addAndGet(1);
    }
  }

  public static void main(String[] args) throws InterruptedException {
    Test t = new Test();
    for (int i = 0; i < 10; i++) {
      new Thread(t::m).start();
    }
    Thread.sleep(100);
    System.out.println(t.count);
  }
}