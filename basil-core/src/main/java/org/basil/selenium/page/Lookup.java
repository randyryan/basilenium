/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.page;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Lookup - A complementary extension to the "findElement - By".
 *
 * @author ryan131
 * @since May 19, 2014, 1:39:52 PM
 */
public interface Lookup {

  // Look up by text

  /**
   * Looks up a WebElement of specified text within a List of WebElement.
   */
  WebElement byText(List<WebElement> elements, String text);

  /**
   * Looks up a WebElement contains specified text within a List of WebElement.
   */
  WebElement byPartialText(List<WebElement> elements, String partialText);

  /**
   * Looks up a WebElement of specified text in results from the given By locator.
   */
  WebElement byText(By by, String text);

  /**
   * Looks up a WebElement contains specified text in results from the given By locator.
   */
  WebElement byPartialText(By by, String partialText);

  /**
   * Simple form of byText(By.tagName(tagName), text)
   */
  WebElement byTagAndText(String tagName, String text);

  /**
   * Simple form of byText(By.className(clazz), text)
   */
  WebElement byClassAndText(String clazz, String text);

  /**
   * Simple form of byText(By.cssSelector(selector), text)
   */
  WebElement byCssSelectorAndText(String selector, String text);

  // Look up by element attribute

  WebElement byAttribute(List<WebElement> elements, String attribute, String value);

  WebElement byAttribute(By by, String attribute, String value);

  // Lookup by label elements

  WebElement label(String text);

  WebElement partialLabel(String partialText);

  /**
   * Take advantage of the label's "for" attribute. For more informations
   * please refer to <a href="http://www.w3schools.com/tags/att_label_for.asp">
   * http://www.w3schools.com/tags/att_label_for.asp</a>.
   */
  WebElement byLabel(WebElement label);

  WebElement byLabel(String text);

  WebElement byPartialLabel(String partialText);

  // Look up links

  WebElement link(String linkText);

  WebElement partialLink(String linkText);

}
