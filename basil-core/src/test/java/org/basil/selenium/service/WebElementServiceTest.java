/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

import org.basil.selenium.base.DriverFactory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
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

//  @Test
//  public void test() {
//    WebElement hierarchy_6 = driver.findElement(By.id("hierarchy_6"));
//    WebElement hierarchy_5 = WebElementUtil.getParentElement(hierarchy_6);
//    Assert.assertEquals("hierarchy_5", hierarchy_5.getAttribute("id"));
//
//    WebElement hierarchy_4_2 = driver.findElement(By.id("hierarchy_4_2"));
//    WebElement hierarchy_4_1 = hierarchy_4_2.findElement(By.xpath("//preceding-sibling::span"));
//    Assert.assertEquals("hierarchy_4_1", hierarchy_4_1.getAttribute("id"));
//  }

  @Test
  public void validationTest() {
    WebElement inputTextBox = new WebElement() {

      @Override
      public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return null;
      }

      @Override
      public void click() {
      }

      @Override
      public void submit() {
      }

      @Override
      public void sendKeys(CharSequence... keysToSend) {
      }

      @Override
      public void clear() {
      }

      @Override
      public String getTagName() {
        return "input";
      }

      @Override
      public String getAttribute(String name) {
        if (!name.equals("type")) {
          return null;
        }
        return "text";
      }

      @Override
      public boolean isSelected() {
        return false;
      }

      @Override
      public boolean isEnabled() {
        return false;
      }

      @Override
      public String getText() {
        return null;
      }

      @Override
      public List<WebElement> findElements(By by) {
        return null;
      }

      @Override
      public WebElement findElement(By by) {
        return null;
      }

      @Override
      public boolean isDisplayed() {
        return false;
      }

      @Override
      public Point getLocation() {
        return null;
      }

      @Override
      public Dimension getSize() {
        return null;
      }

      @Override
      public Rectangle getRect() {
        return null;
      }

      @Override
      public String getCssValue(String propertyName) {
        return null;
      }

    };

    System.out.println(WebElementUtil.validate(inputTextBox, ValidationRule.isInputTextBox()));
  }

  @AfterClass
  public static void tearDownClass() {
    driver.quit();
  }

}
