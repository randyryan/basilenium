/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.ui;

import java.util.List;

import org.basil.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * A superset of the <code>ExpectedConditions</code>.
 *
 * @author ryan131
 * @since Oct 18, 2015, 4:55:16 PM
 */
public final class ExtendedConditions {
  private ExtendedConditions() {}

  /**
   * Helper method for By based conditions to utilize.
   */
  private static WebElement findElement(SearchContext searchContext, By locator) {
    return searchContext.findElement(locator);
  }

  /**
   * Helper method for By based conditions to utilize.
   */
  @SuppressWarnings("unused")
  private static List<WebElement> findElements(SearchContext searchContext, By locator) {
    return searchContext.findElements(locator);
  }

  // -------------------------------------------------------------------------------------------- //
  // ----------                                  ARIA                                  ---------- //
  // -------------------------------------------------------------------------------------------- //

  public static ExpectedCondition<Boolean> ariaStateToBeTrue(
      final WebElement element, final ARIAs.Attribute ariaAttribute) {
    ExpectedCondition<Boolean> ec = new ExpectedCondition<Boolean>() {
      public ExpectedCondition<String> attributeToBePresentInElement =
          attributeToBePresentInElement(element, ariaAttribute.attribute());

      @Override
      public Boolean apply(WebDriver driver) {
        if (ariaAttribute.isProperty()) {
          throw new IllegalArgumentException(ariaAttribute.attribute());
        }
        return attributeToBePresentInElement.apply(driver).equals("true");
      }

      @Override
      public String toString() {
        return "";
      }
    };

    return ec;
  }

  public static ExpectedCondition<Boolean> ariaStateToBeFalse(
      final WebElement element, final ARIAs.Attribute ariaAttribute) {
    ExpectedCondition<Boolean> ec = new ExpectedCondition<Boolean>() {
      public ExpectedCondition<String> attributeToBePresentInElement =
          attributeToBePresentInElement(element, ariaAttribute.attribute());

      @Override
      public Boolean apply(WebDriver driver) {
        if (ariaAttribute.isProperty()) {
          throw new IllegalArgumentException(ariaAttribute.attribute());
        }
        return attributeToBePresentInElement.apply(driver).equals("false");
      }

      @Override
      public String toString() {
        return "";
      }
    };

    return ec;
  }

  // -------------------------------------------------------------------------------------------- //
  // ----------                                Attribute                               ---------- //
  // -------------------------------------------------------------------------------------------- //

  /**
   * An expectation for checking if the given attribute is present in the element located
   * by the specified locator.
   *
   * @param locator to find the WebElement
   * @param attribute to be present in the element
   * @return true once the element contains the given attribute
   * @since Selenium WebDriver 2.52.0
   */
  public static ExpectedCondition<Boolean> attributeToPresentInElementLocated(
      final By locator, final String attribute) {
    ExpectedCondition<Boolean> ec = new ExpectedCondition<Boolean>() {
      public ExpectedCondition<String> attributeToBePresentInElementLocated =
          attributeToBePresentInElementLocated(locator, attribute);

      @Override
      public Boolean apply(WebDriver driver) {
        return attributeToBePresentInElementLocated.apply(driver) != null;
      }

      @Override
      public String toString() {
        return String.format(
            "attribute ('%s') to be present in element located by %s", attribute, locator);
      }
    };

    return ec;
  }

  /**
   * An expectation for checking if the given attribute is present in the specified element.
   *
   * @param element the WebElement
   * @param attribute to be present in the element
   * @return true once the element contains the given attribute
   * @since Selenium WebDriver 2.48.0
   */
  public static ExpectedCondition<Boolean> attributeToPresentInElement(
      final WebElement element, final String attribute) {
    ExpectedCondition<Boolean> ec = new ExpectedCondition<Boolean>() {
      public ExpectedCondition<String> attributeToBePresentInElement =
          attributeToBePresentInElement(element, attribute);

      @Override
      public Boolean apply(WebDriver driver) {
        return attributeToBePresentInElement.apply(driver) != null;
      }

      @Override
      public String toString() {
        return String.format("attribute ('%s') to be present in element %s", attribute, element);
      }
    };

    return ec;
  }

