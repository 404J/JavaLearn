import java.util.concurrent.Semaphore;

public class Test {
  static Semaphore semaphore = new Semaphore(3);

  static void getPermit() {
    try {
      semaphore.acquire();
      Thread.sleep(100);
    } catch (InterruptedException e) {
    } finally {
      semaphore.release();
    }
    System.out.println("Competitor-" + Thread.currentThread().getName() + " get permit !");
  }
  public static void main(String[] args) {
    for (int i = 0; i < 10; i++) {
      new Thread(Test::getPermit).start();
    }
  }
}