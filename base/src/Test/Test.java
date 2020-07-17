public class Test {
  private int count = 0;
  public synchronized void m() {
    while (true) {
      count ++;
      System.out.println(count);
      if(count == 10) {
        System.out.println(Thread.currentThread().getName());
        int i = 1 / 0;
      }
      if(count == 100) {
        System.out.println(Thread.currentThread().getName());
        break;
      }
    }
  }

  public static void main(String[] args) {
    Test t1 = new Test();
    for (int i = 0; i < 2; i++) {
      new Thread(() -> {
        t1.m();
      }).start();
    }
  }
}