  /**
   * An expectation for checking if the given attribute is present in the element located
   * by the specified locator.
   *
   * @param locator to find the WebElement
   * @param attribute to be present in the element
   * @return value of attribute once the element contains the given attribute
   * @since Selenium WebDriver 2.52.0
   */
  public static ExpectedCondition<String> attributeToBePresentInElementLocated(
      final By locator, final String attribute) {
    ExpectedCondition<String> ec = new ExpectedCondition<String>() {
      @Override
      public String apply(WebDriver driver) {
        String attributeValue = findElement(driver, locator).getAttribute(attribute);
        if (attributeValue != null) {
          return attributeValue;
        } else {
          return null;
        }
      }

      @Override
      public String toString() {
        return String.format(
            "attribute ('%s') to be present in element located by %s" , attribute, locator);
      }
    };

    return ec;
  }

  /**
   * An expectation for checking if the given attribute is present in the specified element.
   *
   * @param element the WebElement
   * @param attribute to be present in the element
   * @return value of attribute once the element contains the given attribute
   * @since Selenium WebDriver 2.48.0
   */
  public static ExpectedCondition<String> attributeToBePresentInElement(
      final WebElement element, final String attribute) {
    ExpectedCondition<String> ec = new ExpectedCondition<String>() {
      @Override
      public String apply(WebDriver driver) {
        String attributeValue = element.getAttribute(attribute);
        if (attributeValue != null) {
          return attributeValue;
        } else {
          return null;
        }
      }

      @Override
      public String toString() {
        return String.format("attribute ('%s') to be present in element %s", attribute, element);
      }
    };

    return ec;
  }

  /**
   * An expectation for checking if the given attribute value is present in the element located
   * by the specified locator.
   *
   * @param locator to find the WebElement
   * @param attribute to check the given attribute value in
   * @param value to be present in the given attribute
   * @return true once the element contains the given attribute and value
   * @since Selenium WebDriver 2.52.0
   */
  public static ExpectedCondition<Boolean> attributeValueToPresentInElementLocated(
      final By locator, final String attribute, final String value) {
    ExpectedCondition<Boolean> ec = new ExpectedCondition<Boolean>() {
      public ExpectedCondition<String> attributeToBePresentInElementLocated =
          attributeToBePresentInElementLocated(locator, attribute);

      @Override
      public Boolean apply(WebDriver driver) {
        String attributeValue = attributeToBePresentInElementLocated.apply(driver);
        if (attributeValue != null && attributeValue.contains(value)) {
          return true;
        } else {
          return false;
        }
      }

      @Override
      public String toString() {
        return String.format(
            "attribute ('%s=%s') to be present in element located by %s"
            , attribute, value, locator);
      }
    };

    return ec;
  }

  /**
   * An expectation for checking if the given attribute value is present in the specified element.
   *
   * @param element the WebElement
   * @param attribute to check the given attribute value in
   * @param value to be present in the given attribute
   * @return true once the element contains the given attribute and value
   * @since Selenium WebDriver 2.48.0
   */
  public static ExpectedCondition<Boolean> attributeValueToPresentInElement(
      final WebElement element, final String attribute, final String value) {
    ExpectedCondition<Boolean> ec = new ExpectedCondition<Boolean>() {
      public ExpectedCondition<String> attributeToBePresentInElement =
          attributeToBePresentInElement(element, attribute);

      @Override
      public Boolean apply(WebDriver driver) {
        String attributeValue = attributeToBePresentInElement.apply(driver);
        if (attributeValue != null && attributeValue.contains(value)) {
          return true;
        } else {
          return false;
        }
      }

      @Override
      public String toString() {
        return String.format(
            "attribute ('%s=%s') to be present in element %s"
            , attribute, value, element);
      }
    };

    return ec;
  }

