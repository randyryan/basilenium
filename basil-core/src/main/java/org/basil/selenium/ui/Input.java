/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.ui;

import org.basil.selenium.service.ValidationRule;
import org.basil.selenium.service.WebElementUtil;
import org.openqa.selenium.WebElement;

/**
 * Input - The elements of tag "input".
 *
 * @author ryan131
 * @since Jun 14, 2014, 1:34:35 PM
 */
public abstract class Input implements Widget {

  public static CheckBox createCheckBox(WebElement element) {
    return new CheckBox(element);
  }

  public static RadioButton createRadioButton(WebElement element) {
    return new RadioButton(element);
  }

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

  // TODO [Input] Support autocomplete attribute

  // TODO [Input] Support autofocus attribute

  public boolean isDisabled() {
    return WebElementUtil.hasAttribute(element, "disabled");
  }

  public String getForm() {
    return WebElementUtil.getAttribute(element, "form");
  }

  // TODO [Input] Support list attribute

  public String getName() {
    return WebElementUtil.getAttribute(element, "name");
  }

  public boolean isReadonly() {
    return WebElementUtil.hasAttribute(element, "readonly");
  }

  public boolean isRequired() {
    return WebElementUtil.hasAttribute(element, "required");
  }

  // TODO [Input] Support tabindex attribute

  public String getType() {
    return WebElementUtil.getType(element);
  }

  // TODO [Input] Support value attribute

  /**
   * The type of input element that receives texts.
   */
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

  public static class CheckBox extends Input {

    public CheckBox(WebElement element) {
      super(element, ValidationRule.isInputCheckBox());
    }

    public void check() {
      if (isUnchecked()) {
        WebElementUtil.selectElement(element);
      }
    }

    public void uncheck() {
      if (isChecked()) {
        WebElementUtil.unselectElement(element);
      }
    }

    public boolean isChecked() {
      if (WebElementUtil.hasAttribute(element, "checked")) {
        return true;
      }
      return WebElementUtil.isSelected(element);
    }

    public boolean isUnchecked() {
      if (WebElementUtil.hasAttribute(element, "checked")) {
        return false;
      }
      return !WebElementUtil.isSelected(element);
    }

  }

  public static class RadioButton extends Input {

    public RadioButton(WebElement element) {
      super(element, ValidationRule.isInputRadioButton());
    }

    public void select() {
      if (!isSelected()) {
        WebElementUtil.selectElement(element);
      }
    }

    public boolean isSelected() {
      if (WebElementUtil.hasAttribute(element, "checked")) {
        return true;
      }
      return WebElementUtil.isSelected(element);
    }

  }

  /**
   * TextBox
   *
   * TODO Support additional attributes
   */
  public static class TextBox extends Textual {

    public TextBox(WebElement element) {
      super(element, ValidationRule.isInputTextBox());
    }

  }

  /**
   * Textarea
   *
   * XXX The textarea is not a input.
   */
  public static class Textarea extends Textual {

    protected Textarea(WebElement element) {
      super(element, ValidationRule.isTextarea());
    }

  }

}
