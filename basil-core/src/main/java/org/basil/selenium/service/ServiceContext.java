/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.service;

import java.util.Arrays;
import java.util.List;

import org.basil.selenium.base.BaseContext;
import org.basil.selenium.base.DriverFactory;
import org.basil.selenium.page.ElementLookup;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * ServiceContext - [Architectural experiment] - provides WebDriver, WebDriverWait,
 * JavascriptExecutor, and ElementLookup objects for service/utility classes.
 *
 * @author ryan131
 * @since Nov 4, 2015, 11:37:23 PM
 */
public abstract class ServiceContext extends BaseContext {

  protected static final WebDriver driver;
  protected static final WebDriverWait wait;
  protected static final JavascriptExecutor jsExecutor;
  protected static final ElementLookup lookup;

  static {
    driver = DriverFactory.getWebDriver();
    wait = DriverFactory.getWebDriverWait();
    jsExecutor = (JavascriptExecutor) driver;
    lookup = ElementLookup.create(getDriver(), getWait());
  }

  public static WebDriver getDriver() {
    return driver;
  }

  public static WebDriverWait getWait() {
    return wait;
  }

  public static JavascriptExecutor getJsExecutor() {
    return jsExecutor;
  }

  public static ElementLookup getLookup() {
    return lookup;
  }

  @Deprecated
  protected static void waitUntilAllElementsLoad(WebElement[] elements) {
    waitUntilAllElementsLoad(Arrays.asList(elements));
  }

  @Deprecated
  protected static void waitUntilAllElementsLoad(List<WebElement> list) {
    getWait().until(ExpectedConditions.visibilityOfAllElements(list));
  }

  // -------------------------------------------------------------------------------------------- //
  // The ServiceContext is rarely used instantiated, if however, is the case, the deprecation of  //
  // the instance members should indicate the user to use BaseContext.                            //
  // -------------------------------------------------------------------------------------------- //

  @Deprecated
  protected ServiceContext() {
    super();
  }

  @Deprecated
  protected ServiceContext(WebDriver driver) {
    super(driver);
  }

}