  /**
   * An expectation for checking if the given attribute value is absent in the element located
   * by the specified locator.
   *
   * @param locator to find the WebElement
   * @param attribute to check the given attribute value in
   * @param value to be present in the given attribute
   * @return true once the given attribute and value are absent from the element
   * @since Selenium WebDriver 2.52.0
   */
  public static ExpectedCondition<Boolean> attributeValueToAbsentInElementLocated(
      final By locator, final String attribute, final String value) {
    ExpectedCondition<Boolean> ec = new ExpectedCondition<Boolean>() {
      public ExpectedCondition<String> attributeToBePresentInElementLocated =
          attributeToBePresentInElementLocated(locator, attribute);

      @Override
      public Boolean apply(WebDriver driver) {
        String attributeValue = attributeToBePresentInElementLocated.apply(driver);
        if (attributeValue == null || !attributeValue.contains(value)) {
          return true;
        } else {
          return false;
        }
      }

      @Override
      public String toString() {
        return String.format(
            "attribute ('%s=%s') to be unpresent in element located by %s"
            , attribute, value, locator);
      }
    };

    return ec;
  }

  /**
   * An expectation for checking if the given attribute value is absent in the specified element.
   *
   * @param element the WebElement
   * @param attribute to check the given attribute value in
   * @param value to be present in the given attribute
   * @return true once the given attribute and value are absent from the element
   * @since Selenium WebDriver 2.48.0
   */
  public static ExpectedCondition<Boolean> attributeValueToAbsentInElement(
      final WebElement element, final String attribute, final String value) {
    ExpectedCondition<Boolean> ec = new ExpectedCondition<Boolean>() {
      public ExpectedCondition<String> attributeToBePresentInElement =
          attributeToBePresentInElement(element, attribute);

      @Override
      public Boolean apply(WebDriver driver) {
        String attributeValue = attributeToBePresentInElement.apply(driver);
        if (attributeValue == null || !attributeValue.contains(value)) {
          return true;
        } else {
          return false;
        }
      }

      @Override
      public String toString() {
        return String.format(
            "attribute ('%s=%s') to be unpresent in element %s"
            , attribute, value, element);
      }
    };

    return ec;
  }

  /**
   * An expectation for checking if a text is present in the specified element.
   *
   * @param element the WebElement
   * @return the text presented in the element
   */
  public static ExpectedCondition<String> textToPresentInElement(final WebElement element) {
    return new ExpectedCondition<String>() {
      @Override
      public String apply(WebDriver driver) {
        try {
          String elementText = element.getText();
          if (elementText.equals("")) {
            return null;
          } else {
            return elementText;
          }
        } catch (StaleElementReferenceException sere) {
          return null;
        }
      }

      @Override
      public String toString() {
        return String.format("text to present in element %s", element);
      }
    };
  }

  public static ExtendedCondition<String> textToAbsentInElement(
      final WebElement element, final String text) {
    return new ExtendedCondition<String>() {
      @Override
      public String apply(SearchContext input) {
        try {
          String elementText = element.getText();
          if (elementText.contains(text)) {
            return null;
          } else {
            return elementText;
          }
        } catch (StaleElementReferenceException sere) {
          return "";
        }
      }
      @Override
      public String toString() {
        return String.format("text to absent in element %s", element);
      }
    };
  }

  // -------------------------------------------------------------------------------------------- //
  // ----------                             Interactibility                            ---------- //
  // -------------------------------------------------------------------------------------------- //

  /**
   * Interactibility Preconditions
   *
   * @author ryan131
   * @since Mar 13, 2014, 2:41:00 PM
   */
  public enum Precondition {

    PRESENT(),
    VISIBLE(),
    CLICKABLE();

    public ExpectedCondition<WebElement> get(final By locator) {
      if (this.equals(PRESENT)) {
        return ExpectedConditions.presenceOfElementLocated(locator);
      }
      if (this.equals(VISIBLE)) {
        return ExpectedConditions.visibilityOfElementLocated(locator);
      }
      if (this.equals(CLICKABLE)) {
        return ExpectedConditions.elementToBeClickable(locator);
      }
      throw new UnsupportedOperationException("Unsupported Precondition.");
    }

    public ExpectedCondition<WebElement> get(final WebElement element) {
      if (this.equals(PRESENT)) {
        return ExtendedConditions.presenceOf(element);
      }
      if (this.equals(VISIBLE)) {
        return ExpectedConditions.visibilityOf(element);
      }
      if (this.equals(CLICKABLE)) {
        return ExpectedConditions.elementToBeClickable(element);
      }
      throw new UnsupportedOperationException("Unsupported Precondition.");
    }

