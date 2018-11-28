/**
 * Copyright (c) 2015-2016 Li Wan. All rights reserved.
 */

package org.basil.selenium;

import org.basil.selenium.SeleniumContext;
import org.openqa.selenium.WebElement;

/**
 * Test Context
 * 
 * Destiny: to be a class handling element interactions for a BasilContext/PageObject
 *          to pair up with Container
 *
 * @author ryan131
 * @since Oct 21, 2015, 7:38:31 PM
 */
public abstract class SeleniumTest extends SeleniumContext {

  public static boolean isEnabled(WebElement element) {
    throw new UnsupportedOperationException("To be implemented.");
  }

  public static boolean isDisabled(WebElement element) {
    throw new UnsupportedOperationException("To be implemented.");
  }

  public static void inputText(WebElement textBox, String text) {
    throw new UnsupportedOperationException("To be implemented.");
  }

  public static void clickLink(String linkText) {
    throw new UnsupportedOperationException("To be implemented.");
  }

  public static void clickPartialLink(String partialLinkText) {
    throw new UnsupportedOperationException("To be implemented.");
  }

  public static void clickButton(WebElement button) {
    throw new UnsupportedOperationException("To be implemented.");
  }

  public static  void checkCheckBox(WebElement checkBox) {
    throw new UnsupportedOperationException("To be implemented.");
  }

  public static void uncheckCheckBox(WebElement checkBox) {
    throw new UnsupportedOperationException("To be implemented.");
  }

  public static void selectRadioButton(WebElement radioButton) {
    throw new UnsupportedOperationException("To be implemented.");
  }

  public static void selectDropdown(WebElement dropdown, WebElement item) {
    throw new UnsupportedOperationException("To be implemented.");
  }

}
