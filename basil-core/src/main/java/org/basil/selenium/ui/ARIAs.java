/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.ui;

import static org.basil.selenium.ui.ExtendedConditions.attributeValueToPresentInElement;

import org.basil.selenium.service.ServiceContext;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ARIA (Accessible Rich Internet Applications) Utility. For more information on ARIA,
 * visit: <a href="https://www.w3.org/TR/wai-aria/">https://www.w3.org/TR/wai-aria/</a>.
 *
 * @author ryan131
 * @since May 26, 2016, 4:37:17 PM
 */
public final class ARIAs extends ServiceContext {
  private ARIAs() {}

  private static final Logger logger = LoggerFactory.getLogger(ARIAs.class);

  public static String attributeValue(WebDriver driver, WebElement element, Attribute attribute) {
    return ExtendedConditions.attributeToBePresentInElement(element, attribute.attribute()).apply(driver);
  }

  public static boolean verify(WebDriver driver, WebElement element, Attribute attribute) {
    return attributeValueToPresentInElement(element, attribute.attribute(), "true").apply(driver);
  }

  @Deprecated
  public static boolean isAriaChecked(WebElement element) {
    return verify(getDriver(), element, Attribute.CHECKED);
  }

  @Deprecated
  public static boolean isAriaExpanded(WebElement element) {
    return verify(getDriver(), element, Attribute.EXPANDED);
  }

  @Deprecated
  public static boolean isAriaHaspopup(WebElement element) {
    return verify(getDriver(), element, Attribute.HASPOPUP);
  }

  @Deprecated
  public static boolean isAriaSelected(WebElement element) {
    return verify(getDriver(), element, Attribute.SELECTED);
  }

  /**
   * Returns the target element's ID provided by aria-owns attribute.
   *
   * @param element bearing the aria-owns attribute
   * @param associatedElement to be clicked to make aria-owns attribute
   * @return the target element's ID provided by aria-owns attribute.
   */
  public static String getAriaOwnsWebELementId(WebElement element, WebElement associatedElement) {
    String ariaOwnsId = null;

    try {
      associatedElement.click(); // A click to ensure the "aria-owns" attribute to appear in the element
      ariaOwnsId =
        getWait().until(ExtendedConditions.attributeToBePresentInElement(element, "aria-owns"));
    } catch (TimeoutException te) {
      logger.warn("Cannot retrieve aria-owns attribute from passed in element.");
    } finally {
//      associatedElement.click(); // A click to kind of restore the element to its original status
    }

    return ariaOwnsId;
  }

  public static WebElement getAriaOwnsWebElement(WebElement element) {
    return getAriaOwnsWebElement(element, element);
  }

  public static WebElement getAriaOwnsWebElement(WebElement element, WebElement associatedElement) {
    return getDriver().findElement(By.id(getAriaOwnsWebELementId(element, associatedElement)));
  }

  /**
   * Supports of aria- attributes
   * See https://www.w3.org/TR/wai-aria/states_and_properties#state_prop_def
   *
   * @author ryan131
   * @since Mar 10, 2014, 7:17:37 PM
   */
  public enum Attribute {

    ACTIVEDESCENDANT("aria-activedescendant"), ATOMIC("aria-atomic"), AUTOCOMPLETE("aria-autocomplete"),
    BUSY("aria-busy"),
    CHECKED("aria-checked"), CONTROLS("aria-controls"),
    DESCRIBEDBY("aria-describedby"), DISABLED("aria-disabled"), DROPEFFECT("aria-dropeffect"),
    EXPANDED("aria-expanded"),
    FLOWTO("aria-flowto"),
    GRABBED("aria-grabbed"),
    HASPOPUP("aria-haspopup"), HIDDEN("aria-hidden"),
    INVALID("aria-invalid"),
    LABEL("aria-label"), LABELLEDBY("aria-labelledby"), LEVEL("aria-level"), LIVE("aria-live"),
    MULTILINE("aria-multiline"), MULTISELECTABLE("aria-multiselectable"),
    ORIENTATION("aria-orientation"), OWNS("aria-owns"),
    POSINSET("aria-posinset"), PRESSED("aria-pressed"),
    READONLY("aria-readonly"), RELEVANT("aria-relevant"), REQUIRED("aria-required"),
    SELECTED("aria-selected"), SETSIZE("aria-setsize"), SORT("aria-sort"),
    VALUEMAX("aria-valuemax"), VALUEMIN("aria-valuemin"), VALUENOW("aria-valuenow"), VALUETEXT("aria-valuetext");

    private String attribute;

    private Attribute(String attribute) {
      this.attribute = attribute;
    }

    public String attribute() {
      return attribute;
    }

    /**
     * https://www.w3.org/TR/wai-aria/terms#def_property
     */
    public boolean isProperty() {
      return !isState();
    }

    /**
     * https://www.w3.org/TR/wai-aria/terms#def_state
     */
    public boolean isState() {
      return equals(BUSY) || equals(CHECKED) || equals(DISABLED) ||
          equals(EXPANDED) || equals(GRABBED) || equals(HIDDEN) ||
          equals(INVALID) || equals(PRESSED) || equals(SELECTED);
    }

  }

}