    @Override
    public String toString() {
      if (this.equals(PRESENT)) {
        return "Interactibility precondition: \"presenceOf\"";
      }
      if (this.equals(VISIBLE)) {
        return "Interactibility precondition: \"visibilityOf\"";
      }
      if (this.equals(CLICKABLE)) {
        return "Interactibility precondition: \"elementToBeClickable\"";
      }
      throw new UnsupportedOperationException("Unsupported Precondition.");
    }

    public static Precondition fromString(String string) {
      if (string.equalsIgnoreCase("present")) {
        return PRESENT;
      }
      if (string.equalsIgnoreCase("visible")) {
        return VISIBLE;
      }
      if (string.equalsIgnoreCase("clickable")) {
        return CLICKABLE;
      }
      throw new IllegalArgumentException("Unable to parse precondition from: \"" + string + "\".");
    }
  }

  /**
   * An expectation for checking an element is interactible based on supported precondition.
   *
   * @param locator used to find the element
   * @return the WebElement once it is located and interactible
   * @see Precondition
   * @since Selenium WebDriver 2.48.0
   */
  public static ExpectedCondition<WebElement> elementToBeInteractible(final By locator) {
    ExpectedCondition<WebElement> ec = new ExpectedCondition<WebElement>() {
      public ExpectedCondition<WebElement> expectedPrecondition =
          Config.Value.WEB_ELEMENT_INTERACTIBILITY_PRECONDITION.get(locator);

      @Override
      public WebElement apply(WebDriver driver) {
        try {
          WebElement element = expectedPrecondition.apply(driver);
          if (element != null &&
              attributeValueToAbsentInElement(element, "class", "isabled").apply(driver)) {
            return element;
          }
        } catch (WebDriverException preconditionNotSatisfied) {
        }
        return null;
      }

      @Override
      public String toString() {
        return "element to be interactible: " + locator;
      }
    };

    return ec;
  }

  /**
   * An expectation for checking an element is interactible based on supported precondition.
   *
   * @param element the WebElement
   * @return the (same) WebElement once it is interactible
   * @see Precondition
   * @since Selenium WebDriver 2.48.0
   */
  public static ExpectedCondition<WebElement> elementToBeInteractible(final WebElement element) {
    ExpectedCondition<WebElement> ec = new ExpectedCondition<WebElement>() {
      public ExpectedCondition<WebElement> expectedPrecondition =
          Config.Value.WEB_ELEMENT_INTERACTIBILITY_PRECONDITION.get(element);

      @Override
      public WebElement apply(WebDriver driver) {
        try {
          WebElement element = expectedPrecondition.apply(driver);
          if (element != null &&
              attributeValueToAbsentInElement(element, "class", "isabled").apply(driver)) {
            return element;
          }
        } catch (WebDriverException preconditionNotSatisfied) {
        }
        return null;
      }

      @Override
      public String toString() {
        return "element to be interactible: " + element;
      }
    };

    return ec;
  }

  public static ExpectedCondition<WebElement> elementToBeStaledThenLocated(
      final WebElement element, final By locator) {
    return new ExpectedCondition<WebElement>() {
      public ExpectedCondition<Boolean> stalenessOf = ExpectedConditions.stalenessOf(element);
      public ExpectedCondition<WebElement> visibilityOfElementLocated =
          ExpectedConditions.visibilityOfElementLocated(locator);

      @Override
      public WebElement apply(WebDriver driver) {
        if (stalenessOf.apply(driver)) {
          return visibilityOfElementLocated.apply(driver);
        }
        return null;
      }

      @Override
      public String toString() {
        return String.format("Element \"%s\" to be staled and another located by \"%s\"",
            element, locator);
      }
    };
  }

  /**
   * An expectation for checking none of the specified conditions are satisfied.
   *
   * @param conditions ExpectedConditions not to be satisfied at all
   * @return true if none of the specified conditions are satisfied
   * @since Selenium WebDriver 2.52.0
   */
  public static ExpectedCondition<Boolean> satisfiesNone(final ExpectedCondition<?>... conditions) {
    ExpectedCondition<Boolean> ec = new ExpectedCondition<Boolean>() {
      @Override
      public Boolean apply(WebDriver driver) {
        for (ExpectedCondition<?> condition : conditions) {
          Object result = condition.apply(driver);
          if (result instanceof Boolean) {
            if ((Boolean) result) {
              return Boolean.FALSE;
            }
          } else {
            if (result != null) {
              return Boolean.FALSE;
            }
          }
        }
        return Boolean.TRUE;
      }

      @Override
      public String toString() {
        String message = "ExpectedCondition(s) to not be satisfied: ";
        for (ExpectedCondition<?> condition : conditions) {
          message += condition;
        }
        return message;
      }
    };

    return ec;
  }

