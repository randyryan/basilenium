/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.base;

import com.google.common.base.Preconditions;
import org.basil.selenium.page.ElementLookup;
import org.basil.selenium.ui.SearchContextWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * BaseContext - [Architectural experiment] - provides WebDriver, WebDriverWait, JavascriptExecutor,
 * and ElementLookup objects for non static classes (for example: PageObject).
 *
 * @author ryan131
 * @since Jul 1, 2014, 5:27:41 PM
 */
public abstract class BaseContext {

  protected WebDriver driver;
  protected WebDriverWait wait;
  protected SearchContext context;
  protected SearchContextWait contextWait;
  protected JavascriptExecutor jsExecutor;
  protected ElementLookup lookup;

  protected BaseContext() {
    driver = DriverFactory.getWebDriver();
    wait = DriverFactory.getWebDriverWait();
    jsExecutor = (JavascriptExecutor) driver;
    lookup = ElementLookup.create(driver, wait);
  }

  protected BaseContext(WebDriver driver) {
    this.driver = Preconditions.checkNotNull(driver);
    wait = new WebDriverWait(driver, 15);
    jsExecutor = (JavascriptExecutor) driver;
    lookup = ElementLookup.create(driver, wait);
  }

  protected BaseContext(SearchContext context) {
    this.context = context;
    contextWait = new SearchContextWait(context);
  }

}
