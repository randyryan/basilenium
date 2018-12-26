/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.dojo;

import java.util.Calendar;
import java.util.List;

import org.basil.dojo.widget.DijitCalendar;
import org.basil.dojo.widget.DijitMenu;
import org.basil.selenium.BasilElement;
import org.basil.selenium.BasilException;
import org.basil.selenium.base.DriverFactory;
import org.basil.selenium.page.BaseLookup;
import org.basil.selenium.service.WebElementService;
import org.basil.selenium.service.WebElementUtil;
import org.basil.selenium.ui.ARIAs;
import org.basil.selenium.ui.Input;
import org.basil.selenium.ui.Pessimistically;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.spearmint.util.Sleeper;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * Dijit Utility
 *
 * TODO Remove all static helper methods, then refactor this according to the structing in:
 *      https://dojotoolkit.org/reference-guide/1.10/dijit/form.html
 *
 * @author ryan131
 * @since May 27, 2016, 3:55:26 PM
 */
public abstract class Dijit extends BasilElement {

  // WebDriver and WebDriverWait

  public static WebDriver getDriver() {
    return DriverFactory.getWebDriver();
  }

  public static WebDriverWait getWait() {
    return DriverFactory.getWebDriverWait();
  }

  // Locator

  public static final By Div(String dojoAttachPoint) {
    return By.xpath("//div[@data-dojo-attach-point='" + dojoAttachPoint + "']");
  }

  public static final By Button(String text) {
    return By.xpath("//span[span/span/span[contains(@class, 'dijitButtonText')][text()='" + text + "']]");
  }

  public static final By Tab(String text) {
    return By.xpath("//div[span[@class='tabLabel'][text()='" + text + "']]");
  }

  public static final By Dialog() {
    return By.xpath("//div[div[contains(@class, 'dijitDialogTitle')]][not(contains(@style, 'display: none;'))]");
  }

  public static final By DialogByTitle(String title) {
    return By.xpath("//div[div/span[@class='dijitDialogTitle'][text()='" + title + "']]");
  }

  public static final By DialogByPartialTitle(String title) {
    return By.xpath("//div[div/span[@class='dijitDialogTitle'][contains(text(), '" + title + "')]]");
  }

  // Wrapper

  public static boolean isWrapper(WebElement element) {
    return element.getAttribute("widgetid") != null ? true : false;
  }

  public static WebElement getWrapper(WebElement widget) {
    String widgetId = widget.getAttribute("id");

    if (isWrapper(widget) || widget == null) {
      throw new IllegalArgumentException("The element is not a valid dijit widget.");
    }

    return getDriver().findElement(By.id("widget_" + widgetId));
  }

  public static WebElement getWrapperAgnostic(WebElement element) {
    WebElement wrapper = null;

    if (isWidget(element)) {
      wrapper = getWrapper(element);
    } else {
      wrapper = element;
    }

    return wrapper;
  }

  // Widget

  public static boolean isWidget(WebElement element) {
    return element.getAttribute("widgetid") == null ? true : false;
  }

  public static WebElement getWidget(WebElement wrapper) {
    return getDriver().findElement(By.id(wrapper.getAttribute("widgetid")));
  }

  public static WebElement getWidgetAgnostic(WebElement element) {
    WebElement widget = null;

    if (isWrapper(element)) {
      widget = getWidget(element);
    } else {
      widget = element;
    }

    return widget;
  }

  public static void selectMenuItem(WebElement select, String item) {
    WebElement menu = ARIAs.getAriaOwnsWebElement(select);
    WebElement menuItem = new BaseLookup(menu).byClassAndText("dijitMenuItem", item);

    getWait().until(ExpectedConditions.visibilityOf(menuItem)).click();
  }

  public static void selectMenuItem(WebElement select, WebElement ariaOwns, String item) {
    WebElement menu = ARIAs.getAriaOwnsWebElement(ariaOwns, select);
    WebElement menuItem = new BaseLookup(menu).byClassAndText("dijitMenuItem", item);

    getWait().until(ExpectedConditions.visibilityOf(menuItem)).click();
  }

