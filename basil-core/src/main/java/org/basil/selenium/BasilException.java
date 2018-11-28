/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium;

/**
 * BasilException - Root type for all exceptions types in Basil.
 *
 * @author ryan131
 * @since Aug 12, 2014, 9:52:14 AM
 */
@SuppressWarnings("serial")
public abstract class BasilException extends RuntimeException {

  public BasilException() {
    super();
  }

  public BasilException(String message) {
    super(message);
  }

  public BasilException(String message, Throwable cause) {
    super(message, cause);
  }

  public BasilException(Throwable cause) {
    super(cause);
  }

  /**
   * BasilElement Exception
   *
   * @author ryan131
   * @since Jun 28, 2014, 10:33:51 AM
   */
  public static class Element extends BasilException {

    public Element() {
      super();
    }

    public Element(String message) {
      super(message);
    }

    public Element(String message, Throwable rootCause) {
      super(message, rootCause);
    }

    public Element(Throwable rootCause) {
      super(rootCause);
    }

    public static class Invalid extends Element {

      public Invalid() {
        super();
      }

      public Invalid(String message) {
        super(message);
      }

      public Invalid(String message, Throwable rootCause) {
        super(message, rootCause);
      }

      public Invalid(Throwable rootCause) {
        super(rootCause);
      }

    }

    public static class InvalidTagName extends Invalid {

      public InvalidTagName() {
        super();
      }

      public InvalidTagName(String message) {
        super(message);
      }

      public InvalidTagName(String message, Throwable rootCause) {
        super(message, rootCause);
      }

      public InvalidTagName(Throwable rootCause) {
        super(rootCause);
      }

    }

    /**
     * Invalid Input Type Exception
     *
     * @author ryan131
     * @since Nov 13, 2014, 2:07:55 PM
     */
    public static class InvalidInput extends Invalid {

      public InvalidInput() {
        super();
      }

      public InvalidInput(String message) {
        super(message);
      }

      public InvalidInput(String message, Throwable rootCause) {
        super(message, rootCause);
      }

      public InvalidInput(Throwable rootCause) {
        super(rootCause);
      }

    }

  }

}
