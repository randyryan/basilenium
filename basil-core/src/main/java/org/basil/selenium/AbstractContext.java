/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium;

import java.util.List;

import org.basil.selenium.page.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * AbstractContext - Provide syntactic sugar and simplifies the structure for BasilContext. Be care
 *                   when extending this class, as this was written for BasilContext specifically.
 *                   Methods with protected modifier have to be overridden in BasilContext to modify
 *                   their returned type.
 *
 * @author ryan131
 * @since Nov 20, 2015, 5:29:22 PM
 */
public abstract class AbstractContext implements SearchContext {

  // Locator

  public abstract LocatorManager locator();

  protected AbstractContext setLocator(By locator) {
    locator().set(locator);
    return this;
  }

  public Basil getLocator() {
    return locator().get();
  }

  public Basil getConfidentLocator() {
    return locator().getConfident();
  }

  public Basil getGeneratedLocator() {
    return locator().getGenerated();
  }

  // Context

  public abstract ContextManager context();

  protected AbstractContext setContext(SearchContext context) {
    context().set(context);
    return this;
  }

  public SearchContext getContext() {
    return context().get();
  }

  protected AbstractContext setParent(SearchContext parent) {
    context().setParent(parent);
    return this;
  }

  public BasilContext getParent() {
    return context().getParent();
  }

  public WebDriver driver() {
    return context().getDriver();
  }

  public boolean isWebDriver() {
    return context().isWebDriver();
  }

  @Override
  public List<WebElement> findElements(By by) {
    return resolve().findElements(by);
  }

  @Override
  public BasilElement findElement(By by) {
    return resolve().findElement(by);
  }

  // find by XPath
  //
  // Shorthands to findElement(s)(By.xpath(xpathExpression)), keep things simple and readable.

  public List<WebElement> findsByXPath(String xpathExpression) {
    return findElements(By.xpath(xpathExpression));
  }

  public BasilElement findByXPath(String xpathExpression) {
    return findElement(By.xpath(xpathExpression));
  }

  public BasilElement firstVisibleByXPath(String xpathExpression) {
    for (WebElement element : findsByXPath(xpathExpression)) {
      if (element.isDisplayed()) {
        return BasilElement.create(element);
      }
    }
    throw new NoSuchElementException("No visible elements are found with: \"" +
        getLocator().append(xpathExpression) + "\".");
  }

  // Resolve

  public abstract ResolutionManager resolve();

  // Miscellaneous

  /**
   * @see {@link BasilContext.Resolve#isResolved()}
   */
  public boolean isDisplayed() {
    if (context().isWebElement()) {
      return context().toWebElement().isDisplayed();
    }
    if (context().isBasilElement()) {
      return context().toBasilElement().isDisplayed();
    }
    if (context().isPageObject()) {
      return context().toBasilElement().isDisplayed();
    }
    throw new UnsupportedOperationException("isDisplay() is unsupported for the current " +
        "context type: " + context().get());
  }

  // Object

  @Override
  public int hashCode() {
    return context().get().hashCode();
  }

  @Override
  public boolean equals(Object object) {
    if (object == this) {
      return true;
    }
    if (object instanceof BasilContext) {
      return context().get().equals(((BasilContext) object).context().get());
    }
    if (object instanceof SearchContext) {
      return context().get().equals(object);
    }
    return false;
  }

  @Override
  public String toString() {
    return context().get().toString();
  }

  public String getClassName() {
    return getClass().getSimpleName();
  }

  public interface LocatorManager {

    void set(By by);

    boolean has();

    Basil get();

    boolean hasConfident();

    Basil getConfident();

    boolean hasGenerated();

    Basil getGenerated();

  }

  public interface ContextManager {

    void set(SearchContext context);

    SearchContext get();

    void setParent(SearchContext parent);

    BasilContext getParent();

    void setDriver(WebDriver driver);

    WebDriver getDriver();

    // Type

    boolean isWebDriver();

    WebDriver toWebDriver();

    boolean isWebElement();

    WebElement toWebElement();

    boolean isBasilElement();

    BasilElement toBasilElement();

    boolean isPageObject();

    PageObject toPageObject();

  }

  public interface ResolutionManager {

    void resolve();

    boolean isResolved();

    List<WebElement> findElements(By by);

    BasilElement findElement(By by);

  }

}
