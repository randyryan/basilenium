/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.basil.Config;
import org.basil.selenium.BasilElement;
import org.basil.selenium.BasilException;
import org.basil.selenium.page.PageObject;
import org.basil.selenium.ui.ExtendedConditions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

/**
 * WebElementServiceImpl
 *
 * TODO Cache the elements' id and tag because those are very less likely to change
 *      and probably very safe to retrieve them only for once.
 *
 * @author ryan131
 * @since Sep 14, 2014, 1:38:52 PM
 */
public class WebElementServiceImpl implements WebElementService {

  // Find element services

  @Override
  public WebElement findElement(By locator, SearchContext context) {
    if (locator instanceof By.ByXPath) {
      return findElementByXPath(XPathUtil.getXPath(locator), context);
    }
    return context.findElement(locator);
  }

  @Override
  public List<WebElement> findElements(By locator, SearchContext context) {
    if (locator instanceof By.ByXPath) {
      return findElementsByXPath(XPathUtil.getXPath(locator), context);
    }
    return context.findElements(locator);
  }

  /**
   * Core service method.
   */
  @Override
  public WebElement findElementByXPath(String xpathExpression, SearchContext context) {
    By locator = By.xpath(xpathExpression);
    if (context instanceof PageObject) {
      locator = ((PageObject) context).getConfidentLocator().append(xpathExpression);
    }
    if (context instanceof WebElement) {
      locator = By.xpath(getXPath((WebElement) context) + xpathExpression);
    }
    return BasilElement.create(context, locator);
  }

  /**
   * Core service method.
   */
  @Override
  public List<WebElement> findElementsByXPath(String xpathExpression, SearchContext context) {
    By locator = By.xpath(xpathExpression);
    if (context instanceof PageObject) {
      locator = ((PageObject) context).getConfidentLocator().append(xpathExpression);
    }
    if (context instanceof WebElement) {
      locator = By.xpath(getXPath((WebElement) context) + xpathExpression);
    }
    xpathStatistics.recordHit(xpathExpression);
    return context.findElements(locator);
  }

  // WebElement attribute services

  @Override
  public String getAttr(WebElement element, String attrName) {
    return ElementActions.attrOf(element, attrName);
  }

  @Override
  public boolean hasAttr(WebElement element, String attrName) {
    return !Strings.isNullOrEmpty(element.getAttribute(attrName));
  }

  @Override
  public String getId(WebElement element) {
    return ElementActions.idOf(element);
  }

  @Override
  public boolean hasId(WebElement element) {
    return hasAttr(element, "id");
  }

  @Override
  public String getClass(WebElement element) {
    return ElementActions.classOf(element);
  }

  @Override
  public boolean hasClass(WebElement element, String className) {
    return getClass(element).toLowerCase().contains(className.toLowerCase());
  }

  @Override
  public String getTitle(WebElement element) {
    return element.getAttribute("title");
  }

  @Override
  public boolean hasTitle(WebElement element) {
    return hasAttr(element, "title");
  }

  @Override
  public String getType(WebElement element) {
    return ElementActions.typeOf(element);
  }

  @Override
  public boolean hasType(WebElement element) {
    return hasAttr(element, "type");
  }

  @Override
  public ValueConverter getValue(WebElement element) {
    return ElementActions.value(element);
  }

  @Override
  public String getInnerHTML(WebElement element) {
    return element.getAttribute("innerHTML");
  }

  @Override
  public String getOuterHTML(WebElement element) {
    return element.getAttribute("outerHTML");
  }

  // WebElement property services

  @Override
  public String getTag(WebElement element) {
    return ElementActions.tagOf(element);
  }

  @Deprecated
  @Override
  public String getXPath(WebElement element) {
    // return ElementActions.xpathOf(element);
    return XPathUtil.getXPath(element);
  }

  @Deprecated
  @Override
  public String getXPath(WebElement element, String xpathExpression) {
    // return ElementActions.xpathOf(element, xpathExpression);
    return XPathUtil.getXPath(element, xpathExpression);
  }

