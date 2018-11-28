/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium;

import java.util.List;

import org.basil.selenium.service.XPathUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import com.google.common.base.Preconditions;

/**
 * Basil - Stuffs that are missing in By, like:
 * <ol>
 * <li><b>Reports if it's a confident locator</b>
 *   <ul>
 *     <li>It contains an specific ID or name</li>
 *     <li>It leads to the exact element without the aid from SearchContext</li>
 *   </ul>
 * </li>
 * <li><b>Shorthand method for is/to(convert)/has(inspect)/get(extract) operations.</b>
 * </li>
 * </ol>
 *
 * @author ryan131
 * @since Feb 22, 2014, 3:57:09 PM
 */
public class Basil extends By {

  public static Basil from(By locator) {
    return new Basil(locator);
  }

  public static Basil id(String id) {
    return new Basil(By.id(id));
  }

  public static Basil name(String name) {
    return new Basil(By.name(name));
  }

  public static Basil tagName(String tagName) {
    return new Basil(By.tagName(tagName));
  }

  public static Basil xpath(String xpathExpression) {
    return new Basil(By.xpath(xpathExpression));
  }

  private By by;

  // Constructor

  private Basil(By by) {
    if (by instanceof Basil) {
      this.by = ((Basil) by).by;
    } else {
      this.by = Preconditions.checkNotNull(by);
    }
  }

  public boolean isDriver() {
    try {
      return getXPath().equals("/html") || getXPath().equals(".//html");
    } catch (IllegalArgumentException iae) {
      return false;
    }
  }

  public boolean isConfident() {
    return hasId() || hasName() || isDriver();
  }

  // ById

  public boolean isById() {
    return by instanceof By.ById;
  }

  public By.ById toById() {
    if (!isById()) {
      throw new IllegalArgumentException(
          "The locator \"" + by + "\" is not an instance of By.ById.");
    }
    return (By.ById) by;
  }

  public boolean hasId() {
    if (isById()) {
      return true;
    }
    if (isByName()) {
      return false;
    }
    if (isByXPath()) {
      return getXPath().contains("@id=");
    }
    return false;
  }

  public String getId() {
    return toById().toString().substring("By.id: ".length());
  }

  // ByName

  public boolean isByName() {
    return by instanceof By.ByName;
  }

  public By.ByName toByName() {
    if (!isByName()) {
      throw new IllegalArgumentException(
          "The locator \"" + by + "\" is not an instance of By.ByName.");
    }
    return (By.ByName) by;
  }

  public boolean hasName() {
    if (isById()) {
      return false;
    }
    if (isByName()) {
      return true;
    }
    if (isByXPath()) {
      return getXPath().contains("@name");
    }
    return false;
  }

  public String getName() {
    return toByName().toString().substring("By.name: ".length());
  }

  // ByTagName

  public boolean isByTagName() {
    return by instanceof By.ByTagName;
  }

  public By.ByTagName toByTagName() {
    if (!isByTagName()) {
      throw new IllegalArgumentException(
          "The locator \"" + by + "\" is not an instance of By.ByTagName.");
    }
    return (By.ByTagName) by;
  }

  public String getTagName() {
    return toByTagName().toString().substring("By.tagName: ".length());
  }

  // ByXPath

  public boolean isByXPath() {
    return by instanceof By.ByXPath;
  }

  public By.ByXPath toByXPath() {
    if (isById()) {
      return (By.ByXPath) By.xpath(".//*[@id='" + getId() + "']"); // Copied from By.ById
    }
    if (isByName()) {
      return (By.ByXPath) By.xpath(".//*[@name='" + getName() + "']"); // Copied from By.ByName
    }
    if (isByTagName()) {
      return (By.ByXPath) By.xpath(".//" + getTagName()); // Copied from By.ByTagName
    }
    if (isByXPath()) {
      return (By.ByXPath) by;
    }
    if (isByClassName()) {
      return (By.ByXPath) By.xpath( // Copied from By.ByClassName
          ".//*[contains(concat(' ', normalize-space(@class), ' '), ' " + getClassName() + " ')]");
    }
    throw new IllegalArgumentException(
        "The locator \"" + by + "\" cannot be transformed to By.ByXPath.");
  }

  public boolean hasXPath() {
    return isById() || isByName() || isByTagName() || isByXPath() || isByClassName();
  }

  public String getXPath() {
    return toByXPath().toString().substring("By.xpath: ".length());
  }

  public Basil append(String xpathExpression) {
    return concat(Basil.xpath(xpathExpression));
  }

  public Basil concat(By by) {
    Basil byToBasil = Basil.from(by);
    if (byToBasil.isConfident() || byToBasil.getXPath().startsWith(getXPath())) {
      return byToBasil; // startsWith the same need to be dealt with
    }
    return Basil.xpath(XPathUtil.append(this.getXPath(), byToBasil.getXPath()));
  }

  // ByClassName

  public boolean isByClassName() {
    return by instanceof By.ByClassName;
  }

  public By.ByClassName toByClassName() {
    if (!isByClassName()) {
      throw new IllegalArgumentException(
          "The locator \"" + by + "\" is not an instance of By.ByClassName.");
    }
    return (By.ByClassName) by;
  }

  public String getClassName() {
    return toByClassName().toString().substring("By.className: ".length());
  }

  // By

  @Override
  public List<WebElement> findElements(SearchContext context) {
    return by.findElements(context);
  }

  @Override
  public WebElement findElement(SearchContext context) {
    return by.findElement(context);
  }

  // Object

  @Override
  public int hashCode() {
    return by.hashCode();
  }

  @Override
  public boolean equals(Object object) {
    if (object == this) {
      return true;
    }
    if (object instanceof Basil) {
      return by.equals(((Basil) object).by);
    }
    if (object instanceof By) {
      return by.equals(object);
    }
    return false;
  }

  @Override
  public String toString() {
    return by.toString();
  }

}
