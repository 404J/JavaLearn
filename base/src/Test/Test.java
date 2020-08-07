import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Test {
  public static void main(String[] args) throws Exception {
    ExecutorService eService = new ThreadPoolExecutor(2, 3, 1L, java.util.concurrent.TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(6),
        Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
    for (int i = 0; i < 10; i++) {
      int j = i;
      eService.execute(() -> {
        TimeUnit.sleep(1);
        System.out.println("ThreadName: " + Thread.currentThread().getName() + ", number: " +j);
      });
    }
  }
}