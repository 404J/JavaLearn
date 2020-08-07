import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Test {
  public static void main(String[] args) throws Exception {
    ExecutorService eService = new ThreadPoolExecutor(
      2, 1, 1, TimeUnit.MILLISECONDS, null);
      Executors.newFixedThreadPool(nThreads)
  }
}