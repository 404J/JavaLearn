public class Test {
  static Thread t1, t2;
  static volatile boolean flag = true;
  public static void main(String[] args) throws InterruptedException {
    char[] alphabets = "ABCDEF".toCharArray();
    char[] numbers = "123456".toCharArray();
    
    t1 = new Thread(() -> {
      for (char c : alphabets) {
        while(flag) {}
        System.out.println(c);
        flag = true;
      }
    });
    t2 = new Thread(() -> {
      for (char c : numbers) {
        while(!flag) {}
        System.out.println(c);
        flag = false;
      }
    });
    t1.start();
    t2.start();
  }
}
