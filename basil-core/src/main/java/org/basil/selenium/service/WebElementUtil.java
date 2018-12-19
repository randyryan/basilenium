/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.service;

import java.util.List;

import org.basil.selenium.BasilElement;
import org.basil.selenium.base.DriverFactory;
import org.basil.selenium.service.WebElementService.NeighboringElementProvider;
import org.basil.selenium.service.WebElementService.ValueConverter;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * WebElementUtil
 *
 * @author ryan131
 * @since Sep 15, 2014, 10:41:19 AM
 */
public final class WebElementUtil {
  private WebElementUtil() {}

  // Find element services

  public static WebElement findElement(By locator) {
    return service.findElement(locator, context);
  }

  public static WebElement findElement(By locator, SearchContext context) {
    return service.findElement(locator, context);
  }

  public static List<WebElement> findElements(By locator) {
    return service.findElements(locator, context);
  }

  public static List<WebElement> findElements(By locator, SearchContext context) {
    return service.findElements(locator, context);
  }

  public static WebElement findElementByXPath(String xpathExpression) {
    return service.findElementByXPath(xpathExpression, context);
  }

  public static WebElement findElementByXPath(String xpathExpression, SearchContext context) {
    return service.findElementByXPath(xpathExpression, context);
  }

  public static List<WebElement> findElementsByXPath(String xpathExpression) {
    return service.findElementsByXPath(xpathExpression, context);
  }

  public static List<WebElement> findElementsByXPath(String xpathExpression, SearchContext context) {
    return service.findElementsByXPath(xpathExpression, context);
  }

  public static WebElement findElementById(String id) {
    return service.findElementByXPath("//*[@id='" + id + "']", context);
  }

  public static WebElement findElementById(String id, SearchContext context) {
    return service.findElementByXPath("//*[@id='" + id + "']", context);
  }

  public static WebElement findElementByText(String text) {
    return service.findElementByXPath("//*[text()='" + text + "']", context);
  }

  public static WebElement findElementByText(String text, SearchContext context) {
    return service.findElementByXPath("//*[text()='" + text + "']", context);
  }

  public static List<WebElement> findElementsByText(String text) {
    return service.findElementsByXPath("//*[text()='" + text + "']", context);
  }

  public static List<WebElement> findElementsByText(String text, SearchContext context) {
    return service.findElementsByXPath("//*[text()='" + text + "']", context);
  }

  public static WebElement findElementByLabel(String label) {
    return service.findElementByXPath("//*[@id=//label[text()='" + label + "']/@for]", context);
  }

  public static WebElement findElementByLabel(String label, SearchContext context) {
//  return service.findElementByXPath("//*[@id=//label[text()='" + label + "']/@for]", context);

//    String xpath = null;
//    if (label.contains("'")) {
//      xpath = "//label[text()=\"" + label + "\"]";
//    } else {
//      xpath = "//label[text()='" + label + "']";
//    }
//    return findElementById(service.findElementByXPath(xpath, context).getAttribute("for"));

    return BasilElement.create(context, label);
  }

  public static List<WebElement> findElementsByLabel(String label) {
    return service.findElementsByXPath("//label[text()='" + label + "']", context);
  }

  public static List<WebElement> findElementsByLabel(String label, SearchContext context) {
    List<WebElement> labels = service.findElementsByXPath("//label[text()='" + label + "']", context);
    List<WebElement> elements = Lists.newArrayList();
    for (WebElement currentLabel : labels) {
      String id = currentLabel.getAttribute("for");
      if (id != null && !id.equals("")) {
        try {
          elements.add(findElementById(id));
        } catch (NoSuchElementException nsee) {
          logger.error("The label \"" + label + "\"'s \"for\" does not point to a valid element.");
        }
      } else {
        logger.error("The label \"" + label + "\" does not contain a valid \"for\" attribute.");
      }
    }
    if (elements.size() == 0) {
      throw new NoSuchElementException("No elements can be found by label \"" + label + "\".");
    }
    return elements;
  }

  public static WebElement findLabel(String label) {
    return service.findElementByXPath("//label[text()='" + label + "']", context);
  }

  public static WebElement findLabel(String label, SearchContext context) {
    return service.findElementByXPath("//label[text()='" + label + "']", context);
  }

  public static String findLabelForId(String label) {
    return findLabel(label).getAttribute("for");
  }

  public static String findLabelForId(String label, SearchContext context) {
    return findLabel(label, context).getAttribute("for");
  }

  // WebElement Standard Services

  // WebElement Click Services

  public static void click(WebElement element) {
    service.click(element);
  }

  public void click(WebElement element, Clicker clicker) {
    service.click(element, clicker);
  }

  public static void clickByJs(WebElement element) {
    service.click(element, Clicker.javascript((JavascriptExecutor) context));
  }

  public static void clickByJs(WebElement element, JavascriptExecutor executor) {
    service.click(element, Clicker.javascript(executor));
  }

  public static void clickByActions(WebElement element) {
    service.click(element, Clicker.actions((WebDriver) context));
  }

  public static void clickByActionsHover(WebElement element) {
    service.click(element, Clicker.actionsHover((WebDriver) context));
  }

  @Deprecated
  public static void clickLink(WebElement link) {
    service.click(link, Clicker.link());
  }

  @Deprecated
  public static void clickButton(WebElement button) {
    service.click(button, Clicker.button());
  }

  @Deprecated
  public static void checkCheckBox(WebElement checkBox) {
    service.validate(checkBox, ValidationRule.isInputCheckBox());
    service.selectElement(checkBox);
  }

