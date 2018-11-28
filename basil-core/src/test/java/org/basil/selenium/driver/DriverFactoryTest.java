/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.driver;

import org.basil.Config;
import org.basil.selenium.base.DriverFactory;
import org.basil.selenium.base.DriverType;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Driver Factory Test
 *
 * @author ryan131
 * @since Apr 27, 2016, 3:56:01 PM
 */
public class DriverFactoryTest extends TestCase {

  public DriverFactoryTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(DriverFactoryTest.class);
  }

  public void testDriverFactory() {
    WebDriver driver = DriverFactory.getWebDriver();

    Assert.assertTrue(driver != null);

    WebDriverWait wait = DriverFactory.getWebDriverWait();

    Assert.assertTrue(wait != null);

    if (!Config.Value.WEB_DRIVER_TYPE.equals(DriverType.SESSION_REUSABLE_REMOTE)) {
      driver.close();
    }
  }

}
