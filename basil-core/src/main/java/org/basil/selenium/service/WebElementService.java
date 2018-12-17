/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.service;

import java.util.List;

import org.basil.selenium.BasilException;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
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

  // Find element services

  WebElement findElement(By locator, SearchContext context);

  List<WebElement> findElements(By locator, SearchContext context);

  WebElement findElementByXPath(String xpathExpression, SearchContext context);

  List<WebElement> findElementsByXPath(String xpathExpression, SearchContext context);

  // WebElement Standard Services

  // WebElement Click Services

  void click(WebElement element);

  void clickByJs(WebElement element);

  void clickByJs(WebElement element, JavascriptExecutor executor);

  void clickByActions(WebElement element);

  void clickByActionsHover(WebElement element);

  void clickLink(WebElement link);

  void clickButton(WebElement button);

  @Deprecated
  void checkCheckBox(WebElement checkBox);

  @Deprecated
  void uncheckCheckBox(WebElement checkBox);

  void selectRadioButton(WebElement radioButton);

  void selectElements(Iterable<WebElement> elements);

  void submit(WebElement element);

  void sendKeys(WebElement element, CharSequence... keysToSend);

  void clear(WebElement element);

  String getTagName(WebElement element);

  @Deprecated
  String getTag(WebElement element);

  // WebElement Attribute Services

  String getAttribute(WebElement element, String name);

  boolean hasAttribute(WebElement element, String attributeName);

  @Deprecated
  String getAttr(WebElement element, String attrName);

  @Deprecated
  boolean hasAttr(WebElement element, String attrName);

  String getId(WebElement element);

  boolean hasId(WebElement element);

  String getClass(WebElement element);

  boolean hasClass(WebElement element, String className);

  String getTitle(WebElement element);

  boolean hasTitle(WebElement element);

  String getType(WebElement element);

  boolean hasType(WebElement element);

  ValueConverter getValue(WebElement element);

  public interface ValueConverter {

    boolean toBoolean();

    byte toByte();

    short toShort();

    int toInt();

    long toLong();

    float toFloat();

    double toDouble();

    char[] toChars();

    String toString();

  }

  String getInnerHTML(WebElement element);

  String getOuterHTML(WebElement element);

  boolean isSelected(WebElement element);

  // WebElement Interactability Services

  boolean isEnabled(WebElement element);

  boolean isDisabled(WebElement element);

  boolean isReadonly(WebElement element);

  boolean isInteractible(WebElement element);

  @Deprecated
  WebElement getInteractible(WebDriverWait wait, WebElement element);

  // WebElement Text Services

  String getText(WebElement element);

  List<String> getText(List<WebElement> elements);

  @Deprecated
  void inputText(WebElement element, String text);

  boolean isDisplayed(WebElement element);

  Point getLocation(WebElement element);

  Dimension getSize(WebElement element);

  Rectangle getRect(WebElement element);

  String getCssValue(WebElement element, String propertyName);

  // WebElement Property Services

  @Deprecated
  String getXPath(WebElement element);

  @Deprecated
  String getXPath(WebElement element, String xpathExpression);

  // WebElement Validation Services

  WebElement validate(WebElement element, ValidationRule rule) throws BasilException.InvalidElement;

  @Deprecated
  boolean validateTag(WebElement element, String tag);

  @Deprecated
  boolean validateTagAndClass(WebElement element, String tag, String clazz);

  @Deprecated
  boolean validateInputType(WebElement element, String type);

  @Deprecated
  boolean validateTextInput(WebElement element);

  @Deprecated
  boolean validateLink(WebElement link);

  @Deprecated
  boolean validateButton(WebElement button);

  @Deprecated
  boolean validateCheckBox(WebElement checkBox);

  @Deprecated
  boolean validateRadioButton(WebElement radioButton);

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