  /**
   * An expectation for checking at least one of the specified conditions is satisfied.
   *
   * @param conditions ExpectedConditions to be satisfied for at least one
   * @return true once one of specified conditions is satisfied
   * @since Selenium WebDriver 2.52.0
   */
  public static ExpectedCondition<Boolean> satisfiesOne(final ExpectedCondition<?>... conditions) {
    ExpectedCondition<Boolean> ec = new ExpectedCondition<Boolean>() {
      @Override
      public Boolean apply(WebDriver driver) {
        for (ExpectedCondition<?> condition : conditions) {
          Object result = condition.apply(driver);
          if (result instanceof Boolean) {
            if ((Boolean) result) {
              return Boolean.TRUE;
            }
          } else {
            if (result != null) {
              return Boolean.TRUE;
            }
          }
        }
        return Boolean.FALSE;
      }

      @Override
      public String toString() {
        String message = "ExpectedCondition(s) to be satisfied at least one: ";
        for (ExpectedCondition<?> condition : conditions) {
          message += condition;
        }
        return message;
      }
    };

    return ec;
  }

  /**
   * An expectation for checking all of the specified conditions are satisfied.
   *
   * @param conditions ExpectedConditions to be satisfied for all
   * @return true once all of the specified conditions are satisfied
   * @since Selenium WebDriver 2.52.0
   */
  public static ExpectedCondition<Boolean> satisfiesAll(final ExpectedCondition<?>... conditions) {
    ExpectedCondition<Boolean> ec = new ExpectedCondition<Boolean>() {
      @Override
      public Boolean apply(WebDriver driver) {
        for (ExpectedCondition<?> condition : conditions) {
          Object result = condition.apply(driver);
          if (result instanceof Boolean) {
            if (!(Boolean) result) {
              return Boolean.FALSE;
            }
          } else {
            if (result == null) {
              return Boolean.FALSE;
            }
          }
        }
        return Boolean.TRUE;
      }

      @Override
      public String toString() {
        String message = "ExpectedCondition(s) to be satisfied: ";
        for (ExpectedCondition<?> condition : conditions) {
          message += condition;
        }
        return message;
      }
    };

    return ec;
  }

  /**
   * An intended expectation for getting a TimeoutException if the element is into staleness.
   * Also a rare case of checking the presence of a WebElement.
   *
   * @param element the WebElement
   * @return the (same) WebElement
   * @since Selenium WebDriver 2.52.0
   */
  public static ExpectedCondition<WebElement> presenceOf(final WebElement element) {
    ExpectedCondition<WebElement> ec = new ExpectedCondition<WebElement>() {
      @Override
      public WebElement apply(WebDriver driver) {
        try {
          element.isEnabled(); // force a staleness check
          return element;
        } catch (StaleElementReferenceException stale) {
          return null;
        }
      }

      @Override
      public String toString() {
        return "presence of element: " + element;
      }
    };

    return ec;
  }

  // -------------------------------------------------------------------------------------------- //
  // ----------                           ExtendedCondition                            ---------- //
  // -------------------------------------------------------------------------------------------- //

  /**
   * An expectation for checking the presence of an element.
   *
   * @param locator used to find the element
   * @return the WebElement once it is located
   */
  public static ExtendedCondition<WebElement> presenceOfElementLocated(final By locator) {
    ExtendedCondition<WebElement> ec = new ExtendedCondition<WebElement>() {
      @Override
      public WebElement apply(SearchContext searchContext) {
        try {
          return searchContext.findElement(locator);
        } catch (NoSuchElementException nsee) {
          return null;
        }
      }

      @Override
      public String toString() {
        return "presence of element located by: " + locator;
      }
    };

    return ec;
  }

