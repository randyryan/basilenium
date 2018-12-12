/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium;

import static org.basil.selenium.service.WebElementUtil.findElementByText;

import java.util.List;

import org.basil.selenium.base.DriverFactory;
import org.basil.selenium.page.PageObject;
import org.basil.selenium.service.WebElementUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

/**
 * BasilElement - Everything I think it's missing in WebElement, they are:
 * <ol>
 * <li><b>Performance boost</b>
 *   <ul>
 *     <li>Locate-on-demand</li>
 *     <li>WebElement-pool <em>(like String-pool)</em> (unimplemented)</li>
 *   </ul>
 * </li>
 * <li><b>Shorthand to attributes</b>
 *   <ul>
 *     <li>id</li>
 *     <li>class</li>
 *     <li>value</li>
 *     <li>innerHTML</li>
 *   </ul>
 * </li>
 * <li><b>Extended properties</b>
 *   <ul>
 *     <li>context <em>(SearchContext)</em></li>
 *     <li>locator <em>(By, original)</em></li>
 *     <li>locator <em>(By, regenerated)</em></li>
 *   </ul>
 * </li>
 * </ol>
 *
 * @author ryan131
 * @since Mar 9, 2014, 7:56:19 PM
 */
@SuppressWarnings({"unused"})
public class BasilElement extends AbstractElement implements WebElement, WrapsElement, Locatable {

  public static BasilElement create(SearchContext parent, By locator) {
    return new BasilElement(parent, locator);
  }

  public static BasilElement create(SearchContext parent, String label) {
    return new BasilElement(parent, label);
  }

  public static BasilElement create(WebElement element) {
    return new BasilElement(element);
  }

  private static final Logger logger = LoggerFactory.getLogger(BasilElement.class);

  private Resolve resolve;

  // Constructor

  protected BasilElement(SearchContext parent, By locator) {
    setParent(parent).setLocator(locator).resolve().when(ResolveWhen.INVOCATION).from(ResolveFrom.BY);
  }

  protected BasilElement(SearchContext parent, String label) {
    setParent(parent).resolve().when(ResolveWhen.INVOCATION).from(ResolveFrom.LABEL).label(label);
  }

  protected BasilElement(WebElement element) {
    setParent(driver()).setContext(element).resolve().when(ResolveWhen.INITIALIZATION).from(ResolveFrom.ELEMENT);
  }

  // Context

  @Override
  public BasilElement setParent(SearchContext parent) {
    return (BasilElement) super.setParent(parent);
  }

  @Override
  protected BasilElement setContext(SearchContext context) {
    return (BasilElement) super.setContext(context);
  }

  @Override
  public BasilElement setLocator(By locator) {
    return (BasilElement) super.setLocator(locator);
  }

  // SearchContext

  @Override
  public List<WebElement> findElements(By by) {
    return super.findElements(by);
  }

  @Override
  public BasilElement findElement(By by) {
    return super.findElement(by);
  }

  // Resolve

  public Resolve resolve() {
    if (resolve == null) {
      resolve = new Resolve();
    }
    return resolve;
  }

  public class Resolve extends AbstractElement.Resolve {

    @Override
    protected Resolve when(ResolveWhen when) {
      return (Resolve) super.when(when);
    }

    @Override
    protected Resolve from(ResolveFrom from) {
      return (Resolve) super.from(from);
    }

    @Override
    protected Resolve label(String label) {
      return (Resolve) super.label(label);
    }

    protected WebElement by(AbstractElement.ResolveBy by) {
      this.by = by;
      if (!isResolved()) {
        BasilElement element = null;
        if (from == ResolveFrom.LABEL && !Strings.isNullOrEmpty(label)) { // Convert label to ById
          setLocator(By.id(findElementByText(label, getParent()).getAttribute("for")));
        }
        if (getLocator().hasXPath()) {
          element = getParent().findElement(getLocator().toByXPath());
        } else {
          element = getParent().findElement(getLocator());
        }
        // This method must guarantee to set the context successfully, so the element must be a
        // resolved one, otherwise setContext will sets the context to null.
        if (element != null) {
          if (element.isResolved()) {
            setContext(element);
          } else {
            // We should believe that the locator returned by getLocator() is perfectly chained
            setContext(driver().findElement(getLocator()));
          }
        } else {
          logger.error("The element " + getLocator() + " failed to resolve in " + getContext());
          logger.error("ResolveWhen: " + when + ", ResolveFrom: " + from + ", ResolveBy: " + by);
          throw new NullPointerException();
        }
      }
      return (WebElement) getContext();
    }

  }

}
