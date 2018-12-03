/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.base;

import java.net.URL;

import org.basil.selenium.remote.BasilWebDriver;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;

/**
 * Drivers
 *
 * @author ryan131
 * @since Oct 10, 2013, 11:16:55 AM
 */
class Driver {

  public static class Chrome {
    public static DesiredCapabilities capabilities;
    public static ChromeOptions options;

    static {
      capabilities = DesiredCapabilities.chrome();
      options = new ChromeOptions();
      capabilities.setCapability(ChromeOptions.CAPABILITY, options);
    }

    public static WebDriver driver = new ChromeDriver(capabilities);
  }

  public static class Edge {
    public static WebDriver driver = new EdgeDriver();
  }

  public static class InternetExplorer {
    private static DesiredCapabilities capabilities;

    static {
      capabilities = DesiredCapabilities.internetExplorer();
      capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
    }

    public static WebDriver driver = new InternetExplorerDriver(capabilities);
  }

  public static class Firefox {
    public static FirefoxProfile profile = new FirefoxProfile();

//    static String seleniumIdeExtention = "D:\\selenium-ide-2.9.0.xpi";
//    static {
//      profile = new FirefoxProfile();
//      try {
//        profile.addExtension(new java.io.File(seleniumIdeExtention));
//      } catch (java.io.IOException ioe) {
//        System.err.printf("Failed to load firefox extention: \"%s\".%n", seleniumIdeExtention);
//      }
//    }

    public static WebDriver driver = new FirefoxDriver(profile);
  }

  public static class Opera {
    public static WebDriver driver = new OperaDriver();
  }

  public static class Safari {
    public static WebDriver driver = new SafariDriver();
  }

}

/**
 * Not singleton
 */
class RemoteDriver {

  private static WebDriver driver;

  public static WebDriver get(URL remoteAddress, Capabilities capabilities) {
    if (driver != null && !(driver instanceof BasilWebDriver.Reusable)) {
      return driver;
    }
    driver = new BasilWebDriver(remoteAddress, capabilities);

    return driver;
  }

  public static WebDriver getSessionReusable(URL remoteAddress, Capabilities capabilities) {
    if (driver != null && !(driver instanceof BasilWebDriver.Reusable)) {
      return driver;
    }
    driver = new BasilWebDriver.Reusable(remoteAddress, capabilities);

    return driver;
  }

}