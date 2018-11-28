/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.test;

/**
 * Test Context Utility
 *
 * @author ryan131
 * @since May 3, 2016, 5:11:55 PM
 */
public final class TestContextUtil {
  private TestContextUtil() {}

  public static TestContext getTestContext() {
    return Context.context;
  }

  private interface Context {
    TestContextImpl context = new TestContextImpl();
  }

}