  public static void selectMenuItem(WebElement select, int itemIndex) {
    WebElement menu = ARIAs.getAriaOwnsWebElement(select);
    List<WebElement> menuItems = menu.findElements(By.className("dijitMenuItem"));

    getWait().until(ExpectedConditions.visibilityOf(menuItems.get(itemIndex))).click();
  }

  public static String getSelectedMenuItem(WebElement select) {
    return select.findElement(By.tagName("td")).getText();
  }

  public static void selectComboBoxMenuItem(WebElement comboBox, String item) {
    comboBox.findElement(By.xpath("//div[input[@value='▼ ']]")).click();
    WebElement menu = ARIAs.getAriaOwnsWebElement(comboBox);
    WebElement menuItem = menu.findElement(
        By.xpath("//div[contains(@class, 'dijitMenuItem')][text()='" + item + "']"));
    getWait().until(ExpectedConditions.visibilityOf(menuItem)).click();
  }

  // Spinner

  public static void setSpinnerValue(WebElement spinner, double targetValue) {
    try {
      WebElementUtil.validateTagAndClass(spinner, "div", "dijitSpinner");
    } catch (BasilException.InvalidElement notWidgetWrapper) {
      spinner = getWrapperAgnostic(spinner);
    }

    // Get initial value

    WebElement textBox = spinner.findElement(
        By.cssSelector("input[role='spinbutton']"));
    double initialValue = WebElementUtil.getValue(textBox).toDouble();

    // Get up arrow and down arrow
  
    WebElement upArrow = spinner.findElement(
        By.cssSelector("div[class*='dijitUpArrowButton']"));
    WebElement downArrow = spinner.findElement(
        By.cssSelector("div[class*='dijitDownArrowButton']"));

    // Set the current value to target value

    WebElement upOrDown = initialValue < targetValue ? upArrow : downArrow;

    while (WebElementUtil.getValue(textBox).toDouble() != targetValue) {
        WebElementUtil.clickButton(upOrDown);
      try { Thread.sleep(50); }
      catch (InterruptedException ignored) {}
    }
  }

  public static void inputSpinnerValue(WebElement spinner, String stringValue) {
    try {
        WebElementUtil.validateTagAndClass(spinner, "div", "dijitSpinner");
    } catch (BasilException.InvalidElement notWidgetWrapper) {
      spinner = getWrapperAgnostic(spinner);
    }

    WebElement textBox = spinner.findElement(
        By.cssSelector("input[role='spinbutton']"));
    if (!textBox.getAttribute("value").trim().equals("")) {
      textBox.clear();
    }
    textBox.sendKeys(stringValue);
  }

  // Dijit

  protected Dijit() {
  }

  /**
   * Initialize this Dijit object with its root element specified.
   */
  protected Dijit(WebElement element) {
    super(element);
  }

  /**
   * Initialize this Dijit object with the parent and locator for its root element specified.
   */
  protected Dijit(SearchContext parent, By locator) {
    super(parent, locator);
  }

  protected WebElement findByClass(String className) {
    return findElement(By.className(className));
  }

  protected WebElement findById(String id) {
    return findElement(By.id(id));
  }

  protected WebElement findByAttachPoint(String attachPoint) {
    return findByXPath("//*[@data-dojo-attach-point='" + attachPoint + "']");
  }

  @Override
  public boolean isEnabled() {
    return !isDisabled();
  }

  public boolean isDisabled() {
    return hasClass("dijitDisabled");
  }

  public boolean isFocused() {
    return hasClass("dijitFocused");
  }

  /**
   * Dijit Widget
   *
   * @author ryan131
   * @since May 27, 2016, 3:59:38 PM
   */
  public static abstract class Widget<W extends org.basil.selenium.ui.Widget> extends Dijit {

    protected W widget;
    protected Mode mode;

