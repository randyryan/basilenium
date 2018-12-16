/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.dojo;

import java.util.Calendar;
import java.util.List;

import org.basil.dojo.widget.DijitCalendar;
import org.basil.dojo.widget.DijitMenu;
import org.basil.selenium.BasilElement;
import org.basil.selenium.BasilException.Element;
import org.basil.selenium.base.DriverFactory;
import org.basil.selenium.page.BaseLookup;
import org.basil.selenium.service.WebElementService;
import org.basil.selenium.service.WebElementUtil;
import org.basil.selenium.ui.ARIAs;
import org.basil.selenium.ui.Pessimistically;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.spearmint.util.Sleeper;

import com.google.common.collect.Lists;

/**
 * Dijit Utility
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

  public static boolean isWidgetRequired(WebElement widget) {
    widget.clear();
    widget.sendKeys(Keys.TAB);

    String dijitValidationContainerXpath =
        "//div[@id='widget_" + widget.getAttribute("id") + "']"
        + "/div[contains(@class, 'dijitValidationContainer')]";

    return getDriver().findElement(By.xpath(dijitValidationContainerXpath)).isDisplayed();
  }

  public static boolean isWidgetRequiredWithForcedFocusLeave(WebElement widget) {
    widget.clear();

    By labelXpath = By.cssSelector("label[for='" + widget.getAttribute("id") + "']");
    getDriver().findElement(labelXpath).click();

    String dijitValidationContainerXpath =
        "//div[@id='widget_" + widget.getAttribute("id") + "']"
        + "/div[contains(@class, 'dijitValidationContainer')]";

    return getDriver().findElement(By.xpath(dijitValidationContainerXpath)).isDisplayed();
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

  public static List<String> getMenuItems(WebElement select) {
    WebElement menu = ARIAs.getAriaOwnsWebElement(select);

    List<String> menuItems = Lists.newArrayList();
    for (WebElement menuItem : menu.findElements(By.className("dijitMenuItem"))) {
      menuItems.add(menuItem.getText());
    }
    select.click();

    return menuItems;
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
    } catch (Element.Invalid notWidgetWrapper) {
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
    } catch (Element.Invalid notWidgetWrapper) {
      spinner = getWrapperAgnostic(spinner);
    }

    WebElement textBox = spinner.findElement(
        By.cssSelector("input[role='spinbutton']"));
    if (!textBox.getAttribute("value").trim().equals("")) {
      textBox.clear();
    }
    textBox.sendKeys(stringValue);
  }

  private static void selectCalendarMenuItem(WebElement monthButton, String month) {
    String ariaOwns = ARIAs.getAriaOwnsWebELementId(monthButton, monthButton);
    WebElement menu = getDriver().findElement(By.xpath("//*[@aria-label='" + ariaOwns+ "']"));

    WebElement menuItem = new BaseLookup(menu).byClassAndText("dijitCalendarMonthLabel", month);
    getWait().until(ExpectedConditions.visibilityOf(menuItem)).click();
  }

  public static void selectDijitCalendar(WebElement dijitCalendar, Calendar calendar) {
    WebDriver driver = getDriver();
    String dijitCalendarId = dijitCalendar.getAttribute("id");
    String dijitCalendarXpath = "//table[@id='" + dijitCalendarId + "']";

    // Select year

    String previousYearXpath = dijitCalendarXpath + "//span[contains(@class, 'PreviousYear')]";
    String currentYearXpath = dijitCalendarXpath + "//span[contains(@class, 'SelectedYear')]";
    String nextYearXpath = dijitCalendarXpath + "//span[contains(@class, 'NextYear')]";

    int selectedYear = Integer.parseInt(driver.findElement(By.xpath(currentYearXpath)).getText());
    int targetYear = calendar.get(Calendar.YEAR);

    if (selectedYear != targetYear) {
      String previousOrNext = selectedYear > targetYear ? previousYearXpath : nextYearXpath;
      while (selectedYear != targetYear) {
        driver.findElement(By.xpath(previousOrNext)).click();
        try { Thread.sleep(100); } catch (InterruptedException ie) {}
          selectedYear = Integer.parseInt(driver.findElement(By.xpath(currentYearXpath)).getText());
        }
      }

      // Select month

      String monthButtonXpath =
          dijitCalendarXpath + "/thead//span[contains(@class, 'dijitDownArrowButton')]";

      String monthToSelect = null;
      switch (calendar.get(Calendar.MONTH)) {
      case Calendar.JANUARY:
          monthToSelect = "January";
          break;
      case Calendar.FEBRUARY:
          monthToSelect = "Febuary";
          break;
      case Calendar.MARCH:
          monthToSelect = "March";
          break;
      case Calendar.APRIL:
          monthToSelect = "April";
          break;
      case Calendar.MAY:
          monthToSelect = "May";
          break;
      case Calendar.JUNE:
          monthToSelect = "June";
          break;
      case Calendar.JULY:
          monthToSelect = "July";
          break;
      case Calendar.AUGUST:
          monthToSelect = "August";
          break;
      case Calendar.SEPTEMBER:
          monthToSelect = "September";
          break;
      case Calendar.OCTOBER:
          monthToSelect = "October";
          break;
      case Calendar.NOVEMBER:
          monthToSelect = "November";
          break;
      case Calendar.DECEMBER:
          monthToSelect = "December";
          break;
      }

      WebElement monthButton = driver.findElement(By.xpath(monthButtonXpath));
      selectCalendarMenuItem(monthButton, monthToSelect);

      // Select day

      int day = calendar.get(Calendar.DAY_OF_MONTH);
      String dayXpath = dijitCalendarXpath + 
          "/tbody//span[text()='" + day + "']/parent::td[contains(@class, 'CurrentMonth')]";
      driver.findElement(By.xpath(dayXpath)).click();
  }

  // Dijit

  public Dijit(WebElement element) {
    super(element);
  }

  public Dijit(SearchContext context, By locator) {
    super(context, locator);
  }

  public Dijit(SearchContext context, String xpathExpression) {
    super(context, By.xpath(xpathExpression));
  }

  protected WebElement findByClass(String className) {
    return findElement(By.className(className));
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

  // Dijit widget

  /**
   * Dijit Widget
   *
   * @author ryan131
   * @since May 27, 2016, 3:59:38 PM
   */
  public static abstract class Widget extends Dijit {

    // Constructor

    public Widget(SearchContext context) {
      super(context, By.xpath("//*[contains(@class, 'dijit') and @id and @widgetid]"));
    }

    public Widget(SearchContext context, By locator) {
      super(context, locator);
    }

    public Widget(SearchContext context, String xpathExpression) {
      super(context, xpathExpression);
    }

    public Widget(WebElement element) {
      super(element);
    }

    // Methods

    public abstract WebElement getWidget();

    public String getWidgetid() {
      return getAttribute("widgetid");
    }

  }

  // Input-based widget

  public static abstract class InputWidget extends Widget {

    // WebElements

    protected WebElement dijitInputInner;
    protected WebElement dijitValidationContainer;

    // Constructor

    public InputWidget(SearchContext context) {
      super(context);
    }

    public InputWidget(SearchContext context, By locator) {
      super(context, locator);
    }

    public InputWidget(SearchContext context, String xpathExpression) {
      super(context, xpathExpression);
    }

    public InputWidget(WebElement element) {
      super(element);
    }

    // Methods

    @Override
    public WebElement getWidget() {
      if (dijitInputInner == null) {
        dijitInputInner = findByClass("dijitInputInner");
      }
      return dijitInputInner;
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
      getWidget().sendKeys(Keys.TAB);
      Sleeper.sleep_250_ms(); // Prevent "dirty-read"
      return !isValid();
    }

  }

  public static abstract class TextWidget extends InputWidget {

    // Constructor

    public TextWidget(SearchContext context) {
      super(context);
    }

    public TextWidget(SearchContext context, By locator) {
      super(context, locator);
    }

    public TextWidget(SearchContext context, String xpathExpression) {
      super(context, xpathExpression);
    }

    public TextWidget(WebElement element) {
      super(element);
    }

    // Methods

    public void input(String value) {
      getWidget().clear();
      getWidget().sendKeys(value);
    }

    public void input(long value) {
      input(String.valueOf(value));
    }

    public void input(double value) {
      input(String.valueOf(value));
    }

    public WebElementService.ValueConverter getValue() {
      return WebElementUtil.getValue(getWidget());
    }

  }

  /**
   * DijitTextBox
   *
   * @author ryan131
   * @since Aug 1, 2015, 11:54:54 AM
   */
  public static class TextBox extends TextWidget {

    // Constructor

    public TextBox(SearchContext context) {
      super(context, By.xpath("//*[contains(@class, 'dijitTextBox') and @widgetid]"));
    }

    public TextBox(SearchContext context, By locator) {
      super(context, locator);
    }

    public TextBox(SearchContext context, String xpathExpression) {
      super(context, xpathExpression);
    }

    public TextBox(WebElement element) {
      super(element);
    }

    // Methods

    @Override
    public WebElement getWidget() {
      if (dijitInputInner == null) {
        dijitInputInner = findByClass("dijitInputInner");
      }
      return dijitInputInner;
    }

  }

  public static class TextArea extends TextWidget {

    // Constructor

    public TextArea(SearchContext context) {
      super(context, By.xpath("//*[contains(@class, 'dijitTextArea') and @widgetid]"));
    }

    public TextArea(SearchContext context, By locator) {
      super(context, locator);
    }

    public TextArea(WebElement element) {
      super(element);
    }

    // Methods

    @Override
    public WebElement getWidget() {
      return this;
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

    private WebElement dijitUpArrowButton;
    private WebElement dijitDownArrowButton;

    // Constructor

    public Spinner(SearchContext context) {
      super(context, By.xpath("//*[contains(@class, 'dijitSpinner') and @widgetid]"));
    }

    public Spinner(SearchContext context, By locator) {
      super(context, locator);
    }

    public Spinner(WebElement element) {
      super(element);
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
      double current = getValue().toDouble();
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

  public static class Select extends InputWidget {

    // WebElements

    private WebElement dijitDownArrowButton;
    private DijitMenu dijitMenu;

    // Constructor

    public Select(SearchContext context) {
      super(context, By.xpath("//*[contains(@class, 'dijitSelect') and @widgetid]"));
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

  }

  public static class ComboBox extends Select {

    // WebElements

    private DijitMenu dijitMenu;

    // Constructor

    public ComboBox(SearchContext context) {
      super(context, By.xpath("//*[contains(@class, 'dijitComboBox') and @widgetid]"));
    }

    public ComboBox(SearchContext context, By locator) {
      super(context, locator);
    }

    public ComboBox(WebElement element) {
      super(element);
    }

    // Methods

    protected DijitMenu dijitMenu() {
      if (dijitMenu == null) {
        String dijitMenuId = Pessimistically.clickGetAttribute(this, dijitDownArrowButton(), "aria-owns");
        dijitMenu = new DijitMenu(getDriver(), By.id(dijitMenuId));
      }
      return dijitMenu;
    }

    // Methods from TextBox

    public void input(String value) {
      getWidget().clear();
      getWidget().sendKeys(value);
    }

    public void input(long value) {
      input(String.valueOf(value));
    }

    public void input(double value) {
      input(String.valueOf(value));
    }

    public WebElementService.ValueConverter getValue() {
      return WebElementUtil.getValue(getWidget());
    }

  }

  public static class DateTextBox extends TextBox {

    // WebElements

    private WebElement dijitDownArrowButton;
    private DijitCalendar dijitCalendar;

    // Constructor

    public DateTextBox(SearchContext context) {
      super(context, By.xpath("//*[contains(@class, 'dijitDateTextBox') and @widgetid]"));
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
  public static class CheckBox extends Widget {

    // Constructor

    public CheckBox(SearchContext context) {
      super(context, By.xpath("//*[contains(@class, 'dijitCheckBox') and @widgetid]"));
    }

    public CheckBox(SearchContext context, By locator) {
      super(context, locator);
    }

    public CheckBox(WebElement element) {
      super(element);
    }

    // Methods

    @Override
    public WebElement getWidget() {
      return findByClass("dijitCheckBoxInput");
    }

    public void check() {
      Pessimistically.clickAndHasClass(this, "dijitCheckBoxChecked");
    }

    public void uncheck() {
      Pessimistically.clickAndHasNoClass(this, "dijitCheckBoxChecked");
    }

    public boolean isChecked() {
      return hasClass("dijitCheckBoxChecked");
    }

    public boolean isUnchecked() {
      return !isChecked();
    }

  }

  /**
   * DijitRadio
   *
   * @author ryan131
   * @since Sep 28, 2015, 5:07:38 PM
   */
  public static class Radio extends CheckBox {

    // Constructor

    public Radio(SearchContext context) {
      super(context, By.xpath("//*[contains(@class, 'dijitRadio') and @widgetid]"));
    }

    public Radio(SearchContext context, By locator) {
      super(context, locator);
    }

    public Radio(WebElement element) {
      super(element);
    }

    // Methods

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

}
