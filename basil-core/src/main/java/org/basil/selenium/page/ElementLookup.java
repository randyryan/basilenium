/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.page;

import java.util.List;

import org.basil.selenium.service.WebElementUtil;
import org.basil.selenium.ui.ExtendedConditions;
import org.basil.selenium.ui.SearchContextWait;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;

/**
 * Element Lookup is an utility for locating web elements
 *
 * @author ryan131
 * @since Oct 22, 2014, 7:10:13 PM
 */
public class ElementLookup extends BaseLookup {

  public static ElementLookup create(SearchContext searchContext) {
    return new ElementLookup(searchContext, new SearchContextWait(searchContext));
  }

  public static ElementLookup create(SearchContext searchContext, long timeout) {
    return new ElementLookup(searchContext, new SearchContextWait(searchContext, timeout));
  }

  public static ElementLookup create(
      SearchContext searchContext, FluentWait<? extends SearchContext> wait) {
    return new ElementLookup(searchContext, new SearchContextWait(searchContext, wait));
  }

  private FluentWait<? extends SearchContext> wait;
  private CachedLookup cachedLookup;
  private SearchContext searchContext;

  private ElementLookup(SearchContext searchContext) {
    this(searchContext, new SearchContextWait(searchContext));
  }

  private ElementLookup(SearchContext searchContext, FluentWait<? extends SearchContext> wait) {
    super(searchContext);
    this.wait = new SearchContextWait(searchContext, wait);
    this.cachedLookup = new CachedLookup(searchContext);
    this.searchContext = searchContext;
  }

  // Explicit wait methods

  public WebElement getVisibleElement(By by) {
    return wait.until(ExtendedConditions.visibilityOfElementLocated(by));
  }

  public WebElement getFirstVisibleElement(By by) {
    return wait.until(ExtendedConditions.visibilityOfFirstVisibleElementLocated(by));
  }

  // Look up by text

  @Override
  public WebElement byText(List<WebElement> elements, String text) {
    return super.byText(elements, text); // No need of caching
  }

  @Override
  public WebElement byPartialText(List<WebElement> elements, String partialText) {
    return super.byPartialText(elements, partialText); // No need of caching
  }

  @Override
  public WebElement byText(By by, String text) {
    return cachedLookup.byText(by, text);
  }

  @Override
  public WebElement byPartialText(By by, String partialText) {
    return cachedLookup.byPartialText(by, partialText);
  }

  @Override
  public WebElement byTagAndText(String tagName, String text) {
    return cachedLookup.byTagAndText(tagName, text);
  }

  @Override
  public WebElement byClassAndText(String clazz, String text) {
    return cachedLookup.byClassAndText(clazz, text);
  }

  @Override
  public WebElement byCssSelectorAndText(String selector, String text) {
    return cachedLookup.byCssSelectorAndText(selector, text);
  }

  // Look up by element attribute

  @Override
  public WebElement byAttribute(List<WebElement> elements, String attribute, String value) {
    return super.byAttribute(elements, attribute, value); // No need of caching
  }

  @Override
  public WebElement byAttribute(By by, String attribute, String value) {
    return cachedLookup.byAttribute(by, attribute, value);
  }

  // Lookup by label elements

  @Override
  public WebElement label(String text) {
    return WebElementUtil.findElementByText(text, searchContext);
  }

  @Override
  public WebElement partialLabel(String partialText) {
    return cachedLookup.partialLabel(partialText);
  }

  @Override
  public WebElement byLabel(String text) {
    return WebElementUtil.findElementByLabel(text, searchContext);
  }

  @Override
  public WebElement byPartialLabel(String partialText) {
    // XXX I don't want to add a findElementByPartialLabel in the WebElementService
    String xpath = null;
    if (partialText.contains("'")) {
      xpath = "//label[contains(text(), \"" + partialText + "\")]";
    } else {
      xpath = "//label[contains(text(), '" + partialText + "')]";
    }
    String id = WebElementUtil.findElementByXPath(xpath, searchContext).getAttribute("for");
    return WebElementUtil.findElementById(id);
  }

}
