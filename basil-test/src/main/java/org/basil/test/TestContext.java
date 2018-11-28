/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.test;

/**
 * Test Context 
 *
 * @author ryan131
 * @since Sep 26, 2015, 2:14:38 PM
 */
public interface TestContext {

  void log(String message);

  void log(String message, Throwable cause);

}
