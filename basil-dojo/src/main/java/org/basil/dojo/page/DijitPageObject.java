/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.dojo.page;

import org.basil.dojo.Dijit;
import org.basil.dojo.widget.DijitButton;
import org.basil.selenium.page.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 * Dijit PageObject
 *
 * @author ryan131
 * @since Aug 30, 2016, 3:21:04 PM
 */
public abstract class DijitPageObject extends PageObject {

  // Constructor

  protected DijitPageObject(SearchContext context, By locator) {
    this(context, locator, 60);
  }

  protected DijitPageObject(SearchContext context, By locator, Object... params) {
    this(context, locator, 60, params);
  }

  // Safe to remove
  protected DijitPageObject(SearchContext context, By locator, long timeout) {
    this(context, locator, timeout, new Object[0]);
  }

  protected DijitPageObject(SearchContext context, By locator, long timeout, Object... params) {
    super(context, locator, timeout, params);
  }

  protected DijitPageObject() {
    super();
  }

  protected DijitPageObject(Object... params) {
    super(params);
  }

  // Fast element locating methods

  protected final WebElement div(String dojoAttachPoint) {
    return findElement(Dijit.Div(dojoAttachPoint));
  }

  protected final DijitButton dijitButton(String text) {
    return dijitButton(text, false);
  }

  protected final DijitButton dijitButton(String text, boolean closeParentOnClick) {
    DijitButton button = new DijitButton(findElement(Dijit.Button(text)), closeParentOnClick);
    button.setParent(this);
    return button;
  }

  protected final Dijit.CheckBox dijitCheckBox(SearchContext context) {
    return new Dijit.CheckBox(context);
  }

  protected final Dijit.Radio dijitRadio(SearchContext context) {
    return new Dijit.Radio(context);
  }

  protected final Dijit.TextBox dijitTextBox(SearchContext context) {
    return new Dijit.TextBox(context);
  }

  protected final Dijit.TextBox dijitTextBox(String xpathExpression) {
    return new Dijit.TextBox(this, xpathExpression);
  }

  protected final Dijit.TextArea dijitTextArea(SearchContext context) {
    return new Dijit.TextArea(context);
  }

  protected final Dijit.Spinner dijitSpinner(SearchContext context) {
    return new Dijit.Spinner(context);
  }

  protected final Dijit.Select dijitSelect(SearchContext context) {
    return new Dijit.Select(context);
  }

  protected final Dijit.ComboBox dijitComboBox(SearchContext context) {
    return new Dijit.ComboBox(context);
  }

  protected final Dijit.DateTextBox dijitDateTextBox(SearchContext context) {
    return new Dijit.DateTextBox(context);
  }

  protected final WebElement dijitTab(String text) {
    return findElement(Dijit.Tab(text));
  }

}
