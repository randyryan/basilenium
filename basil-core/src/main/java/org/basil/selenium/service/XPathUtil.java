/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

/**
 * XPathUtil
 *
 * @author ryan131
 * @since Jul 1, 2016, 3:55:24 PM
 */
public final class XPathUtil {
  private XPathUtil() {}

  private static final XPathService service = new XPathServiceImpl();

  // XPath retrieval

  public static String getXPath(WebElement element) {
    return service.getXPath(element);
  }

  public static String getXPath(WebElement element, String xpathExpression) {
    return service.getXPath(element, xpathExpression);
  }

  public static String getXPath(By locator) {
    return service.getXPath(locator);
  }

  public static String getXPath(By locator, String xpathExpression) {
    return service.getXPath(locator, xpathExpression);
  }

  /**
   * Retrieves the XPath expression of the specified element using a JS script.
   *
   * @param executor the JavascriptExecutor
   * @param element to retrieve the XPath expression of
   * @return the XPath expression of the specified element
   */
  @Deprecated
  public static String getXPath(WebElement element, JavascriptExecutor executor) {
    String xpath = null;
    try {
      String xpathJs = new String(Files.readAllBytes(Paths.get(
          Thread.currentThread().getContextClassLoader().getResource("xpath.js").toURI()
      )));
      xpath = (String) executor.executeScript(xpathJs, element);
    } catch (URISyntaxException use) {
    } catch (IOException ioe) {
    }
    return xpath;
  }

  // Concatenation

  public static String append(String... xpathExpressions) {
    return service.append(xpathExpressions);
  }

  public static String append(Iterable<String> xpathExpressions) {
    return service.append(xpathExpressions);
  }

  public static String append(String xpathExpression, By... locators) {
    return service.append(xpathExpression, locators);
  }

  public static String append(By... locators) {
    return service.append(locators);
  }

  public static String append(By locator, String... xpathExpressions) {
    return service.append(locator, xpathExpressions);
  }

  public static By.ByXPath concat(String... xpathExpressions) {
    return service.concat(xpathExpressions);
  }

  public static By.ByXPath concat(String xpathExpression, By... locators) {
    return service.concat(xpathExpression, locators);
  }

  public static By.ByXPath concat(By... locators) {
    return service.concat(locators);
  }

  public static By.ByXPath concat(By locator, String... xpathExpressions) {
    return service.concat(locator, xpathExpressions);
  }

}
