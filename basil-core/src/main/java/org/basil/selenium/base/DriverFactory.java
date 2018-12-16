/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.base;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.basil.Config;
import org.basil.selenium.ui.Waits;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Driver Factory Utility for WebDriver and WebDriverWait objects.
 *
 * @author ryan131
 * @since Sep 27, 2013, 9:51:06 AM
 */
public final class DriverFactory {
  private DriverFactory() {}

  private static final BrowserType BROWSER_TYPE = Config.BROWSER_TYPE;

  private static Dimension BROWSER_DIMENSION = Config.BROWSER_WINDOW_DIMENSION;

  private static boolean BROWSER_MAXIMIZED = Config.BROWSER_WINDOW_MAXIMIZED;

  private static final Capabilities CAPABILITIES = DriverUtil.getCapabilities(BROWSER_TYPE);

  private static final DriverType DRIVER_TYPE = Config.WEB_DRIVER_TYPE;

  private static final URL REMOTE_ADDRESS = Config.WEB_DRIVER_REMOTE_URL;

  static {
    System.setProperty("webdriver.chrome.driver", Config.WEB_DRIVER_EXECUTABLE_CHROME);
    System.setProperty("webdriver.ie.driver", Config.WEB_DRIVER_EXECUTABLE_IE);
  }

  private static final long WAIT_POLL_INTERVAL = Config.WEB_DRIVER_WAIT_POLL;

  private static WebDriver driverOfChoice;

  public static WebDriver getWebDriver() {
    return getWebDriver(BROWSER_TYPE, DRIVER_TYPE);
  }

  public static WebDriver getWebDriver(BrowserType browserType) {
    return getWebDriver(browserType, DRIVER_TYPE);
  }

  public static WebDriver getWebDriver(BrowserType browserType, DriverType driverType) {
    if (driverOfChoice != null) {
      return driverOfChoice;
    }

    // Local

    if (DRIVER_TYPE == DriverType.STANDARD) {
      switch (browserType) {
      case CHROME:
        driverOfChoice = Driver.Chrome.driver;
        break;
      case EDGE:
        driverOfChoice = Driver.Edge.driver;
        break;
      case FIREFOX:
        driverOfChoice = Driver.Firefox.driver;
        break;
      case INTERNET_EXPLORER:
        driverOfChoice = Driver.InternetExplorer.driver;
        break;
      case OPERA:
        driverOfChoice = Driver.Opera.driver;
        break;
      case SAFARI:
        driverOfChoice = Driver.Safari.driver;
        break;
      }
    }

    // Remote

    if (DRIVER_TYPE == DriverType.REMOTE) {
      driverOfChoice = RemoteDriver.get(REMOTE_ADDRESS, CAPABILITIES);
    }
    if (DRIVER_TYPE == DriverType.SESSION_REUSABLE_REMOTE) {
      driverOfChoice = RemoteDriver.getSessionReusable(REMOTE_ADDRESS, CAPABILITIES);
    }

    // Set size/maximize

    driverOfChoice.manage().window().setSize(BROWSER_DIMENSION);
    if (BROWSER_MAXIMIZED) {
      driverOfChoice.manage().window().maximize();
    }

    return driverOfChoice;
  }

  // Wait

  private static class WebDriverWaitHolder {
    public static WebDriverWait instance = Waits.getWebDriverWait(driverOfChoice);
  }

  public static WebDriverWait getWebDriverWait() {
    WebDriverWaitHolder.instance.pollingEvery(WAIT_POLL_INTERVAL, TimeUnit.MILLISECONDS);
    return WebDriverWaitHolder.instance;
  }

  public static long totalWaitedTime = 0;

}
