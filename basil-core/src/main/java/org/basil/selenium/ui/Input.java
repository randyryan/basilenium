/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.ui;

import org.basil.selenium.service.ValidationRule;
import org.basil.selenium.service.WebElementUtil;
import org.openqa.selenium.WebElement;

/**
 * Input
 *
 * @author ryan131
 * @since Jun 14, 2014, 1:34:35 PM
 */
public abstract class Input implements Widget {

  public static TextBox createTextBox(WebElement element) {
    return new TextBox(element);
  }

  public static Textarea createTextarea(WebElement element) {
    return new Textarea(element);
  }

  protected WebElement element;

  protected Input(WebElement element, ValidationRule rule) {
    this.element = WebElementUtil.validate(element, rule);
  }

  @Override
  public WebElement getWebElement() {
    return element;
  }

  public static abstract class Textual extends Input {

    protected Textual(WebElement element, ValidationRule rule) {
      super(element, rule);
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
      return getValue().toString();
    }

  }

  /**
   * Textarea
   *
   * XXX The textarea is not a input
   */
  public static class Textarea extends Textual {

    protected Textarea(WebElement element) {
      super(element, ValidationRule.isTextarea());
    }

  }

  /**
   * TextBox
   *
   * TODO Support additional attributes
   */
  public static class TextBox extends Textual {

    protected TextBox(WebElement element) {
      super(element, ValidationRule.isInputTextBox());
    }

  }

}
