/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.base;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Driver Utility
 *
 * @author ryan131
 * @since Apr 25, 2014, 5:35:55 PM
 */
public final class DriverUtil {
  private DriverUtil() {}

  // Methods

  public static BrowserType getBrowserType(String config) {
    BrowserType browserType = null;

    switch (config) {
    case "chrome":
      browserType = BrowserType.CHROME;
      break;
    case "edge":
      browserType = BrowserType.EDGE;
      break;
    case "firefox":
      browserType = BrowserType.FIREFOX;
      break;
    case "ie":
      browserType = BrowserType.INTERNET_EXPLORER;
      break;
    case "opera":
      browserType = BrowserType.OPERA;
      break;
    case "safari":
      browserType = BrowserType.SAFARI;
      break;
    }

    return browserType;
  }

  public static Dimension getBrowserSize(int[] config) {
    return new Dimension(config[0], config[1]);
  }

  public static Capabilities getCapabilities(BrowserType browserType) {
    Capabilities capabilities = null;

    switch (browserType) {
    case CHROME:
      capabilities = DesiredCapabilities.chrome();
      break;
    case EDGE:
      capabilities = DesiredCapabilities.edge();
      break;
    case FIREFOX:
      capabilities = DesiredCapabilities.firefox();
      break;
    case INTERNET_EXPLORER:
      capabilities = DesiredCapabilities.internetExplorer();
      break;
    case OPERA:
      capabilities = DesiredCapabilities.operaBlink();
      break;
    case SAFARI:
      capabilities = DesiredCapabilities.safari();
      break;
    default:
      capabilities = new DesiredCapabilities();
      break;
    }

    return capabilities;
  }

  public static DriverType getDriverType(String config) {
    DriverType driverType = null;

    switch (config) {
    case "standard":
      driverType = DriverType.STANDARD;
      break;
    case "remote":
      driverType = DriverType.REMOTE;
      break;
    case "session-reusable-remote":
      driverType = DriverType.SESSION_REUSABLE_REMOTE;
      break;
    }

    return driverType;
  }

}
