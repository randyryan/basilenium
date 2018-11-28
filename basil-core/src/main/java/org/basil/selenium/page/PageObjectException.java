/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.page;

import org.basil.selenium.BasilException;

/**
 * PageObjectException
 *
 * @author ryan131
 * @since Aug 12, 2015, 9:51:06 AM
 */
public class PageObjectException extends BasilException {

  private static final long serialVersionUID = 1306223813981671946L;

  public PageObjectException() {
    super();
  }

  public PageObjectException(String message) {
    super(message);
  }

  public PageObjectException(String message, Throwable cause) {
    super(message, cause);
  }

  public PageObjectException(Throwable cause) {
    super(cause);
  }

  /**
   * Initialization
   *
   * @author ryan131
   * @since Aug 12, 2016, 10:31:31 AM
   */
  public static class Initialization extends PageObjectException {

    private static final long serialVersionUID = -4977410372337730477L;

    public Initialization() {
      super();
    }

    public Initialization(String message) {
      super(message);
    }

    public Initialization(String message, Throwable cause) {
      super(message, cause);
    }

    public Initialization(Throwable cause) {
      super(cause);
    }

  }

}
