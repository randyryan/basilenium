/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.ui;


import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import com.google.common.base.Function;

/**
 * Pessimistics
 *
 * @author ryan131
 * @since Jul 31, 2015, 9:47:56 AM
 */
public class Pessimistics {
  private Pessimistics() {}

  public static Function<WebElement, WebElement> click() {
    return new Pessimistic<WebElement>() {
      @Override
      public WebElement apply(WebElement input) {
        try {
          input.click();
          return input;
        } catch (ElementNotVisibleException enve) {
          return null;   // clicking unsuccessful
        } catch (StaleElementReferenceException sere) {
          return null;   // clicking unsuccessful
        } catch (WebDriverException we) {
          if (we.getMessage().contains("Other element would receive the click")) {
            return null; // clicking unsuccessful
          }
        }
        return null;     // clicking unsuccessful
      }
    };
  }

  public static Function<WebElement, WebElement> unclickable() {
    return new Pessimistic<WebElement>() {
      @Override
      public WebElement apply(WebElement input) {
        try {
          input.click();
        } catch (ElementNotVisibleException enve) {
          return input;
        } catch (StaleElementReferenceException sere) {
          return input;
        } catch (WebDriverException we) {
          if (we.getMessage().contains("Other element would receive the click")) {
            return input;
          }
        }
        return null;
      }
    };
  }

  public static Function<WebElement, String> attribute(final String name) {
    return new Pessimistic<String>() {
      @Override
      public String apply(WebElement input) {
        if (input == null) {
          return null;
        }
        return input.getAttribute(name);
      }
    };
  }

  public static Function<WebElement, Boolean> attributeContains(final String name, final String value) {
    return new Pessimistic<Boolean>() {
      @Override
      public Boolean apply(WebElement input) {
        String values = attribute(name).apply(input);
        if (values == null) {
          return false;
        }
        return values.contains(value);
      }
    };
  }

  public static Function<WebElement, Boolean> attributeNotContains(final String name, final String value) {
    return new Pessimistic<Boolean>() {
      @Override
      public Boolean apply(WebElement input) {
        return !attributeContains(name, value).apply(input);
      }
    };
  }

  public static Function<WebElement, String> clickAndAttribute(final String name) {
    return new Pessimistic<String>() {
      @Override
      public String apply(WebElement input) {
        return click().andThen(attribute(name)).apply(input);
      }
    };
  }

  public static Function<WebElement, String> clickAndAttribute(final WebElement clickableElement, final String name) {
    return new Pessimistic<String>() {
      @Override
      public String apply(WebElement input) {
        clickableElement.click();
        return attribute(name).apply(input);
      }
    };
  }

  public static Function<WebElement, Boolean> clickAndHasClass(final String className) {
    return new Function<WebElement, Boolean>() {
      @Override
      public Boolean apply(WebElement input) {
        return click().andThen(attributeContains("class", className)).apply(input);
      }
    };
  }

  public static Function<WebElement, Boolean> clickAndHasNoClass(final String className) {
    return new Function<WebElement, Boolean>() {
      @Override
      public Boolean apply(WebElement input) {
        return !clickAndHasClass(className).apply(input);
      }
    };
  }

}
