/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.page;

import static org.basil.Config.PAGE_OBJECT_LOCATE_TIMEOUT;

import org.basil.Config;
import org.basil.selenium.Basil;
import org.basil.selenium.BasilContext;
import org.basil.selenium.BasilElement;
import org.basil.selenium.base.DriverFactory;
import org.basil.selenium.service.WebElementUtil;
import org.basil.selenium.ui.ExtendedConditions;
import org.basil.selenium.ui.Pessimistically;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * PageObject - the base class for writing Basil page objects.
 *
 * @author ryan131
 * @since Oct 22, 2014, 8:34:33 PM
 */
public abstract class PageObject extends PageObjectAnnotation implements SearchContext {

  public static <E extends WebElement> E nullToNoSuch(E element) {
    return nullToNoSuch(element, "unknown element");
  }

  public static <E extends WebElement> E nullToNoSuch(E element, String description) {
    if (element == null) {
      throw new NoSuchElementException("The \"" + description + "\" is not available.");
    }
    return element;
  }

  private static final Logger logger = LoggerFactory.getLogger(PageObject.class);

  private final Timer timer;

  protected WebDriverWait wait;
  protected final Object[] params;

  protected final ElementLookup lookup;
  protected BasilElement pageObject;

  // Constructors

  protected PageObject(SearchContext context, By locator) {
    this(context, locator, 60);
  }

  protected PageObject(SearchContext context, By locator, Object... params) {
    this(context, locator, 60, params);
  }

  protected PageObject(SearchContext context, By locator, long timeout) {
    this(context, locator, timeout, new Object[0]);
  }

  protected PageObject(SearchContext context, By locator, long timeout, Object... params) {
    this.wait = new WebDriverWait(DriverFactory.getWebDriver(), timeout);
    this.params = params;

    this.timer = new Timer(getClass());
    this.lookup = ElementLookup.create(this, wait);

    construct(context, locator);
  }

  protected PageObject(WebElement pageObject) {
    this(pageObject, new Object[0]);
  }

  protected PageObject(WebElement pageObject, Object... params) {
    this.wait = DriverFactory.getWebDriverWait();
    this.params = params;

    this.timer = new Timer(getClass());
    this.lookup = ElementLookup.create(this);

    this.pageObject = BasilElement.create(pageObject);
    if (!initializeOnTheFly()) {
      construct();
    }
  }

  protected PageObject() {
    this(new Object[0]);
  }

  protected PageObject(Object... params) {
    this.wait = DriverFactory.getWebDriverWait();
    this.params = params;

    this.timer = new Timer(getClass());
    this.lookup = ElementLookup.create(this, wait);
  }

  // Initialization mechanism

  protected void construct(SearchContext context, By locator) {
    super.setLocator(locator);
    super.setParent(context);
    if (!initializeOnTheFly()) {
      construct();
    }
  }

  protected void construct() {
    timer.tokenBegin();
    initializePageObject();
    setContext(pageObject);
    timer.tokenEnd();

    timer.elementBegin();
    initializeWebElements();
    timer.elementEnd();

    timer.printTimerMessage();
  }

  public void initialize(SearchContext context, By locator) {
    super.setLocator(locator);
    super.setParent(context);
    initialize();
  }

  /**
   * Locate and WAIT for the PageObject to be VISIBLE.
   */
  protected void initializePageObject() {
    if (pageObject != null) {
      waitUntilVisible();
      return;
    }

    BasilContext parent = getParent();
    Basil locator = getLocator();
    if (locator().hasConfident()) {
      locator = getConfidentLocator();
    }
    WebElement pageObject = null;
    try {
      ElementLookup lookup = ElementLookup.create(parent, PAGE_OBJECT_LOCATE_TIMEOUT);
      if (locator.isConfident()) {
        pageObject = lookup.getVisibleElement(getLocator());
      }
      if (locator.hasXPath() && !parent.isWebDriver()) {
        if (locator.equals(parent.getLocator()) || locator.equals(parent.getConfidentLocator())) {
          // My locator's should not be the same as my parent's locator
          logger.error("The locator \"" + locator + "\" is conflict with it's parent's locator.");
        }
        if (!(locator.equals(parent.getLocator())) &&
            !(locator.equals(parent.getConfidentLocator()) && locatorRegeneration())) {
          // When a confident locator is concatenated with another locator, the concatenated
          // locator is guaranteed to be is a confident locator. For example:
          //
          //     Confident locator: //table[@id='1']
          //     concatenated with: //tr
          //     resulted:          //table[@id='1']//tr
          //
          // The locator produced in the example above is not confident.
          pageObject = lookup.getFirstVisibleElement(locator); //Lookup already does prefix the
          // locator so it is looking up using getParent().getConfidentLocator().concat(locator)
        }
      }
      if (pageObject == null) { // Fail-safe in case previous lookup did not work
        pageObject = lookup.getFirstVisibleElement(locator);
      }
    } catch (TimeoutException te) {
      logger.error(getParent().toString());
      logger.error("PageObject has failed to initialize with locator: " + locator);
      throw new NoSuchElementException("Cannot locate page object by: " + locator);
    }
    this.pageObject = BasilElement.create(pageObject).setParent(parent).setLocator(locator);
  }

