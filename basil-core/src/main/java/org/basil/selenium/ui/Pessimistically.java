/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.ui;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;

/**
 * Pessimistically
 *
 * @author ryan131
 * @since Jul 31, 2015, 11:51:06 AM
 */
public class Pessimistically {
  private Pessimistically() {}

  public static void click(final WebElement element) {
    wait(element).until(Pessimistics.unclickable());
  }

  public static void clickAndHasClass(final WebElement element, final String className) {
    wait(element).until(Pessimistics.clickAndHasClass(className));
  }

  public static void clickAndHasNoClass(final WebElement element, final String className) {
    wait(element).until(Pessimistics.clickAndHasNoClass(className));
  }

  public static String clickGetAttribute(final WebElement element, final String attribute) {
    return wait(element).until(Pessimistics.clickAndAttribute(attribute));
  }

  public static String clickGetAttribute(final WebElement element, final WebElement clickableElement, final String attribute) {
    return wait(element).until(Pessimistics.clickAndAttribute(clickableElement, attribute));
  }

  public static FluentWait<WebElement> wait(WebElement element) {
    return new FluentWait<WebElement>(element)
        .withTimeout(2500, TimeUnit.MILLISECONDS)
        .pollingEvery(125, TimeUnit.MILLISECONDS);
  }

}
