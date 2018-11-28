/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.base;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;

/**
 * WebElementDecorator - Simplifies the process of writing WebElement decorator,
 * concrete class only need to provide the methods to be overridden.
 *
 * @author ryan131
 * @since Nov 1, 2014, 3:20:08 PM
 */
public abstract class WebElementDecorator implements WebElement, WrapsElement {

  protected WebElement wrappedElement;

  protected WebElementDecorator(WebElement wrappedElement) {
    this.wrappedElement = wrappedElement;
  }

  @Override
  public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
    return wrappedElement.getScreenshotAs(target);
  }

  @Override
  public WebElement getWrappedElement() {
    return wrappedElement;
  }

  @Override
  public void click() {
    wrappedElement.click();
  }

  @Override
  public void submit() {
    wrappedElement.submit();
  }

  @Override
  public void sendKeys(CharSequence... keysToSend) {
    wrappedElement.sendKeys(keysToSend);
  }

  @Override
  public void clear() {
    wrappedElement.clear();
  }

  @Override
  public String getTagName() {
    return wrappedElement.getTagName();
  }

  @Override
  public String getAttribute(String name) {
    return wrappedElement.getAttribute(name);
  }

  @Override
  public boolean isSelected() {
    return wrappedElement.isSelected();
  }

  @Override
  public boolean isEnabled() {
    return wrappedElement.isEnabled();
  }

  @Override
  public String getText() {
    return wrappedElement.getText();
  }

  @Override
  public List<WebElement> findElements(By by) {
    return wrappedElement.findElements(by);
  }

  @Override
  public WebElement findElement(By by) {
    return wrappedElement.findElement(by);
  }

  @Override
  public boolean isDisplayed() {
    return wrappedElement.isDisplayed();
  }

  @Override
  public Point getLocation() {
    return wrappedElement.getLocation();
  }

  @Override
  public Dimension getSize() {
    return wrappedElement.getSize();
  }

  @Override
  public Rectangle getRect() {
    return wrappedElement.getRect();
  }

  @Override
  public String getCssValue(String propertyName) {
    return wrappedElement.getCssValue(propertyName);
  }

}
