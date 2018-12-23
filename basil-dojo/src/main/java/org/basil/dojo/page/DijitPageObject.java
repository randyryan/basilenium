/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.dojo.page;

import org.basil.dojo.Dijit;
import org.basil.dojo.widget.DijitButton;
import org.basil.selenium.page.PageObject;
import org.basil.selenium.ui.Input;
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

  private DijitWidget dijit;
  private FormWidget widget;

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

  protected final WebElement dijitTab(String text) {
    return findElement(Dijit.Tab(text));
  }

  protected DijitWidget dijit() {
    if (dijit == null) {
      dijit = new DijitWidget();
    }
    return dijit;
  }

  public class DijitWidget {

    private FormWidget formWidget;

    private DijitWidget() {
      formWidget = new FormWidget();
    }

    public FormWidget form() {
      return formWidget;
    }
  }

  public class FormWidget {

    public final Dijit.TextArea Textarea(String rootXPath) {
      return new Dijit.TextArea(DijitPageObject.this, rootXPath);
    }

    public final Dijit.TextArea textarea(WebElement element) {
      return new Dijit.TextArea(Input.createTextarea(element));
    }

    public final Dijit.TextBox TextBox(String rootXPath) {
      return new Dijit.TextBox(DijitPageObject.this, rootXPath);
    }

    public final Dijit.TextBox TextBox(WebElement element) {
      return new Dijit.TextBox(Input.createTextBox(element));
    }

    protected final Dijit.Spinner Spinner(String rootXPath) {
      return new Dijit.Spinner(DijitPageObject.this, rootXPath);
    }

    protected final Dijit.ComboBox ComboBox(String rootXPath) {
      return new Dijit.ComboBox(DijitPageObject.this, rootXPath);
    }

    protected final Dijit.DateTextBox DateTextBox(String rootXPath) {
      return new Dijit.DateTextBox(DijitPageObject.this, rootXPath);
    }

    protected final Dijit.CheckBox CheckBox(String rootXPath) {
      return new Dijit.CheckBox(DijitPageObject.this, rootXPath);
    }

    protected final Dijit.CheckBox CheckBox(WebElement element) {
      return new Dijit.CheckBox(Input.createCheckBox(element));
    }

    protected final Dijit.Radio Radio(String rootXPath) {
      return new Dijit.Radio(DijitPageObject.this, rootXPath);
    }

    protected final Dijit.Radio Radio(WebElement element) {
      return new Dijit.Radio(Input.createRadioButton(element));
    }

    protected final Dijit.Select dijitSelect(String rootXPath) {
      return new Dijit.Select(DijitPageObject.this, rootXPath);
    }
  }

}
