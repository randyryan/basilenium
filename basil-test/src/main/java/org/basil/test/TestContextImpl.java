/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.test;

/**
 * TestContext (Implementation)
 *
 * @author ryan131
 * @since Apr 28, 2016, 4:49:41 PM
 */
public class TestContextImpl implements TestContext {

  @Override
  public synchronized void log(String message) {
    System.err.println(message);
  }

  @Override
  public synchronized void log(String message, Throwable cause) {
    System.err.println(message);
    cause.printStackTrace(System.err);
  }

}
