/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.page;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cached Lookup
 *
 * @author ryan131
 * @since Dec 14, 2014, 7:32:40 PM
 */
public class CachedLookup extends BaseLookup {

  private static final Logger logger = LoggerFactory.getLogger(CachedLookup.class);

  private Map<By, Map<String, WebElement>> elementMapMap;

  CachedLookup(SearchContext searchContext) {
    // So far, CachedLookup is never used directly.
    super(searchContext);
    this.elementMapMap = new HashMap<By, Map<String, WebElement>>();
  }

  // Internal service method

  private Map<String, WebElement> initializeElementMap(By by) {
    Map<String, WebElement> elementMap = elementMapMap.get(by);

    if (elementMap == null) {
      // Create map if there isn't a map for the given by
      long cacheStart = System.currentTimeMillis();

      elementMap = new HashMap<String, WebElement>();
//      logger.info("Cache sector created for locator: \"" + by + "\".");

      int ignoredElementCount = 0;
      for (WebElement element : searchContext.findElements(by)) {
        if (!element.isDisplayed()) {
          continue;
        }

        String elementText = element.getText();
//        String elementId = element.getAttribute("id");

        if (!elementText.equals("")) {
          elementMap.put(elementText, element);
//            logger.info("Cached element \"" + elementText + "\" <===> \"" + elementId + "\".");
        } else {
          ignoredElementCount++;
//          logger.warn("Element \"" + elementText + "\" isn't eligible to be cached.");
        }
      }

      elementMapMap.put(by, elementMap);

      long cacheUsed = System.currentTimeMillis() - cacheStart;
      String ignored = "and ignored " + ignoredElementCount + " elements";
      String message = "Lookup cached " + elementMap.size() + " elements " +
          (ignoredElementCount > 0 ? ignored : "") + " in " + cacheUsed + " ms.";
      //logger.info(message);
    }

    return elementMap;
  }

  // Look up by text

  @Override
  public WebElement byText(By by, String text) {
    WebElement element = initializeElementMap(by).get(text);

    if (element == null) {
      logger.error("Unable to locate element \"" + text + "\" with " + by + "\".");
    }

    return element;
  }

  @Override
  public WebElement byPartialText(By by, String partialText) {
    String completeText = "";

    for (String text : initializeElementMap(by).keySet()) {
      if (text.contains(partialText)) {
        completeText = text;
      }
    }

//    logger.info("Looked up \"" + completeText + "\" for \"" + partialText + "\".");
    return byText(by, completeText);
  }

  @Override
  public WebElement byTagAndText(String tagName, String text) {
    return byText(By.tagName(tagName), text);
  }

  @Override
  public WebElement byClassAndText(String clazz, String text) {
    return byText(By.className(clazz), text);
  }

  @Override
  public WebElement byCssSelectorAndText(String selector, String text) {
    return byText(By.cssSelector(selector), text);
  }

  // Look up by element attribute

  @Override
  public WebElement byAttribute(By by, String attribute, String value) {
    WebElement element = null;

    for (Entry<String, WebElement> entry : initializeElementMap(by).entrySet()) {
      WebElement currentIterateElement = entry.getValue();
      if (currentIterateElement.getAttribute(attribute).equals(value)) {
        element = currentIterateElement;
      }
    }

    return element;
  }

  // Lookup by label elements

  @Override
  public WebElement label(String text) {
    return initializeElementMap(By.tagName("label")).get(text);
  }

  @Override
  public WebElement partialLabel(String partialText) {
    String completeText = "";

    for (String text : initializeElementMap(By.tagName("label")).keySet()) {
      if (text.contains(partialText)) {
        completeText = text;
      }
    }

    return label(completeText);
  }

  @Override
  public WebElement byLabel(String text) {
    WebElement label = label(text);

    if (label == null) {
      logger.error("Unable to locate element with label: \"" + text + "\".");
      throw new NoSuchElementException(text);
    }

    return searchContext.findElement(By.id(label.getAttribute("for")));
  }

  @Override
  public WebElement byPartialLabel(String partialText) {
    WebElement label = partialLabel(partialText);

    if (label == null) {
      logger.error("Unable to locate element with partial label: \"" + partialText + "\".");
      throw new NoSuchElementException(partialText);
    }

    return searchContext.findElement(By.id(label.getAttribute("for")));
  }

}
