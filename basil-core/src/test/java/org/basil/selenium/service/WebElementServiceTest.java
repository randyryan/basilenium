/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.service;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

/**
 * WebElementServiceTest
 *
 * @author ryan131
 * @since Apr 15, 2015, 8:12:04 PM
 */
public class WebElementServiceTest {

  private WebElementService service;

  @Before
  public void setUp() {
    service = new WebElementServiceImpl();
  }

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

    service.validate(inputTextBox, ValidationRule.isInputTextBox());
  }

}
