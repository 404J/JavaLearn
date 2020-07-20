import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test {
  public static void main(String[] args) throws InterruptedException {
    int playerCount = 10;
    CountDownLatch begin = new CountDownLatch(1);
    CountDownLatch end = new CountDownLatch(playerCount);

    for (int i = 0; i < playerCount; i++) {
      int number = i;
      new Thread(() -> {
        try {
          begin.await();
          System.out.println("Player-" + number + " arrived!");
          end.countDown();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

      }).start();
    }
    System.out.println("Game begin!");
    begin.countDown();
    end.await();
    System.out.println("Game over!");
  }
}