  @Deprecated
  public static void uncheckCheckBox(WebElement checkBox) {
    service.validate(checkBox, ValidationRule.isInputCheckBox());
    service.unselectElement(checkBox);
  }

  @Deprecated
  public static void selectRadioButton(WebElement radioButton) {
    service.validate(radioButton, ValidationRule.isInputRadioBox());
    service.selectElement(radioButton);
  }

  public static void selectElement(WebElement element) {
    service.selectElement(element);
  }

  public static void selectElements(Iterable<WebElement> elements) {
    service.selectElements(elements);
  }

  public static void unselectElement(WebElement element) {
    service.unselectElement(element);
  }

  public static void unselectElements(Iterable<WebElement> elements) {
    service.unselectElements(elements);
  }

  public static void submit(WebElement element) {
    service.submit(element);
  }

  public static void sendKeys(WebElement element, CharSequence... keysToSend) {
    service.sendKeys(element, keysToSend);
  }

  public static void clear(WebElement element) {
    service.clear(element);
  }

  public static String getTagName(WebElement element) {
    return service.getTagName(element);
  }

  @Deprecated
  public static String getTag(WebElement element) {
    return service.getTag(element);
  }

  // WebElement Attribute Services

  public static String getAttribute(WebElement element, String name) {
    return service.getAttribute(element, name);
  }

  public static boolean hasAttribute(WebElement element, String attributeName) {
    return service.hasAttribute(element, attributeName);
  }

  @Deprecated
  public static String getAttr(WebElement element, String attrName) {
    return service.getAttr(element, attrName);
  }

  @Deprecated
  public static boolean hasAttr(WebElement element, String attrName) {
    return service.hasAttr(element, attrName);
  }

  public static String getId(WebElement element) {
    return service.getId(element);
  }

  public static boolean hasId(WebElement element) {
    return service.hasId(element);
  }

  public static String getClass(WebElement element) {
    return service.getClass(element);
  }

  public static boolean hasClass(WebElement element, String className) {
    return service.hasClass(element, className);
  }

  public static String getTitle(WebElement element) {
    return service.getTitle(element);
  }

  public static boolean hasTitle(WebElement element) {
    return service.hasTitle(element);
  }

  public static String getType(WebElement element) {
    return service.getType(element);
  }

  public static boolean hasType(WebElement element) {
    return service.hasType(element);
  }

  public static ValueConverter getValue(WebElement element) {
    return service.getValue(element);
  }

  public static String getInnerHTML(WebElement element) {
    return service.getInnerHTML(element);
  }

  public static String getOuterHTML(WebElement element) {
    return service.getOuterHTML(element);
  }

  public static boolean isSelected(WebElement element) {
    return service.isSelected(element);
  }

  // WebElement Interactability Services

  public static boolean isEnabled(WebElement element) {
    return service.isEnabled(element);
  }

  public static boolean isDisabled(WebElement element) {
    return service.isDisabled(element);
  }

  public static boolean isReadonly(WebElement element) {
    return service.isReadonly(element);
  }

  public static boolean isInteractible(WebElement element) {
    return service.isInteractible(element);
  }

  @Deprecated
  public static WebElement getInteractible(WebDriverWait wait, WebElement element) {
    return service.getInteractible(wait, element);
  }

  // WebElement Text Services

  public static String getText(WebElement element) {
    return service.getText(element);
  }

  public static List<String> getText(List<WebElement> elements) {
    return service.getText(elements);
  }

  @Deprecated
  public static void inputText(WebElement element, String text) {
    service.inputText(element, text);
  }

  public static boolean isDisplayed(WebElement element) {
    return service.isDisplayed(element);
  }

  public static Point getLocation(WebElement element) {
    return service.getLocation(element);
  }

  public static Dimension getSize(WebElement element) {
    return service.getSize(element);
  }

  public static Rectangle getRect(WebElement element) {
    return service.getRect(element);
  }

  public static String getCssValue(WebElement element, String propertyName) {
    return service.getCssValue(element, propertyName);
  }

  // WebElement Property Services

  @Deprecated
  public static String getXPath(WebElement element) {
    return service.getXPath(element);
  }

  @Deprecated
  public static String getXPath(WebElement element, String xpathExpression) {
    return service.getXPath(element, xpathExpression);
  }

  // WebElement Validation Services

  public static WebElement validate(WebElement element, ValidationRule rule) {
    return service.validate(element, rule);
  }

  // TODO Remove this method
  @Deprecated
  public static boolean validateTagAndClass(WebElement element, String tag, String clazz) {
    return WebElementServiceImpl.ElementValidator.validateTagAndClass(element, tag, clazz);
  }

  // Neighboring elements services

  @Deprecated
  public static WebElement getPreviousElementSibling(WebElement element) {
    return service.getPreviousElementSibling(element);
  }

  @Deprecated
  public static WebElement getNextElementSibling(WebElement element) {
    return service.getNextElementSibling(element);
  }

  @Deprecated
  public static WebElement getParentElement(WebElement childElement) {
    return service.getParentElement(childElement);
  }

  @Deprecated
  public static WebElement getGrandparentElement(WebElement grandchildElement) {
    return service.getGrandparentElement(grandchildElement);
  }

  @Deprecated
  public static WebElement getChildElement(WebElement parentElement, int index) {
    return service.getChildElement(parentElement, index);
  }

  public static NeighboringElementProvider getNeighboringElement(WebElement element) {
    return service.getNeighboringElement(element);
  }

  private static final Logger logger = LoggerFactory.getLogger(WebElementService.class);

  private static final WebElementService service = new WebElementServiceImpl();

  private static final SearchContext context = DriverFactory.getWebDriver();

}
