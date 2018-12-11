/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;

import com.google.common.base.Strings;

/**
 * AbstractElement
 *
 * @author ryan131
 * @since Nov 20, 2015, 11:13:38 PM
 */
public abstract class AbstractElement extends BasilContext implements WebElement, WrapsElement,
  Locatable {

  // SearchContext

  @Override
  public List<WebElement> findElements(By by) {
    return resolve().by(ResolveBy.findElements).findElements(by);
  }

  @Override
  public BasilElement findElement(By by) {
    resolve().by(ResolveBy.findElement);
    return super.findElement(by);
  }

  // TakeScreenshot

  @Override
  public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
    return resolve().by(ResolveBy.getScreenshotAs).getScreenshotAs(target);
  }

  // WrapsElement

  @Override
  public WebElement getWrappedElement() {
    return resolve().by(ResolveBy.getWrappedElement);
  }

  // Locatable

  @Override
  public Coordinates getCoordinates() {
    return ((Locatable) resolve().by(ResolveBy.getCoordinates)).getCoordinates();
  }

  // WebElement

  @Override
  public void click() {
    resolve().by(ResolveBy.click).click();
  }

  @Override
  public void submit() {
    resolve().by(ResolveBy.submit).submit();
  }

  @Override
  public void sendKeys(CharSequence... keysToSend) {
    resolve().by(ResolveBy.sendKeys).sendKeys(keysToSend);
  }

  public void clear() {
    resolve().by(ResolveBy.clear).clear();
  }

  @Override
  public String getTagName() {
    return resolve().by(ResolveBy.getTagName).getTagName();
  }

  @Override
  public String getAttribute(String name) {
    if (name.equals("for")) {
      return resolve().by(ResolveBy.getLabelForId).getAttribute("for");
    }
    return resolve().by(ResolveBy.getAttribute).getAttribute(name);
  }

  @Override
  public boolean isSelected() {
    return resolve().by(ResolveBy.isSelected).isSelected();
  }

  @Override
  public boolean isEnabled() {
    return resolve().by(ResolveBy.isEnabled).isEnabled();
  }

  @Override
  public String getText() {
    return resolve().by(ResolveBy.getText).getText();
  }

  @Override
  public boolean isDisplayed() {
    return resolve().by(ResolveBy.isDisplayed).isDisplayed();
  }

  @Override
  public Point getLocation() {
    return resolve().by(ResolveBy.getLocation).getLocation();
  }

  @Override
  public Dimension getSize() {
    return resolve().by(ResolveBy.getSize).getSize();
  }

  @Override
  public Rectangle getRect() {
    return resolve().by(ResolveBy.getRect).getRect();
  }

  @Override
  public String getCssValue(String propertyName) {
    return resolve().by(ResolveBy.getCssValue).getCssValue(propertyName);
  }

  // WebElement extension

  public boolean hasAttribute(String attribute) {
    return !Strings.isNullOrEmpty(getAttribute(attribute));
  }

  public String getId() {
    return getAttribute("id");
  }

  public boolean hasId() {
    return hasAttribute("id");
  }

  public String getClazz() {
    return getAttribute("class");
  }

  public boolean hasClass(String className) {
    String classNames = getClazz();
    if (Strings.isNullOrEmpty(classNames)) {
      return false;
    }
    return classNames.contains(className);
  }

  public String getTitle() {
    return getAttribute("title");
  }

  public boolean hasTitle() {
    return hasAttribute("title");
  }

  public String value() {
    return getAttribute("value");
  }

  public String getInnerHTML() {
    return getAttribute("innerHTML");
  }

  // Object

  @Override
  public int hashCode() {
    return resolve().by(ResolveBy.hashCode).hashCode();
  }

  @Override
  public boolean equals(Object object) {
    if (object == this) {
      return true;
    }
    if (object instanceof BasilElement) {
      object = ((BasilElement) object).resolve().by(ResolveBy.equals);
    }
    if (object instanceof WebElement) {
      return resolve().by(ResolveBy.equals).equals(((WebElement) object));
    }
    return false;
  }

  @Override
  public String toString() {
    return resolve().by(ResolveBy.toString).toString();
  }

  // Resolution

  public abstract Resolve resolve();

  public boolean isResolved() {
    return resolve().isResolved();
  }

  public abstract class Resolve extends BasilContext.Resolve  {

    protected ResolveWhen when;
    protected ResolveFrom from;
    protected ResolveBy by;
    protected String label;

    protected Resolve when(ResolveWhen when) {
      this.when = when;
      return this;
    }

    protected Resolve from(ResolveFrom from) {
      this.from = from;
      return this;
    }

    protected Resolve label(String label) {
      this.label = label;
      return this;
    }

    protected abstract WebElement by(ResolveBy by);

    // resolve() and isResolved()

    public void resolve() {
      if (!isResolved()) {
        by(ResolveBy.resolve);
      }
    }

    public boolean isResolved() {
      return context().isResolved();
    }

    // When, From, and By

    protected ResolveWhen isResolvedWhen() {
      return when;
    }

    protected ResolveFrom isResolvedFrom() {
      return from;
    }

    protected ResolveBy isResolvedBy() {
      return by;
    }

  }

  public enum ResolveWhen {

    /**
     * Resolve the element at the creation of the element.
     */
    INITIALIZATION,

    /**
     * Resolve the element at the first method invocation of the element.
     */
    INVOCATION;

  }

  public enum ResolveFrom {

    /**
     * A By object
     */
    BY,

    /**
     * A label's text
     */
    LABEL,

    /**
     * An Element that is located beforehand
     */
    ELEMENT;

  }

  public enum ResolveBy {

    // Resolution

    resolve          ("          resolve", "resolve          "),

    // SearchContext

    findElements     ("     findElements", "findElements     "),
    findElement      ("      findElement", "findElement      "),

    // TakesScreenshot

    getScreenshotAs  ("  getScreenshotAs", "getScreenshotAs  "),

    // WrapsElement

    getWrappedElement("getWrappedElement", "getWrappedElement"),

    // Locatable

    getCoordinates   ("   getCoordinates", "getCoordinates   "),

    // WebElement

    click            ("            click", "click            "),
    submit           ("           submit", "submit           "),
    sendKeys         ("         sendKeys", "sendKeys         "),
    clear            ("            clear", "clear            "),
    getTagName       ("       getTagName", "getTagName       "),
    getAttribute     ("     getAttribute", "getAttribute     "),
    getLabelForId    ("    getLabelForId", "getLabelForId    "),
    isSelected       ("       isSelected", "isSelected       "),
    isEnabled        ("        isEnabled", "isEnabled        "),
    getText          ("          getText", "getText          "),
    isDisplayed      ("      isDisplayed", "isDisplayed      "),
    getLocation      ("      getLocation", "getLocation      "),
    getSize          ("          getSize", "getSize          "),
    getRect          ("          getRect", "getRect          "),
    getCssValue      ("      getCssValue", "getCssValue      "),

    // Object

    hashCode         ("         hashCode", "hashCode         "),
    equals           ("           equals", "equals           "),
    toString         ("         toString", "toString         ");

    private String right;
    private String left;

    private ResolveBy(String right, String left) {
      this.right = right;
      this.left = left;
    }

    public String right() {
      return right;
    }

    public String left() {
      return left;
    }

  }

}
