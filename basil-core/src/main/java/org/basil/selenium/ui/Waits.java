/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.ui;

import org.basil.Config;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Waits
 *
 * @author ryan131
 * @since Jun 28, 2014, 11:24:45 AM
 */
public final class Waits {
  private Waits() {}

  public static WebDriverWait getWebDriverWait(WebDriver driver) {
    return new WebDriverWait(driver, Config.Value.WEB_DRIVER_WAIT_TIMEOUT);
  }

}