    // Constructor

    protected Widget() {
    }

    /**
     * Initialize this Dijit widget with the parent for it's root element specified.
     */
    protected Widget(SearchContext parent) {
      super(parent, By.xpath("//*[contains(@class, 'dijit') and @id and @widgetid]"));
      setMode(Mode.OUTER);
    }

    /**
     * Initialize this Dijit widget with the parent and locator for it's root element specified.
     */
    protected Widget(SearchContext parent, By locator) {
      super(parent, locator);
      setMode(Mode.OUTER);
    }

    /**
     * Initialize this Dijit widget with its root element specified.
     */
    protected Widget(WebElement element) {
      super(element);
      setMode(Mode.OUTER);
    }

    /**
     * Initialize this Dijit widget with its inner Widget specified.
     */
    protected Widget(W widget, Mode mode) {
      setWidget(widget);
      Preconditions.checkArgument(mode != Mode.OUTER);
      setMode(mode);
      setLocator(By.xpath("//*[@id='widget_'" + widget.getId() + "']"));
    }

    // Methods

    protected void setMode(Mode mode) {
      this.mode = mode;
    }

    protected Mode getMode() {
      return mode;
    }

    protected void setWidget(W innerWidget) {
      this.widget = innerWidget;
    }

    public abstract W getWidget();

    /**
     * Returns the "widgetid" attribute of this Dijit widget.
     */
    public String getWidgetid() {
      if (getMode() == Mode.INNER || getMode() == Mode.UNISON) {
        return WebElementUtil.getId(widget.getWebElement());
      }
      return getAttribute("widgetid");
    }

    public enum Mode {

      /**
       * The widget is given a root WebElement. All nested elements used are located
       * with/under this given root WebElement.
       */
      OUTER,

      /**
       * The widget is given a inner Widget that can be used to perform most of the
       * operations except few that Dojo designated to other elements.
       */
      INNER,

      /**
       * The widget's root element is also the "inner" Widget at the same time.
       */
      UNISON;

    }

  }

  /**
   * Input tag based widget
   */
  public static abstract class InputWidget<I extends Input> extends Widget<I> {

    // WebElements

//    protected WebElement dijitInputInner;
    protected WebElement dijitValidationContainer;

    // Constructor

    protected InputWidget() {
    }

    protected InputWidget(SearchContext parent) {
      super(parent);
    }

    protected InputWidget(SearchContext parent, By locator) {
      super(parent, locator);
    }

    protected InputWidget(WebElement element) {
      super(element);
    }

    protected InputWidget(I input, Mode mode) {
      super(input, mode);
    }

    // Methods

    @Override
    protected void setWidget(I inputWidget) {
      super.setWidget(inputWidget);
    }

    protected WebElement dijitValidationContainer() {
      if (dijitValidationContainer == null) {
        dijitValidationContainer = findByXPath("/div[@class='dijitReset dijitValidationContainer']");
      }
      return dijitValidationContainer;
    }

    @Override
    public void clear() {
      getWidget().clear();
    }

    public boolean isValid() {
      return !dijitValidationContainer().isDisplayed();
    }

    public boolean isRequired() {
      getWidget().clear();
      getWidget().sendKeys(Keys.TAB); // TODO Add blur() to Input and use blur()
      Sleeper.sleep_250_ms(); // Prevent "dirty-read"
      return !isValid();
    }

  }

  public static abstract class TextInput<T extends Input.Textual> extends InputWidget<T> {

    // Constructor

    protected TextInput(SearchContext parent) {
      super(parent);
    }

    protected TextInput(SearchContext parent, By locator) {
      super(parent, locator);
    }

    protected TextInput(WebElement element) {
      super(element);
    }

    protected TextInput(T textual, Mode mode) {
      super(textual, mode);
    }

    // Methods

    public void input(String value) {
      getWidget().input(value);
    }

    public void input(long value) {
      getWidget().input(value);
    }

