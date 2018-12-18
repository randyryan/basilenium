/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.service;

import java.util.List;
import java.util.Map;

import org.basil.Config;
import org.basil.selenium.BasilElement;
import org.basil.selenium.BasilException;
import org.basil.selenium.page.PageObject;
import org.basil.selenium.ui.ExtendedConditions;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spearmint.util.Sleeper;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
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

  // -------------------------------------------------------------------------------------------- //
  //                                                                                              //
  //     WebElement Standard Services                                                             //
  //     ----------------------------                                                             //
  //     Services for methods declared in the WebElement interface and some of the most common    //
  //     usages and derived                                                                       //
  //                                                                                              //
  // -------------------------------------------------------------------------------------------- //

  // WebElement Click Services

  @Override
  public void click(WebElement element) {
    element.click();
  }

  @Override
  public void click(WebElement element, Clicker clicker) {
    clicker.click(element);
  }

  @Override
  public void clickByJs(WebElement element) {
    // TODO Use not ServiceContext to be stateless
    click(element, Clicker.javascript(ServiceContext.getJsExecutor()));
  }

  @Override
  public void clickByJs(WebElement element, JavascriptExecutor executor) {
    click(element, Clicker.javascript(executor));
  }

  @Override
  public void clickByActions(WebElement element) {
    // TODO Use not ServiceContext to be stateless
    click(element, Clicker.actions(ServiceContext.getDriver()));
  }

  @Override
  public void clickByActionsHover(WebElement element) {
    // TODO Use not ServiceContext to be stateless
    click(element, Clicker.actionsHover(ServiceContext.getDriver()));
  }

  @Override
  public void clickLink(WebElement link) {
    click(link, Clicker.link());
  }

  @Override
  public void clickButton(WebElement button) {
    click(button, Clicker.button());
  }

  @Deprecated
  @Override
  public void checkCheckBox(WebElement checkBox) {
    validate(checkBox, ValidationRule.isInputCheckBox());
//  while (!isSelected(checkBox)) {
//    Sleeper.sleep_100_ms();
//    click(checkBox);
//  }
    selectElement(checkBox);
  }

  @Deprecated
  @Override
  public void uncheckCheckBox(WebElement checkBox) {
    validate(checkBox, ValidationRule.isInputCheckBox());
//  while (!isSelected(checkBox)) {
//    Sleeper.sleep_100_ms();
//    click(checkBox);
//  }
    click(checkBox, Clicker.satisfies(ExtendedConditions.elementToBeUnselected(checkBox)));
  }

  @Override
  public void selectRadioButton(WebElement radioButton) {
    validate(radioButton, ValidationRule.isInputRadioBox());
//  while (!isSelected(radioButton)) {
//    Sleeper.sleep_100_ms();
//    click(radioButton);
//  }
    selectElement(radioButton);
  }

  @Override
  public void selectElement(WebElement element) {
    click(element, Clicker.satisfies(ExpectedConditions.elementToBeSelected(element)));
  }

  @Override
  public void selectElements(Iterable<WebElement> elements) {
    Actions actions = new Actions(ServiceContext.getDriver());
    actions.keyDown(Keys.CONTROL);
    for (WebElement element : elements) {
      selectElement(element);
      Sleeper.sleep_50_ms();
    }
    actions.keyUp(Keys.CONTROL);
    actions.perform();
  }

  @Override
  public void unselectElement(WebElement element) {
    click(element, Clicker.satisfies(ExtendedConditions.elementToBeUnselected(element)));
  }

  @Override
  public void unselectElements(Iterable<WebElement> elements) {
    Actions actions = new Actions(ServiceContext.getDriver());
    actions.keyDown(Keys.CONTROL);
    for (WebElement element : elements) {
      unselectElement(element);
      Sleeper.sleep_50_ms();
    }
    actions.keyUp(Keys.CONTROL);
    actions.perform();
  }

  @Override
  public void submit(WebElement element) {
    element.submit();
  }

  @Override
  public void sendKeys(WebElement element, CharSequence... keysToSend) {
    element.sendKeys(keysToSend);
  }

  @Override
  public void clear(WebElement element) {
    element.clear();
  }

  @Override
  public String getTagName(WebElement element) {
    return element.getTagName();
  }

  @Deprecated
  @Override
  public String getTag(WebElement element) {
    return getTagName(element);
  }

  // WebElement Attribute Services

  @Override
  public String getAttribute(WebElement element, String name) {
    return element.getAttribute(name);
  }

  @Override
  public boolean hasAttribute(WebElement element, String attributeName) {
    return !Strings.isNullOrEmpty(element.getAttribute(attributeName));
  }

  @Override
  @Deprecated
  public String getAttr(WebElement element, String attrName) {
    return getAttribute(element, attrName);
  }

  @Override
  @Deprecated
  public boolean hasAttr(WebElement element, String attrName) {
    return hasAttribute(element, attrName);
  }

  @Override
  public String getId(WebElement element) {
    return element.getAttribute("id");
  }

  @Override
  public boolean hasId(WebElement element) {
    return hasAttribute(element, "id");
  }

  @Override
  public String getClass(WebElement element) {
    return element.getAttribute("class");
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
    return hasAttribute(element, "title");
  }

  @Override
  public String getType(WebElement element) {
    return element.getAttribute("type");
  }

  @Override
  public boolean hasType(WebElement element) {
    return hasAttribute(element, "type");
  }

  @Override
  public ValueConverter getValue(WebElement element) {
    final String value = Strings.nullToEmpty(element.getAttribute("value"));
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

  @Override
  public String getInnerHTML(WebElement element) {
    return element.getAttribute("innerHTML");
  }

  @Override
  public String getOuterHTML(WebElement element) {
    return element.getAttribute("outerHTML");
  }

  @Override
  public boolean isSelected(WebElement element) {
    return element.isSelected();
  }

  // WebElement Interactability Services

  @Override
  public boolean isEnabled(WebElement element) {
    if (Config.WEB_ELEMENT_ENABLE_LATENCY > 0) {
      Sleeper.sleepSilently(Config.WEB_ELEMENT_ENABLE_LATENCY);
    }
    return isDisplayed(element) && element.isEnabled() && !isDisabled(element);
  }

  @Override
  public boolean isDisabled(WebElement element) {
    // This is a method not in the WebElement interface, so it is better to offer
    // something different rather than simply negates WebElement#isEnabled()
    return !hasClass(element, "disabled") && !hasAttribute(element, "disabled");
  }

  @Override
  public boolean isReadonly(WebElement element) {
    return hasAttribute(element, "readonly");
  }

  @Override
  public boolean isInteractible(WebElement element) {
    return !isDisabled(element) && !isReadonly(element);
  }

  @Deprecated
  @Override
  public WebElement getInteractible(WebDriverWait wait, WebElement element) {
    return wait.until(ExtendedConditions.elementToBeInteractible(element));
  }

  // WebElement Text Services

  @Override
  public String getText(WebElement element) {
    return element.getText();
  }

  @Override
  public List<String> getText(List<WebElement> elements) {
    List<String> texts = Lists.newArrayListWithCapacity(elements.size());
    for (WebElement element : elements) {
      texts.add(getText(element));
    }
    return texts;
  }

  @Deprecated
  @Override
  public void inputText(WebElement element, String text) {
    validate(element, ValidationRule.isTextualInput());
    clear(element);
    if (!Strings.isNullOrEmpty(text)) {
      sendKeys(element);
    }
  }

  @Override
  public boolean isDisplayed(WebElement element) {
    return element.isDisplayed();
  }

  @Override
  public Point getLocation(WebElement element) {
    return element.getLocation();
  }

  @Override
  public Dimension getSize(WebElement element) {
    return element.getSize();
  }

  @Override
  public Rectangle getRect(WebElement element) {
    return element.getRect();
  }

  @Override
  public String getCssValue(WebElement element, String propertyName) {
    return element.getCssValue(propertyName);
  }

  // WebElement Property Services

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

  // WebElement Validation Services

  public WebElement validate(final WebElement element, ValidationRule rule) {
    if (!Config.WEB_ELEMENT_VALIDATION_ENABLED) {
      return element;
    }
    if (Config.WEB_ELEMENT_VALIDATION_IGNORED_TYPES.contains(rule.getType())) {
      return element;
    }
    BasilException.InvalidElement validationException = rule.apply(element);
    if (validationException != null) {
      String message = "Element: " + element + " did not succeed validation: " +
          validationException.getMessage();
      if (Config.WEB_ELEMENT_VALIDATION_EXCEPTION) {
        throw new BasilException.InvalidElement(message, validationException);
      } else {
        logger.warn(message);
      }
    }
    return element;
  }

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
   * @since Oct 11, 2013, 7:00:18 PM
   */
  static final class ElementActions extends ServiceContext {
    private ElementActions() {}

    // Property

    @Deprecated
    public static String attrOf(WebElement element, String attribute) {
      return WebElementUtil.getAttribute(element, attribute);
    }

    @Deprecated
    public static String idOf(WebElement element) {
      return WebElementUtil.getId(element);
    }

    @Deprecated
    public static String classOf(WebElement element) {
      return WebElementUtil.getClass(element);
    }

    @Deprecated
    public static String typeOf(WebElement element) {
      return WebElementUtil.getType(element);
    }

    @Deprecated
    public static String valueOf(WebElement element) {
      return WebElementUtil.getValue(element).toString();
    }

    @Deprecated
    public static ValueConverter value(WebElement element) {
      return WebElementUtil.getValue(element);
    }

    @Deprecated
    public static String tagOf(WebElement element) {
      return WebElementUtil.getTagName(element);
    }

    @Deprecated
    public static String textOf(WebElement element) {
      return WebElementUtil.getText(element);
    }

    @Deprecated
    public static List<String> textOf(List<WebElement> elements) {
      return WebElementUtil.getText(elements);
    }

    @Deprecated
    public static String xpathOf(WebElement element) {
      return XPathUtil.getXPath(element);
    }

    @Deprecated
    public static String xpathOf(WebElement element, String xpathExpression) {
      return XPathUtil.getXPath(element, xpathExpression);
    }

    @Deprecated
    public static boolean isEnabled(WebElement element) {
      return WebElementUtil.isEnabled(element);
    }

    @Deprecated
    public static boolean isDisabled(WebElement element) {
      return WebElementUtil.isDisabled(element);
    }

    @Deprecated
    public static WebElement interactible(WebDriverWait wait, WebElement element) {
      return WebElementUtil.getInteractible(wait, element);
    }

    // Interaction

    @Deprecated
    public static void click(WebElement element) {
      WebElementUtil.click(element);
    }

    @Deprecated
    public static void jsClick(WebElement element) {
      WebElementUtil.clickByJs(element);
    }

    @Deprecated
    public static void jsClick(JavascriptExecutor executor, WebElement element) {
      WebElementUtil.clickByJs(executor, element);
    }

    @Deprecated
    public static void actionsClick(WebElement element) {
      WebElementUtil.clickByActions(element);
    }

    @Deprecated
    public static void actionsHoverClick(WebElement element) {
      WebElementUtil.clickByActionsHover(element);
    }

    @Deprecated
    public static void clickLink(WebElement link) {
      ElementValidator.validateLink(link);
      link = interactible(getWait(), link);
      link.click();
    }

    @Deprecated
    public static void clickButton(WebElement button) {
      ElementValidator.validateButton(button);
      button = interactible(getWait(), button);
      button.click();
    }

    @Deprecated
    public static void checkCheckBox(WebElement checkBox) {
      ElementValidator.validateCheckBox(checkBox);
      checkBox = interactible(getWait(), checkBox);
      if (!checkBox.isSelected()) {
        checkBox.click();
      }
    }

    @Deprecated
    public static void uncheckCheckBox(WebElement checkBox) {
      ElementValidator.validateCheckBox(checkBox);
      checkBox = interactible(getWait(), checkBox);
      if (checkBox.isSelected()) {
        checkBox.click();
      }
    }

    @Deprecated
    public static void selectRadioButton(WebElement radioButton) {
      ElementValidator.validateRadioButton(radioButton);
      radioButton = interactible(getWait(), radioButton);
      if (!radioButton.isSelected()) {
        radioButton.click();
      }
    }

    @Deprecated
    public static void selectElements(Iterable<WebElement> elements) {
      WebElementUtil.selectElements(elements);
    }

    @Deprecated
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
   * @deprecated Use {@link WebElementService#validate(WebElement, ValidationRule)}
   */
  @Deprecated
  static final class ElementValidator {
    ElementValidator() {}

    private static void reportInvalid(WebElement actual, String expected) {
      if (ENABLED && THROW_EXCEPTION) {
        String xpath = XPathUtil.getXPath(actual);
        throw new BasilException.InvalidElement(
                "The element \"" + xpath + "\" is not a valid \"" + expected + "\".");
      }
    }

    private static void reportInvalidTagName(WebElement element, String expectedTagName) {
      if (ENABLED && THROW_EXCEPTION) {
        String xpath = XPathUtil.getXPath(element);
        throw new BasilException.InvalidTagName(
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
      } catch (BasilException.InvalidElement validatePassword) {}
      try {
        return validateInputType(input, "password");
      } catch (BasilException.InvalidElement validateTextarea) {}
      try {
        return validateTag(input, "textarea");
      } catch (BasilException.InvalidElement report) {
        reportInvalid(input, "textual input");
      }
      return false;
    }

    public static boolean validateLink(WebElement link) {
      try {
        return validateTag(link, "a");
      } catch (BasilException.InvalidTagName validateLabel) {}
      try {
        // label can be used as links, See http://stackoverflow.com/a/17964889
        return validateTag(link, "label");
      } catch (BasilException.InvalidElement report) {
        reportInvalid(link, "link");
      }
      return false;
    }

    public static boolean validateButton(WebElement button) {
      if (!Config.WEB_ELEMENT_VALIDATION_IGNORED_TYPES.contains("button")) {
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

    private static final boolean ENABLED = Config.WEB_ELEMENT_VALIDATION_ENABLED;
    private static final boolean THROW_EXCEPTION = Config.WEB_ELEMENT_VALIDATION_EXCEPTION;

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
