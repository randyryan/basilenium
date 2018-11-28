/**
 * Copyright (c) 2015-2016 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.basil.selenium.SeleniumContext;
import org.openqa.selenium.support.ui.Duration;
import org.openqa.selenium.support.ui.Sleeper;

/**
 * Selenium Test Utility
 *
 * @author ryan131
 * @since Apr 28, 2016, 11:07:56 AM
 */
public final class SeleniumTestUtil extends SeleniumContext {
  private SeleniumTestUtil() {}

  // Temporary methods

  public static String temp_getTimestamp() {
    return new Timestamp(new Date().getTime()).toString();
  }

  /**
   * This method is used only before the tests are refactored to use a appropriate logging class.
   */
  public static void temp_log(String classSimpleName, String message) {
    System.out.printf("[%s][INFO] %s (%s).%n", classSimpleName, message, temp_getTimestamp());
  }

  public static void temp_setWaitTimeout(long duration, TimeUnit unit) {
    wait.withTimeout(duration, unit);
  }

  public static void pauseTest(String classSimpleName, long interval, boolean countdown) {
    if (!countdown) {
      try {
        Thread.sleep(interval);
      } catch (InterruptedException ie) {}
    } else {
      long seconds = interval / 1000;
      for (long i = seconds; i > 0; i--) {
        temp_log(classSimpleName, new Long(i).toString());
        try {
          Thread.sleep(1000);
          Sleeper.SYSTEM_SLEEPER.sleep(new Duration(1000, TimeUnit.MILLISECONDS));
        } catch (InterruptedException ie) {}
      }
    }
  }

}
