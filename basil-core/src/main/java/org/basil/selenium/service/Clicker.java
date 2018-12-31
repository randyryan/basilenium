/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */
package org.basil.selenium.service;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.spearmint.util.Sleeper;

import com.google.common.base.Function;

/**
 * Clicker - The clicking strategy
 *
 * @author ryan131
 * @since Oct 11, 2013, 7:00:18 PM
 */
public interface Clicker {

  void click(WebElement element);

  public static Clicker element() {
    return new Clicker() {
      @Override
      public void click(WebElement element) {
        element.click();
      }
    };
  }

  public static Clicker javascript(final JavascriptExecutor executor) {
    return new Clicker() {
      @Override
      public void click(WebElement element) {
        executor.executeScript("arguments[0].click();", element);
      }
    };
  }

  public static Clicker actions(final WebDriver driver) {
    return new Clicker() {
      @Override
      public void click(WebElement element) {
        new Actions(driver).click(element).click().perform();
      }
    };
  }

  public static Clicker actions(final Actions actions) {
    return new Clicker() {
      @Override
      public void click(WebElement element) {
        actions.click(element);
      }
    };
  }

  public static Clicker actionsHover(final WebDriver driver) {
    return new Clicker() {
      @Override
      public void click(WebElement element) {
        new Actions(driver).moveToElement(element).click().perform();
      }
    };
  }

  public static Clicker link() {
    return new Clicker() {
      @Override
      public void click(WebElement element) {
        WebElementUtil.click(WebElementUtil.validate(element, ValidationRule.isLink()));
      }
    };
  }

  public static Clicker button() {
    return new Clicker() {
      @Override
      public void click(WebElement element) {
        WebElementUtil.click(WebElementUtil.validate(element, ValidationRule.isButton()));
      }
    };
  }

  public static <S extends SearchContext> Clicker satisfies(final Function<S, ?> condition) {
    return satisfies(condition, element());
  }

  /**
   * Click repetitively until the incoming condition is satisfied or timed out.
   *
   * The method signature is weird but works with both ExpectedCondition and ExtendedCondition.
   */
  @SuppressWarnings("unchecked")
  public static <S extends SearchContext> Clicker satisfies(
      final Function<S, ?> condition, final Clicker clicker) {
    final int tries = 8;       // Times of tries
    final long interval = 125; // Interval between each try

    return new Clicker() {
      @Override
      public void click(WebElement element) {
        for (int i = tries; i > 0; i--) {
          clicker.click(element);
          Object result = null;
          if (condition instanceof ExpectedCondition) {
            try {
              // Avoid ClassCastException from casting the WebDriver input to type S. This works
              // when the WebDriver is not used in the ExpectedCondition, for conditions did use
              // the WebDriver, user should be notified that the condition is not supported.
              result = condition.apply(null);
            } catch (NullPointerException npe) {
              throw new UnsupportedOperationException("ExpectedCondition \"" + condition + "\"" +
                  "cannot be used with Clicker.satisfies().");
            }
          } else {
            result = condition.apply((S) element);
          }
          if (result instanceof Boolean) {
            if (result.equals(Boolean.TRUE)) {
              break;
            }
          } else if (result != null) {
            break;
          }
          Sleeper.sleepSilently(interval);
        }
      }
    };
  }

}