  @Override
  public String getText(WebElement element) {
    return ElementActions.textOf(element);
  }

  @Override
  public List<String> getText(List<WebElement> elements) {
    return ElementActions.textOf(elements);
  }

  @Override
  public boolean isEnabled(WebElement element) {
    return ElementActions.isEnabled(element);
  }

  @Override
  public boolean isDisabled(WebElement element) {
    return ElementActions.isDisabled(element);
  }

  @Override
  public boolean isInteractible(WebElement element) {
    // ElementActions does not contain such method
    boolean isDisplayed = element.isDisplayed();
    boolean isEnabled = element.isEnabled();
    boolean isEnabledInClass = !hasClass(element, "disabled");
    return isDisplayed && isEnabled && isEnabledInClass;
  }

  @Deprecated
  @Override
  public WebElement getInteractible(WebDriverWait wait, WebElement element) {
    return ElementActions.interactible(wait, element);
  }

  // Validation services

  @Override
  public boolean validateTag(WebElement element, String tag) {
    return ElementValidator.validateTag(element, tag);
  }

  @Override
  public boolean validateTagAndClass(WebElement element, String tag, String clazz) {
    return ElementValidator.validateTagAndClass(element, tag, clazz);
  }

  @Override
  public boolean validateInputType(WebElement input, String type) {
    return ElementValidator.validateInputType(input, type);
  }

  @Override
  public boolean validateTextInput(WebElement input) {
    return ElementValidator.validateTextInput(input);
  }

  @Override
  public boolean validateLink(WebElement link) {
    return ElementValidator.validateLink(link);
  }

  @Override
  public boolean validateButton(WebElement button) {
    return ElementValidator.validateButton(button);
  }

  @Override
  public boolean validateCheckBox(WebElement checkBox) {
    return ElementValidator.validateCheckBox(checkBox);
  }

  @Override
  public boolean validateRadioButton(WebElement radioButton) {
    return ElementValidator.validateRadioButton(radioButton);
  }

  // Interaction services

  @Override
  public void click(WebElement element) {
    ElementActions.click(element);
  }

  @Override
  public void jsClick(WebElement element) {
    ElementActions.jsClick(element);
  }

  @Override
  public void jsClick(JavascriptExecutor executor, WebElement element) {
    ElementActions.jsClick(executor, element);
  }

  @Override
  public void actionsClick(WebElement element) {
    ElementActions.actionsClick(element);
  }

  @Override
  public void actionsHoverClick(WebElement element) {
    ElementActions.actionsHoverClick(element);
  }

  @Override
  public void clickLink(WebElement link) {
    ElementActions.clickLink(link);
  }

  @Override
  public void clickButton(WebElement button) {
    ElementActions.clickButton(button);
  }

  @Override
  public void checkCheckBox(WebElement checkBox) {
    ElementActions.checkCheckBox(checkBox);
  }

  @Override
  public void uncheckCheckBox(WebElement checkBox) {
    ElementActions.uncheckCheckBox(checkBox);
  }

  @Override
  public void selectRadioButton(WebElement radioButton) {
    ElementActions.selectRadioButton(radioButton);
  }

  @Override
  public void selectElements(Iterable<WebElement> elements) {
    ElementActions.selectElements(elements);
  }

  @Override
  public void inputText(WebElement element, String text) {
    ElementActions.inputText(element, text);
  }

  // Neighboring elements services

  @Deprecated
  @Override
  public WebElement getPreviousElementSibling(WebElement element) {
    return ElementActions.getPreviousElementSibling(element);
  }

  @Deprecated
  @Override
  public WebElement getNextElementSibling(WebElement element) {
    return ElementActions.getNextElementSibling(element);
  }

  @Deprecated
  @Override
  public WebElement getParentElement(WebElement childElement) {
    return ElementActions.getParentElement(childElement);
  }

  @Deprecated
  @Override
  public WebElement getGrandparentElement(WebElement grandchildElement) {
    return ElementActions.getGrandparentElement(grandchildElement);
  }