    public void input(double value) {
      getWidget().input(value);
    }

    public WebElementService.ValueConverter getValue() {
      return getWidget().getValue();
    }

  }

  /**
   * DijitTextArea
   *
   * @author ryan131
   */
  public static class TextArea extends TextInput<Input.Textarea> {

    // Constructor

    public TextArea(SearchContext context) {
      super(context, By.xpath("//*[contains(@class, 'dijitTextArea') and @widgetid]"));
    }

    public TextArea(SearchContext parent, String rootXPath) {
      super(parent, By.xpath(rootXPath + "//textarea[contains(@class, 'dijitTextArea')]"));
    }

    public TextArea(SearchContext context, By locator) {
      super(context, locator);
    }

    public TextArea(WebElement element) {
      super(element);
    }

    public TextArea(Input.Textarea textarea) {
      super(textarea, Mode.UNISON);
    }

    // Methods

    @Override
    public Input.Textarea getWidget() {
      if (getMode() != Mode.UNISON && widget == null) { // Initialized in Mode.OUTER.
        setWidget(Input.createTextarea((WebElement) getContext()));
      }
      return widget; // The unison mode widget
    }

  }

  /**
   * DijitTextBox
   *
   * @author ryan131
   * @since Aug 1, 2015, 11:54:54 AM
   */
  public static class TextBox extends TextInput<Input.TextBox> {

    // Constructor

    public TextBox(SearchContext parent) {
      super(parent, By.xpath("//*[contains(@class, 'dijitTextBox') and @widgetid]"));
    }

    public TextBox(SearchContext parent, String rootXPath) {
      super(parent, By.xpath(rootXPath + "//div[contains(@class, 'dijitTextBox')]"));
    }

    public TextBox(SearchContext parent, By locator) {
      super(parent, locator);
    }

    public TextBox(WebElement element) {
      super(element);
    }

    public TextBox(Input.TextBox textBox) {
      super(textBox, Mode.INNER);
    }

    // Methods

    @Override
    public Input.TextBox getWidget() {
      if (getMode() != Mode.INNER && widget == null) {
        setWidget(Input.createTextBox(findById(getWidgetid())));
      }
      return widget;
    }

  }

  /**
   * DijitSpinner
   *
   * @author ryan131
   * @since Sep 28, 2015, 10:31:04 AM
   */
  public static class Spinner extends TextBox {

    // WebElements

    protected WebElement dijitUpArrowButton;
    protected WebElement dijitDownArrowButton;

    // Constructor

    public Spinner(SearchContext context) {
      super(context, By.xpath("//*[contains(@class, 'dijitSpinner') and @widgetid]"));
    }

    public Spinner(SearchContext parent, String rootXPath) {
      super(parent, By.xpath(rootXPath + "//div[contains(@class, 'dijitSpinner')]"));
    }

    public Spinner(SearchContext context, By locator) {
      super(context, locator);
    }

    public Spinner(WebElement element) {
      super(element);
    }

    public Spinner(Input.TextBox textbox) {
      super(textbox);
    }

    // Methods

    protected WebElement dijitUpArrowButton() {
      if (dijitUpArrowButton == null) {
        dijitUpArrowButton = findByClass("dijitUpArrowButton");
      }
      return dijitUpArrowButton;
    }

    protected WebElement dijitDownArrowButton() {
      if (dijitDownArrowButton == null) {
        dijitDownArrowButton = findByClass("dijitDownArrowButton");
      }
      return dijitDownArrowButton;
    }

    public void spin(long value) {
      spin(Double.valueOf(value));
    }

    public void spin(double value) {
      double current = getValue().toDouble(); // TODO Add ability to use mouse scroll and then let
      // user to choose to use arrow button or scroll in the configuration.
      WebElement arrowButton = current < value ? dijitUpArrowButton() : dijitDownArrowButton();
      while (current != value) {
        arrowButton.click();
        current = getValue().toDouble();
      }
    }

