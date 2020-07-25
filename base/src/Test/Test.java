import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test<T> {

  public static void main(String[] args) {
    Lock lock = new ReentrantLock();
    new Thread(() -> {
      lock.lock();
    }).start();
    TimeUnit.sleep(1);
    lock.lock();

    lock.unlock();
  }
}