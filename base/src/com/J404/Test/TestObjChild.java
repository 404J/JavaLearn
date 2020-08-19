package com.J404.Test;

/**
 * TestObj
 */
public class TestObjChild {

  @Override
  protected void finalize() throws Throwable {
    System.out.println("TestObjChild finalize!");
  }
}