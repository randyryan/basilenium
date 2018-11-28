/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.service;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * XPathService
 *
 * @author ryan131
 * @since Oct 14, 2016, 3:17:04 PM
 */
public interface XPathService {

  // XPath retrieval

  String getXPath(WebElement element);

  String getXPath(WebElement element, String xpathExpression);

  String getXPath(By locator);

  String getXPath(By locator, String xpathExpression);

  // Concatenation

  String append(String... xpathExpressions);

  String append(Iterable<String> xpathExpressions);

  String append(String xpathExpression, By... locators);

  String append(By... locators);

  String append(By locator, String... xpathExpressions);

  By.ByXPath concat(String... xpathExpressions);

  By.ByXPath concat(String xpathExpression, By... locators);

  By.ByXPath concat(By... locators);

  By.ByXPath concat(By locator, String... xpathExpressions);

}