  @Deprecated
  @Override
  public WebElement getChildElement(WebElement parentElement, int index) {
    return ElementActions.getChildElement(parentElement, index);
  }

  @Override
  public NeighboringElementProvider getNeighboringElement(WebElement element) {
    return new NeighboringElementProvider() {

      @Override
      public WebElement previous() throws NoSuchElementException {
        return previous(1);
      }

      @Override
      public WebElement previous(int nth) throws NoSuchElementException {
        throw new UnsupportedOperationException("To be implemented.");
      }

      @Override
      public WebElement next() throws NoSuchElementException {
        return next(1);
      }

      @Override
      public WebElement next(int nth) throws NoSuchElementException {
        throw new UnsupportedOperationException("To be implemented.");
      }

      @Override
      public WebElement parent() throws NoSuchElementException {
        return parent(1);
      }

      @Override
      public WebElement parent(int nth) throws NoSuchElementException {
        throw new UnsupportedOperationException("To be implemented.");
      }

      @Override
      public WebElement child() throws NoSuchElementException {
        return child(1);
      }

      @Override
      public WebElement child(int nth) throws NoSuchElementException {
        throw new UnsupportedOperationException("To be implemented.");
      }

    };
  }

  /**
   * Element Actions provides easy of operation on certain types of web controls
   * as well as reducing code repeat and boosting test script performance as JVM
   * will automatically optimizes frequently used objects.
   *
   * @author ryan131
   * @since Oct 11, 2015, 7:00:18 PM
   */
  static final class ElementActions extends ServiceContext {
    private ElementActions() {}

    // Property

    public static String idOf(WebElement element) {
      return attrOf(element, "id");
    }

    public static String tagOf(WebElement element) {
      return element.getTagName();
    }

    public static String typeOf(WebElement element) {
      return attrOf(element, "type");
    }

    public static String attrOf(WebElement element, String attribute) {
      return element.getAttribute(attribute);
    }

    public static String classOf(WebElement element) {
      return attrOf(element, "class");
    }

    @Deprecated
    public static String xpathOf(WebElement element) {
      return XPathUtil.getXPath(element);
    }

    @Deprecated
    public static String xpathOf(WebElement element, String xpathExpression) {
      return XPathUtil.getXPath(element, xpathExpression);
    }

    public static String valueOf(WebElement element) {
      return attrOf(element, "value");
    }

    public static ValueConverter value(WebElement element) {
      final String value = Strings.nullToEmpty(valueOf(element));
      return new ValueConverter() {

        @Override
        public boolean toBoolean() {
          return value.equals("") ? false : Boolean.parseBoolean(value);
        }

        @Override
        public byte toByte() {
          return value.equals("") ? 0 : Byte.parseByte(value);
        }

        @Override
        public short toShort() {
          return value.equals("") ? 0 : Short.parseShort(value);
        }

        @Override
        public int toInt() {
          return value.equals("") ? 0 : Integer.parseInt(value);
        }

        @Override
        public long toLong() {
          return value.equals("") ? 0L : Long.parseLong(value);
        }

        @Override
        public float toFloat() {
          return value.equals("") ? 0.0F : Float.parseFloat(value);
        }

        @Override
        public double toDouble() {
          return value.equals("") ? 0.0D : Double.parseDouble(value);
        }

        @Override
        public char[] toChars() {
          return value.toCharArray();
        }

        @Override
        public String toString() {
          return value;
        }

      };
    }

    public static String textOf(WebElement element) {
      return element.getText();
    }

    public static List<String> textOf(List<WebElement> elements) {
      List<String> texts = new ArrayList<String>();
      for (WebElement element : elements) {
        texts.add(element.getText());
      }
      return texts;
    }

