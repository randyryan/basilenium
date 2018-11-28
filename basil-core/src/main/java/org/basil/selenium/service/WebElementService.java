/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.service;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * WebElementService
 *
 * @author ryan131
 * @since Sep 14, 2014, 1:34:15 PM
 */
public interface WebElementService {

  // Find services

  WebElement findElement(By locator);

  WebElement findElement(By locator, SearchContext context);

  List<WebElement> findElements(By locator);

  List<WebElement> findElements(By locator, SearchContext context);

  WebElement findElementByXPath(String xpath);

  WebElement findElementByXPath(String xpath, SearchContext context);

  List<WebElement> findElementsByXPath(String xpath);

  List<WebElement> findElementsByXPath(String xpath, SearchContext context);

  // Property services

  String getId(WebElement element);

  String getTag(WebElement element);

  String getType(WebElement element);

  String getAttr(WebElement element, String attrName);

  String getClass(WebElement element);

  @Deprecated
  String getXPath(WebElement element);

  @Deprecated
  String getXPath(WebElement element, String xpathExpression);

  String getValue(WebElement element);

  ValueConverter value(WebElement element);

  public interface ValueConverter {

    int asInt();

    long asLong();

    float asFloat();

    double asDouble();

    boolean asBoolean();

  }

  String getText(WebElement element);

  List<String> getText(List<WebElement> elements);

  boolean isEnabled(WebElement element);

  boolean isDisabled(WebElement element);

  boolean isInteractible(WebElement element);

  @Deprecated
  WebElement getInteractible(WebDriverWait wait, WebElement element);

  // Validation services

  boolean validateTag(WebElement element, String tag);

  boolean validateTagAndClass(WebElement element, String tag, String clazz);

  boolean validateInputType(WebElement element, String type);

  boolean validateTextInput(WebElement element);

  boolean validateLink(WebElement link);

  boolean validateButton(WebElement button);

  boolean validateCheckBox(WebElement checkBox);

  boolean validateRadioButton(WebElement radioButton);

  // Interaction services

  void click(WebElement element);

  void jsClick(WebElement element);

  void jsClick(JavascriptExecutor executor, WebElement element);

  void actionsClick(WebElement element);

  void actionsHoverClick(WebElement element);

  void clickLink(WebElement link);

  void clickButton(WebElement button);

  void checkCheckBox(WebElement checkBox);

  void uncheckCheckBox(WebElement checkBox);

  void selectRadioButton(WebElement radioButton);

  void selectElements(Iterable<WebElement> elements);

  void inputText(WebElement element, String text);

  // Neighboring elements services

  @Deprecated
  WebElement getPreviousElementSibling(WebElement element);

  @Deprecated
  WebElement getNextElementSibling(WebElement element);

  @Deprecated
  WebElement getParentElement(WebElement childElement);

  @Deprecated
  WebElement getGrandparentElement(WebElement grandchildElement);

  @Deprecated
  WebElement getChildElement(WebElement parentElement, int index);

  NeighboringElementProvider getNeighboringElement(WebElement element);

  public interface NeighboringElementProvider {

    WebElement previous() throws NoSuchElementException;

    WebElement previous(int nth) throws NoSuchElementException;

    WebElement next() throws NoSuchElementException;

    WebElement next(int nth) throws NoSuchElementException;

    WebElement parent() throws NoSuchElementException;

    WebElement parent(int nth) throws NoSuchElementException;

    WebElement child() throws NoSuchElementException;

    WebElement child(int nth) throws NoSuchElementException;

  }

  // Transformation services

}
