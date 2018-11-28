/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.page;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base Lookup - The backbone of all other types of Lookup. Uses no cache,
 * operations on this class can be costly when there are too many elements.
 *
 * @author ryan131
 * @since May 19, 2014, 2:18:03 PM
 */
public class BaseLookup implements Lookup {

  private static final Logger logger = LoggerFactory.getLogger(BaseLookup.class);

  protected SearchContext searchContext;

  public BaseLookup(SearchContext searchContext) {
    this.searchContext = searchContext;
  }

  // Look up by text

  @Override
  public WebElement byText(List<WebElement> elements, String text) {
    for (WebElement element : elements) {
      if (element.getText().trim().equals(text)) {
//        logger.info("Found \"" + text + "\" among " + elements.size() + " elements.");
        return element;
      }
    }

    logger.error("Cannot find \"" + text + "\" in the current search context.");
    return null;
  }

  @Override
  public WebElement byPartialText(List<WebElement> elements, String partialText) {
    for (WebElement element : elements) {
      if (element.getText().trim().contains(partialText)) {
        logger.info("Found \"" + partialText + "\" (partial text) among " + elements.size() + " elements.");
        return element;
      }
    }

    logger.error("Cannot find \"" + partialText + "\" (partial text) in the current search context.");
    return null;
  }

  @Override
  public WebElement byText(By by, String text) {
    return byText(searchContext.findElements(by), text);
  }

  @Override
  public WebElement byPartialText(By by, String partialText) {
    return byPartialText(searchContext.findElements(by), partialText);
  }

  @Override
  public WebElement byTagAndText(String tagName, String text) {
    return byText(searchContext.findElements(By.tagName(tagName)), text);
  }

  @Override
  public WebElement byClassAndText(String clazz, String text) {
    return byText(searchContext.findElements(By.className(clazz)), text);
  }

  @Override
  public WebElement byCssSelectorAndText(String selector, String text) {
    return byText(searchContext.findElements(By.cssSelector(selector)), text);
  }

  // Look up by element attribute

  @Override
  public WebElement byAttribute(List<WebElement> elements, String attribute, String value) {
    for (WebElement element : elements) {
      if (element.getAttribute(attribute).equals(value)) {
        return element;
      }
    }

    logger.error("Cannot find element with attribute \"" +
        attribute + "=" + value + "\" in the current search context.");
    return null;
  }

  @Override
  public WebElement byAttribute(By by, String attribute, String value) {
    return byAttribute(searchContext.findElements(by), attribute, value);
  }

  // Lookup by label elements

  @Override
  public WebElement label(String text) {
    return byTagAndText("label", text);
  }

  @Override
  public WebElement partialLabel(String partialText) {
    return byPartialText(By.tagName("label"), partialText);
  }

  @Override
  public WebElement byLabel(WebElement label) {
    return searchContext.findElement(By.id(label.getAttribute("for")));
  }

  @Override
  public WebElement byLabel(String text) {
    return byLabel(label(text));
  }

  @Override
  public WebElement byPartialLabel(String partialText) {
    return byLabel(partialLabel(partialText));
  }

  @Override
  public WebElement link(String linkText) {
    return searchContext.findElement(By.linkText(linkText));
  }

  @Override
  public WebElement partialLink(String linkText) {
    return searchContext.findElement(By.partialLinkText(linkText));
  }

}
