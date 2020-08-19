package com.J404.Test;

/**
 * TestObj
 */
public class TestObj {

  private TestObjChild[] testObjChilds;

  public TestObj(TestObjChild[] testObjChilds) {
    this.testObjChilds = testObjChilds;
  }

  @Override
  protected void finalize() throws Throwable {
    System.out.println("TestObj finalize!");
  }
}