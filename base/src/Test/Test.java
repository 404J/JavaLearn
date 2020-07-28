import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Test {
  public static void main(String[] args) throws InterruptedException {
    BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>();
    blockingQueue.put("1");
  }
}
