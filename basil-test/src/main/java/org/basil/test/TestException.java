/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.test;

import org.basil.selenium.BasilException;

/**
 * Defines a general exception a Test can throw when it encounters difficulty.
 *
 * @author ryan131
 * @since Sep 19, 2015, 3:15:23 PM
 */
@SuppressWarnings("serial")
public class TestException extends BasilException {

  public TestException() {
    super();
  }

  public TestException(String message) {
    super(message);
  }

  public TestException(String message, Throwable rootCause) {
    super(message, rootCause);
  }

  public TestException(Throwable rootCause) {
    super(rootCause);
  }

  public Throwable getRootCause() {
    return getCause();
  }

}