  /**
   * Override if there are WebElements you wish to initialize once the PageObject is created.
   */
  protected void initializeWebElements() {
    // Do nothing
  }

  /**
   * Externally controlled initialization when the page object is initialized on-the-fly.
   */
  public void initialize() {
    Preconditions.checkState(initializeOnTheFly(),
        "The current page object \"" + getClassName() + "\" cannot be initialized externally.");
    construct();
  }

  public boolean isInitialized() {
    return pageObject != null;
  }

  public boolean isEnabled() {
    return WebElementUtil.isEnabled(pageObject);
  }

  public boolean isDisabled() {
    return WebElementUtil.isDisabled(pageObject);
  }

  public void waitUntilVisible() {
    wait.until(ExtendedConditions.visibilityOf(pageObject));
  }

  public void waitUntilInvisible() {
    wait.until(ExtendedConditions.invisibilityOf(pageObject));
  }

  @SuppressWarnings("unchecked")
  protected <T> T getParameterByTypeInference() {
    T toReturn = null;
    for (Object param : params) {
      try {
        toReturn = (T) param;
        return toReturn;
      } catch (ClassCastException cce) {
        continue;
      }
    }
    if (toReturn == null) {
      throw new ClassCastException("No desired argument type found.");
    }
    return toReturn;
  }

  protected void clickAndExitPageObject(WebElement button) {
//    WebElementUtil.clickButton(button);
    Pessimistically.click(button);
    waitUntilInvisible();
  }

  private class Timer {

    private String name;

    private long tokenBegin;
    private long tokenUsed;
    private long elementBegin;
    private long elementUsed;

    private Timer(Class<?> clazz) {
//      Class<?> superClazz = clazz.getSuperclass();
//      if (superClazz.equals(PageObject.class)) {
//        name = clazz.getSimpleName();
//      } else {
//        name = superClazz.getSimpleName() + "." + clazz.getSimpleName();
//      }
      // Exclude any type of parent PageObject prefix
      String superSimpleName = clazz.getSuperclass().getSimpleName();
      if (superSimpleName.endsWith("PageObject")) {
        name = clazz.getSimpleName();
      } else {
        name = superSimpleName + "." + clazz.getSimpleName();
      }
    }

    private void tokenBegin() {
      if (tokenBegin == 0) {
        tokenBegin = System.currentTimeMillis();
      } else {
        logger.warn("Token timer already begun");
      }
    }

    private void tokenEnd() {
      if (tokenUsed == 0) {
        tokenUsed = System.currentTimeMillis() - tokenBegin;
      } else {
        logger.warn("Token timer already ended");
      }
    }

    private void elementBegin() {
      if (elementBegin == 0) {
        elementBegin = System.currentTimeMillis();
      } else {
        logger.warn("Element timer already begun");
      }
    }

    private void elementEnd() {
      if (elementUsed == 0) {
        elementUsed = System.currentTimeMillis() - elementBegin;
      } else {
        logger.warn("Element timer already ended");
      }
    }

    public void printTimerMessage() {
      long totalUsed = tokenUsed + elementUsed;
      StringBuilder message = new StringBuilder();

      message.append(String.format("[%s @ %s] initialized", name, pageObject.getId()));
      if (Config.PAGE_OBJECT_TIMER_31S) {
        try {
          Thread.sleep(31);
          totalUsed += 31;
        } catch (InterruptedException ie) {}
      }
      if (Config.PAGE_OBJECT_TIMER_STYLE.equals(TimerStyle.CONCISE)) {
        message.append(String.format(" in %d ms", totalUsed));
      }
      if (Config.PAGE_OBJECT_TIMER_STYLE.equals(TimerStyle.VERBOSE)) {
        message.append(String.format(
            " in %d ms (token: %d ms elements: %d ms", totalUsed, tokenUsed, elementUsed));
        if (Config.PAGE_OBJECT_TIMER_31S) {
          message.append(" idle: 31 ms");
        }
        message.append(")");
      }

      logger.info(message.toString());
      DriverFactory.totalWaitedTime += totalUsed;
    }

  }

  public enum TimerStyle {

    CONCISE,
    VERBOSE;

    public static TimerStyle fromText(String text) {
      for (TimerStyle style : values()) {
        if (text.equalsIgnoreCase(style.name())) {
          return style;
        }
      }
      throw new IllegalArgumentException(text);
    }

  }

}