    public static boolean isEnabled(WebElement element) {
      if (Config.Value.WEB_ELEMENT_ENABLE_LATENCY > 0) {
        try {
          Thread.sleep(Config.Value.WEB_ELEMENT_ENABLE_LATENCY);
        } catch (InterruptedException ignored) {}
      }
      boolean enabled = element.isDisplayed() && element.isEnabled();
      boolean attributeEnabled = !element.getAttribute("class").contains("isable");
      return enabled && attributeEnabled;
    }

    public static boolean isDisabled(WebElement element) {
      return !isEnabled(element);
    }

    public static WebElement interactible(WebDriverWait wait, WebElement element) {
      return wait.until(ExtendedConditions.elementToBeInteractible(element));
    }

    // Interaction

    public static void click(WebElement element) {
      element.click();
    }

    public static void jsClick(WebElement element) {
      jsClick(getJsExecutor(), element);
    }

    public static void jsClick(JavascriptExecutor executor, WebElement element) {
      executor.executeScript("arguments[0].click();", element);
    }

    public static void actionsClick(WebElement element) {
      new Actions(getDriver()).click(element).click().perform();
    }

    public static void actionsHoverClick(WebElement element) {
      new Actions(getDriver()).moveToElement(element).click().perform();
    }

    public static void clickLink(WebElement link) {
      ElementValidator.validateLink(link);
      link = interactible(getWait(), link);
      link.click();
    }

    public static void clickButton(WebElement button) {
      ElementValidator.validateButton(button);
      button = interactible(getWait(), button);
      button.click();
    }

    public static void checkCheckBox(WebElement checkBox) {
      ElementValidator.validateCheckBox(checkBox);
      checkBox = interactible(getWait(), checkBox);
      if (!checkBox.isSelected()) {
        checkBox.click();
      }
    }

    public static void uncheckCheckBox(WebElement checkBox) {
      ElementValidator.validateCheckBox(checkBox);
      checkBox = interactible(getWait(), checkBox);
      if (checkBox.isSelected()) {
        checkBox.click();
      }
    }

    public static void selectRadioButton(WebElement radioButton) {
      ElementValidator.validateRadioButton(radioButton);
      radioButton = interactible(getWait(), radioButton);
      if (!radioButton.isSelected()) {
        radioButton.click();
      }
    }

    public static void selectElements(Iterable<WebElement> elements) {
      Actions actions = new Actions(getDriver());
      actions.keyDown(Keys.CONTROL);
      for (WebElement element : elements) {
        try { actions.click(element); Thread.sleep(50); }
        catch (InterruptedException ie) {}
      }
      actions.keyUp(Keys.CONTROL);
      actions.perform();
    }

    public static void inputText(WebElement textBox, String text) {
      ElementValidator.validateTextInput(textBox);
      textBox = interactible(getWait(), textBox);

      textBox.clear();
      if (!Strings.isNullOrEmpty(text)) {
        textBox.sendKeys(text);
      }
    }

    // Neighboring elements

    public static WebElement getPreviousElementSibling(WebElement element) {
      String script = "return arguments[0].previousElementSibling;";
      WebElement previousSiblingElement =
              (WebElement) getJsExecutor().executeScript(script, element);
      return previousSiblingElement;
    }

    public static WebElement getNextElementSibling(WebElement element) {
      String script = "return arguments[0].nextElementSibling;";
      WebElement nextSiblingElement =
              (WebElement) getJsExecutor().executeScript(script, element);
      return nextSiblingElement;
    }

    public static WebElement getParentElement(WebElement childElement) {
      String script = "return arguments[0].parentElement;";
      WebElement parentElement =
              (WebElement) getJsExecutor().executeScript(script, childElement);
      return parentElement;
    }

    public static WebElement getGrandparentElement(WebElement grandchildElement) {
      return getParentElement(getParentElement(grandchildElement));
    }

    public static WebElement getChildElement(WebElement parentElement, int index) {
      String script = "return arguments[0].children[arguments[1]];";
      Object response = getJsExecutor().executeScript(script, parentElement, index);

      WebElement childElement = null;
      if (response instanceof WebElement) {
        childElement = (WebElement) response;
      }

      if (childElement == null) {
        String message = String.format("Cannot find child element with given index of %d.%n", index);
        logger.error(message);
        throw new NoSuchElementException(message);
      }

      return childElement;
    }

  }