    public boolean isInteger() {
      return !getWidget().getAttribute("aria-valuemin").contains(".");
    }

  }

  public static class ComboBox extends TextBox {

    // WebElements

    protected WebElement dijitDownArrowButton;
    protected DijitMenu dijitMenu;

    // Constructor

    public ComboBox(SearchContext context) {
      super(context, By.xpath("//*[contains(@class, 'dijitComboBox') and @widgetid]"));
    }

    public ComboBox(SearchContext parent, String rootXPath) {
      super(parent, By.xpath(rootXPath + "//div[contains(@class, 'dijitComboBox')]"));
    }

    public ComboBox(SearchContext context, By locator) {
      super(context, locator);
    }

    public ComboBox(WebElement element) {
      super(element);
    }

    // Methods

    protected WebElement dijitDownArrowButton() {
      if (dijitDownArrowButton == null) {
        dijitDownArrowButton = findByClass("dijitDownArrowButton");
      }
      return dijitDownArrowButton;
    }

    protected DijitMenu dijitMenu() {
      if (dijitMenu == null) {
        dijitDownArrowButton().click(); // TODO Update the way to click and wait for the attribute
        String dijitMenuId = getAttribute("aria-owns");
//        String dijitMenuId = Pessimistically.clickGetAttribute(this, dijitDownArrowButton(), "aria-owns");
        dijitMenu = new DijitMenu(getDriver(), By.id(dijitMenuId));
      }
      return dijitMenu;
    }

    public void selectItem(String name) {
      dijitMenu().selectItem(name);
    }

    public void selectItem(org.basil.selenium.ui.Select.Option option) {
      dijitMenu().selectItem(option.value());
    }

    public String getSelectedItem() {
      return findByClass("dijitSelectLabel").getText();
    }

  }

  public static class DateTextBox extends TextBox {

    // WebElements

    protected WebElement dijitDownArrowButton;
    protected DijitCalendar dijitCalendar;

    // Constructor

    public DateTextBox(SearchContext context) {
      super(context, By.xpath("//*[contains(@class, 'dijitDateTextBox') and @widgetid]"));
    }

    public DateTextBox(SearchContext parent, String rootXPath) {
      super(parent, By.xpath(rootXPath + "//div[contains(@class, 'dijitDateTextBox')]"));
    }

    public DateTextBox(SearchContext context, By locator) {
      super(context, locator);
    }

    public DateTextBox(WebElement element) {
      super(element);
    }

    // Methods

    protected WebElement dijitDownArrowButton() {
      if (dijitDownArrowButton == null) {
        dijitDownArrowButton = findByClass("dijitDownArrowButton");
      }
      return dijitDownArrowButton;
    }

    protected DijitCalendar dijitCalendar() {
      if (dijitCalendar == null) {
        String dijitCalendarId = Pessimistically.clickGetAttribute(this, dijitDownArrowButton(), "aria-owns");
        dijitCalendar = new DijitCalendar(getDriver(), By.id(dijitCalendarId));
      }
      return dijitCalendar;
    }

    public void select(Calendar calendar) {
      dijitCalendar().select(calendar);
    }

  }

  /**
   * DijitCheckBox
   *
   * @author ryan131
   * @since Sep 27, 2015, 5:40:32 PM
   */
  public static class CheckBox extends Widget<Input.CheckBox> {

    // Constructor

    public CheckBox(SearchContext context) {
      super(context, By.xpath("//*[contains(@class, 'dijitCheckBox') and @widgetid]"));
    }

    public CheckBox(SearchContext parent, String rootXPath) {
      super(parent, By.xpath(rootXPath + "//div[contains(@class, 'dijitCheckBox')]"));
    }

    public CheckBox(SearchContext context, By locator) {
      super(context, locator);
    }

    public CheckBox(WebElement element) {
      super(element);
    }

    public CheckBox(Input.CheckBox checkBox) {
      super(checkBox, Mode.INNER);
    }

    // Methods

