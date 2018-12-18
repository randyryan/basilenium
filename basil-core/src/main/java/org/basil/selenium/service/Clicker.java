/**
 * Copyright (c) 2010-2018 Ryan Li Wan. All rights reserved.
 */
package org.basil.selenium.service;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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

  @SuppressWarnings("unchecked")
  public static <S extends SearchContext> Clicker satisfies(Function<S, ?> condition) {
    final int tries = 8;       // Times of tries
    final long interval = 125; // Interval between each try
    return new Clicker() {
      @Override
      public void click(WebElement element) {
        for (int i = tries; i > 0; i--) {
          element.click();
          Object result = condition.apply((S) element);
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