  /**
   * ElementValidator
   *
   * @author ryan131
   * @since Dec 2, 2015, 19:36:35 PM
   */
  static final class ElementValidator {
    private ElementValidator() {}

    private static final boolean THROW_EXCEPTION = Config.Value.WEB_ELEMENT_VALIDATION_EXCEPTION;
    private static final boolean VALIDATE_BUTTONS = // TODO [Basil] Improve the handling
            Config.Value.WEB_ELEMENT_VALIDATION_IGNORED_TYPES.indexOf("button") == -1;

    private static void reportInvalid(WebElement actual, String expected) {
      if (THROW_EXCEPTION) {
        String xpath = XPathUtil.getXPath(actual);
        throw new BasilException.Element.Invalid(
                "The element \"" + xpath + "\" is not a valid \"" + expected + "\".");
      }
    }

    private static void reportInvalidTagName(WebElement element, String expectedTagName) {
      if (THROW_EXCEPTION) {
        String xpath = XPathUtil.getXPath(element);
        throw new BasilException.Element.InvalidTagName(
                "The element \"" + xpath + "\" is not a valid \"" + expectedTagName + "\".");
      }
    }

    public static boolean validateTag(WebElement element, String tag) {
      if (!ElementActions.tagOf(element).equals(tag)) {
        reportInvalidTagName(element, tag);
        return false;
      }
      return true;
    }

    public static boolean validateTagAndClass(WebElement element, String tag, String clazz) {
      boolean isValidTag = validateTag(element, tag);
      if (!ElementActions.classOf(element).contains(clazz)) {
        reportInvalid(element, clazz);
        return isValidTag && false;
      }
      return true;
    }

    public static boolean validateInputType(WebElement input, String type) {
      boolean isInput = validateTag(input, "input");
      if (!ElementActions.typeOf(input).equals(type)) {
        reportInvalid(input, type);
        return isInput && false;
      }
      return true;
    }

    public static boolean validateTextInput(WebElement input) {
      try {
        return validateInputType(input, "text");
      } catch (BasilException.Element.Invalid validatePassword) {}
      try {
        return validateInputType(input, "password");
      } catch (BasilException.Element.Invalid validateTextarea) {}
      try {
        return validateTag(input, "textarea");
      } catch (BasilException.Element.Invalid report) {
        reportInvalid(input, "textual input");
      }
      return false;
    }

    public static boolean validateLink(WebElement link) {
      try {
        return validateTag(link, "a");
      } catch (BasilException.Element.InvalidTagName validateLabel) {}
      try {
        // label can be used as links, See http://stackoverflow.com/a/17964889
        return validateTag(link, "label");
      } catch (BasilException.Element.Invalid report) {
        reportInvalid(link, "link");
      }
      return false;
    }

    public static boolean validateButton(WebElement button) {
      if (VALIDATE_BUTTONS) {
        return validateInputType(button, "button");
      }
      return true;
    }

    public static boolean validateCheckBox(WebElement checkBox) {
      return validateInputType(checkBox, "checkbox");
    }

    public static boolean validateRadioButton(WebElement radioButton) {
      return validateInputType(radioButton, "radio");
    }

  }

  public XPathStatistics statistics() {
    return xpathStatistics;
  }

  public static class XPathStatistics {

    private Map<String, Long> xpathHits = Maps.newHashMap();

    public void recordHit(String xpath) {
      xpathHits.put(xpath, getHit(xpath) + 1);
    }

    public long getHit(String xpath) {
      Long current = xpathHits.get(xpath);
      return current == null ? 0 : current;
    }

  }

  private static final Logger logger = LoggerFactory.getLogger(WebElementService.class);

  private XPathStatistics xpathStatistics;

  WebElementServiceImpl() {
    xpathStatistics = new XPathStatistics();
  }

}
