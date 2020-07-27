/**
 * TestObj
 */
public class TestObj {

  @Override
  protected void finalize() throws Throwable {
    System.out.println("TestObj finalize!");
  }
}