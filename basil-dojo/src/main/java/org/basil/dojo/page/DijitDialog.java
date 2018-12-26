/**
 * Copyright (c) 2015-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.dojo.page;

import org.basil.dojo.Dijit;
import org.basil.dojo.widget.DijitButton;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.base.Strings;

/**
 * RsDijitDialog
 *
 * @author ryan131
 * @since Aug 30, 2016, 3:57:18 PM
 */
public abstract class DijitDialog extends DijitPageObject {

  // WebElements

  private WebElement title;
  private WebElement closeIcon;

  // Constructor

  public DijitDialog(WebDriver driver) {
    super(driver, Dijit.Dialog());
  }

  protected DijitDialog(WebDriver driver, String title) {
    this(driver, Dijit.DialogByTitle(title));
  }

  protected DijitDialog(WebDriver driver, String title, Object... params) {
    this(driver, Dijit.DialogByTitle(title), params);
  }

  protected DijitDialog(WebDriver driver, By locator) {
    super(driver, locator);
  }

  protected DijitDialog(WebDriver driver, By locator, Object... params) {
    super(driver, locator, params);
  }

  // Title

  public String title() {
    if (title == null) {
      try {
        title = findByXPath("//*[@class='dijitDialogTitle']");
      } catch (NoSuchElementException nsee) {
        return "";
      }
    }
    return title.getText();
  }

  public boolean hasTitle() {
    return !Strings.isNullOrEmpty(title());
  }

  // Close

  public WebElement closeIcon() {
    if (!hasCloseIcon()) {
      throw new UnsupportedOperationException("The dialog does not have a close icon.");
    }
    return closeIcon;
  }

  public boolean hasCloseIcon() {
    if (closeIcon == null) {
      try {
        closeIcon = findByXPath("//*[@class='dijitDialogCloseIcon']");
      } catch (NoSuchElementException nsee) {
      }
    }
    return closeIcon != null;
  }

  public void close() {
    clickAndExitPageObject(closeIcon());
  }

  // Button

  public DijitButton button(String buttonText) {
    return dijit().Button(buttonText, false);
  }

  public boolean hasButton(String buttonText) {
    try {
      return dijit().Button(buttonText, false).isDisplayed();
    } catch (NoSuchElementException nsee) {
      return false;
    }
  }

  public DijitButton OK() {
    return dijit().Button("OK", true);
  }

  public DijitButton Cancel() {
    return dijit().Button("Cancel", true);
  }

  public DijitButton Yes() {
    return dijit().Button("Yes", true);
  }

  public DijitButton No() {
    return dijit().Button("No", true);
  }

}
