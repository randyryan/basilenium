/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.ui;

import org.basil.selenium.service.ValidationRule;
import org.basil.selenium.service.WebElementUtil;
import org.openqa.selenium.WebElement;

import com.google.common.base.Strings;

/**
 * Input
 *
 * @author ryan131
 * @since Jun 14, 2014, 1:34:35 PM
 */
public abstract class Input {

  public static TextBox createTextBox(WebElement element) {
    return new TextBox(element);
  }

  protected WebElement element;

  protected Input(WebElement element, ValidationRule rule) {
    this.element = WebElementUtil.validate(element, rule);
  }

  public boolean isDisabled() {
    return element.getAttribute("disabled") != null;
  }

  public String getName() {
    return element.getAttribute("name");
  }

  public String getValue() {
    return element.getAttribute("value");
  }

  public boolean hasValue() {
    return !Strings.isNullOrEmpty(getValue());
  }

  /**
   * TextBox
   *
   * TODO Support additional attributes
   */
  public static class TextBox extends Input {

    private TextBox(WebElement element) {
      super(element, ValidationRule.isInputTextBox());
    }

    public void input(String text) {
      element.sendKeys(text);
    }

    public void input(long number) {
      input(String.valueOf(number));
    }

    public void input(double number) {
      input(String.valueOf(number));
    }

    public void setText(String text) {
      element.clear();
      input(text);
    }

    public void setText(long number) {
      setText(String.valueOf(number));
    }

    public void setText(double number) {
      setText(String.valueOf(number));
    }

    public String getText() {
      return getValue();
    }

  }

}
