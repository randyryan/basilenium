/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.basil.selenium.base.DriverFactory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * WebElementServiceTest
 *
 * @author ryan131
 * @since Apr 15, 2015, 8:12:04 PM
 */
public class WebElementServiceTest {

  private static WebDriver driver;

  @BeforeClass
  public static void setUpClass() {
    driver = DriverFactory.getWebDriver();

    String wautUrl = null;
    try {
      wautUrl = Thread.currentThread().getContextClassLoader().getResource("WAUT.html")
          .toURI().toURL().toExternalForm();
    } catch (URISyntaxException use) {
    } catch (MalformedURLException mue) {
    }

    driver.get(wautUrl);
  }

  @Test
  public void test() {
    WebElement hierarchy_6 = driver.findElement(By.id("hierarchy_6"));
    WebElement hierarchy_5 = WebElementUtil.getParentElement(hierarchy_6);
    Assert.assertEquals("hierarchy_5", hierarchy_5.getAttribute("id"));

    WebElement hierarchy_4_2 = driver.findElement(By.id("hierarchy_4_2"));
    WebElement hierarchy_4_1 = hierarchy_4_2.findElement(By.xpath("//preceding-sibling::span"));
    Assert.assertEquals("hierarchy_4_1", hierarchy_4_1.getAttribute("id"));
  }

  @AfterClass
  public static void tearDownClass() {
    driver.quit();
  }

}