  /**
   * An expectation for checking that an element is visible. This is an ExtendedCondition
   * version of ExpectedConditions.visibilityOf.
   *
   * @param element the WebElement
   * @return the (same) WebElement once it is visible
   * @since Selenium WebDriver 2.48.0
   */
  public static ExtendedCondition<WebElement> visibilityOf(final WebElement element) {
    ExtendedCondition<WebElement> ec = new ExtendedCondition<WebElement>() {
      @Override
      public WebElement apply(SearchContext searchContext) {
        try {
          return element.isDisplayed() ? element : null;
        } catch (StaleElementReferenceException sere) {
          return null;
        }
      }

      @Override
      public String toString() {
        return "visibility of " + element;
      }
    };

    return ec;
  }

  /**
   * An expectation for checking that an element is invisible.
   *
   * @param element the WebElement
   * @return the (same) WebElement once it is invisible
   * @since Selenium WebDriver 2.48.0
   */
  public static ExtendedCondition<WebElement> invisibilityOf(final WebElement element) {
    ExtendedCondition<WebElement> ec = new ExtendedCondition<WebElement>() {
      @Override
      public WebElement apply(SearchContext searchContext) {
        try {
          return element.isDisplayed() ? null : element;
        } catch (StaleElementReferenceException sere) {
          return element;
        }
      }

      @Override
      public String toString() {
        return "invisibility of " + element;
      }
    };

    return ec;
  }

  /**
   * An alternative to <code>ExpectedConditions.visibilityOfElementLocated</code>
   * that allows you to specify a <code>SearchContext</code> instead of driver so
   * it won't be searching the entire page (the driver).
   *
   * @param locator used to find the element
   * @return the WebElement once it is located and visible
   * @since Selenium WebDriver 2.48.0
   */
  public static ExtendedCondition<WebElement> visibilityOfElementLocated(final By locator) {
    ExtendedCondition<WebElement> ec = new ExtendedCondition<WebElement>() {
      private SearchContext searchContext;

      @Override
      public WebElement apply(SearchContext searchContext) {
        this.searchContext = searchContext;

        try {
          WebElement element = searchContext.findElement(locator);
          return element.isDisplayed() ? element : null;
        } catch (StaleElementReferenceException sere) {
          return null;
        }
      }

      @Override
      public String toString() {
        return "visibility of element located by " + locator + " in " + searchContext;
      }
    };

    return ec;
  }

  /**
   * An expectation for checking that an element is invisible or absent.
   *
   * @param locator used to find the element
   * @return true once the element is invisible or not located
   * @since Selenium WebDriver 3.3.1
   */
  public static ExtendedCondition<Boolean> invisibilityOfElementLocated(final By locator) {
    ExtendedCondition<Boolean> ec = new ExtendedCondition<Boolean>() {
      private SearchContext searchContext;

      @Override
      public Boolean apply(SearchContext searchContext) {
        this.searchContext = searchContext;
        try {
          return !searchContext.findElement(locator).isDisplayed();
        } catch (NoSuchElementException nsee) {
        } catch (StaleElementReferenceException sefe) {
        }
        return true;
      }

      @Override
      public String toString() {
        return "invisibility of element located by " + locator + " in " + searchContext;
      }
    };

    return ec;
  }

  /**
   * An alternative to <code>ExtendedConditions.visibilityOfElementLocated</code>
   * that uses a slightly different mechanism to locate the element.
   *
   * @param locator used to find the element
   * @return the WebElement once it is located and visible
   * @since Selenium WebDriver 2.48.0
   */
  public static ExtendedCondition<WebElement> visibilityOfFirstVisibleElementLocated(final By locator) {
    ExtendedCondition<WebElement> ec = new ExtendedCondition<WebElement>() {
      private SearchContext searchContext;

      @Override
      public WebElement apply(SearchContext searchContext) {
        this.searchContext = searchContext;

        for (WebElement element : searchContext.findElements(locator)) {
          try {
            if (element.isDisplayed()) {
              return element;
            }
          } catch (StaleElementReferenceException sere) {
            // return null outside of the loop
          }
        }
        return null;
      }

      @Override
      public String toString() {
        return "visibility of first visible element located by " + locator + " in " + searchContext;
      }
    };

    return ec;
  }

}
