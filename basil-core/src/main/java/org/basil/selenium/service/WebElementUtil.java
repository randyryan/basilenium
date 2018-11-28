/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.service;

import java.util.List;

import org.basil.selenium.BasilElement;
import org.basil.selenium.base.DriverFactory;
import org.basil.selenium.service.WebElementService.NeighboringElementProvider;
import org.basil.selenium.service.WebElementService.ValueConverter;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
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

  private static final Logger logger = LoggerFactory.getLogger(WebElementService.class);
  private static final WebElementService service = new WebElementServiceImpl(DriverFactory.getWebDriver());

  // Find services

  public static WebElement findElementByXPath(String xpath) {
    return service.findElementByXPath(xpath);
  }

  public static WebElement findElementByXPath(String xpath, SearchContext context) {
    return service.findElementByXPath(xpath, context);
  }

  public static List<WebElement> findElementsByXPath(String xpath) {
    return service.findElementsByXPath(xpath);
  }

  public static List<WebElement> findElementsByXPath(String xpath, SearchContext context) {
    return service.findElementsByXPath(xpath, context);
  }

  public static WebElement findElementById(String id) {
    return service.findElementByXPath("//*[@id='" + id + "']");
  }

  public static WebElement findElementByText(String text) {
    return service.findElementByXPath("//*[text()='" + text + "']");
  }

  public static WebElement findElementByText(String text, SearchContext context) {
    return service.findElementByXPath("//*[text()='" + text + "']", context);
  }

  public static List<WebElement> findElementsByText(String text) {
    return service.findElementsByXPath("//*[text()='" + text + "']");
  }

  public static List<WebElement> findElementsByText(String text, SearchContext context) {
    return service.findElementsByXPath("//*[text()='" + text + "']", context);
  }

  public static WebElement findElementByLabel(String label) {
    return service.findElementByXPath("//*[@id=//label[text()='" + label + "']/@for]");
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
    return service.findElementsByXPath("//label[text()='" + label + "']");
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

  // Property services

  public static String getId(WebElement element) {
    return service.getId(element);
  }

  public static String getTag(WebElement element) {
    return service.getTag(element);
  }

  public static String getType(WebElement element) {
    return service.getType(element);
  }

  public static String getAttr(WebElement element, String attrName) {
    return service.getAttr(element, attrName);
  }

  public static String getClass(WebElement element) {
    return service.getClass(element);
  }

  @Deprecated
  public static String getXPath(WebElement element) {
    return service.getXPath(element);
  }

  @Deprecated
  public static String getXPath(WebElement element, String xpathExpression) {
    return service.getXPath(element, xpathExpression);
  }

  public static String getValue(WebElement element) {
    return service.getValue(element);
  }

  public static ValueConverter value(WebElement element) {
    return service.value(element);
  }

  public static String getText(WebElement element) {
    return service.getText(element);
  }

  public static List<String> getText(List<WebElement> elements) {
    return service.getText(elements);
  }

  public static boolean isEnabled(WebElement element) {
    return service.isEnabled(element);
  }

  public static boolean isDisabled(WebElement element) {
    return service.isDisabled(element);
  }

  public static boolean isInteractible(WebElement element) {
    return service.isInteractible(element);
  }

  @Deprecated
  public static WebElement getInteractible(WebDriverWait wait, WebElement element) {
    return service.getInteractible(wait, element);
  }

  // Validation services

  /**
   * Temporarily allow this method to be accessed here, 'till there's a new set of validation API.
   */
  @Deprecated
  public static boolean validateTagAndClass(WebElement element, String tag, String clazz) {
    return WebElementServiceImpl.ElementValidator.validateTagAndClass(element, tag, clazz);
  }

  // Interaction services

  public static void click(WebElement element) {
    service.click(element);
  }

  public static void jsClick(WebElement element) {
    service.jsClick(element);
  }

  public static void jsClick(JavascriptExecutor executor, WebElement element) {
    service.jsClick(executor, element);
  }

  public static void actionsClick(WebElement element) {
    service.actionsClick(element);
  }

  public static void actionsHoverClick(WebElement element) {
    service.actionsHoverClick(element);
  }

  public static void clickLink(WebElement link) {
    service.clickLink(link);
  }

  public static void clickButton(WebElement button) {
    service.clickButton(button);
  }

  public static void checkCheckBox(WebElement checkBox) {
    service.checkCheckBox(checkBox);
  }

  public static void uncheckCheckBox(WebElement checkBox) {
    service.uncheckCheckBox(checkBox);
  }

  public static void selectRadioButton(WebElement radioButton) {
    service.selectRadioButton(radioButton);
  }

  public static void selectElements(Iterable<WebElement> elements) {
    service.selectElements(elements);
  }

  public static void inputText(WebElement element, String text) {
    service.inputText(element, text);
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

  public NeighboringElementProvider getNeighboringElement(WebElement element) {
    return service.getNeighboringElement(element);
  }

}