    @Override
    public org.basil.selenium.ui.Input.CheckBox getWidget() {
      if (getMode() != Mode.INNER && widget == null) {
        setWidget(new Input.CheckBox(findById(getWidgetid())));
      }
      return widget;
    }

    public void check() {
//      Pessimistically.clickAndHasClass(this, "dijitCheckBoxChecked");
      getWidget().check(); // TODO use the attribute
    }

    public void uncheck() {
//      Pessimistically.clickAndHasNoClass(this, "dijitCheckBoxChecked");
      getWidget().uncheck(); // TODO use the attribute
    }

    public boolean isChecked() {
//      return hasClass("dijitCheckBoxChecked");
      return getWidget().isChecked(); // TODO use the attribute
    }

    public boolean isUnchecked() {
//      return !isChecked();
      return getWidget().isUnchecked(); // TODO use the attribute
    }

  }

  /**
   * DijitRadio
   *
   * @author ryan131
   * @since Sep 28, 2015, 5:07:38 PM
   */
  public static class Radio extends Widget<Input.RadioButton> {

    // Constructor

    public Radio(SearchContext context) {
      super(context, By.xpath("//*[contains(@class, 'dijitRadio') and @widgetid]"));
    }

    public Radio(SearchContext parent, String rootXPath) {
      super(parent, By.xpath(rootXPath + "//div[contains(@class, 'dijitRadio')]"));
    }

    public Radio(SearchContext context, By locator) {
      super(context, locator);
    }

    public Radio(WebElement element) {
      super(element);
    }

    public Radio(Input.RadioButton radioButton) {
      super(radioButton, Mode.INNER);
    }

    // Methods

    @Override
    public org.basil.selenium.ui.Input.RadioButton getWidget() {
      if (getMode() != Mode.INNER && widget == null) {
        setWidget(new Input.RadioButton(findById(getWidgetid())));
      }
      return widget;
    }

    public void check() {
      Pessimistically.clickAndHasClass(this, "dijitRadioChecked");
    }

    public void uncheck() {
      Pessimistically.clickAndHasNoClass(this, "dijitRadioChecked");
    }

    public boolean isChecked() {
      return hasClass("dijitRadioChecked");
    }

    public boolean isUnchecked() {
      throw new UnsupportedOperationException("Select another radio button in group to deselect.");
    }

  }

  public static class Select extends Widget<org.basil.selenium.ui.Widget> {

    // WebElements

    protected WebElement dijitDownArrowButton;
    protected DijitMenu dijitMenu;

    // Constructor

    public Select(SearchContext context) {
      super(context, By.xpath("//*[contains(@class, 'dijitSelect') and @widgetid]"));
    }

    public Select(SearchContext parent, String rootXPath) {
      super(parent, By.xpath(rootXPath + "//table[contains(@class, 'dijitSelect')]"));
    }

    public Select(SearchContext context, By locator) {
      super(context, locator);
    }

    public Select(WebElement element) {
      super(element);
    }

    // Methods

    protected WebElement dijitDownArrowButton() {
      if (dijitDownArrowButton == null) {
        dijitDownArrowButton = findByClass("dijitDownArrowButton");
      }
      return dijitDownArrowButton;
    }

    protected DijitMenu dijitMenu() {
      if (dijitMenu == null) {
        String dijitMenuId = Pessimistically.clickGetAttribute(this, "aria-owns");
        dijitMenu = new DijitMenu(getDriver(), By.id(dijitMenuId));
      }
      return dijitMenu;
    }

    public void selectItem(String name) {
      dijitMenu().selectItem(name);
    }

    public void selectItem(org.basil.selenium.ui.Select.Option option) {
      dijitMenu().selectItem(option.value());
    }

    public String getSelectedItem() {
      return findByClass("dijitSelectLabel").getText();
    }

    @Override
    public org.basil.selenium.ui.Widget getWidget() {
      throw new UnsupportedOperationException("DijitSelect is not a standard select!");
    }

  }

}
