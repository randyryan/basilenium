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
        BasilElement resolvedElement = null;
        if (from == ResolveFrom.LABEL && !Strings.isNullOrEmpty(label)) {
          setLocator(By.id(findElementByText(label, getParent()).getAttribute("for")));
        }
        if (from != ResolveFrom.ELEMENT) {
          if (getLocator().hasXPath()) {
            resolvedElement = getParent().findElement(getLocator().toByXPath());
          } else {
            resolvedElement = getParent().findElement(getLocator());
          }
        }
        // This method must guarantee that the setContext() method sets the context in BasilContext
        // successfully, therefore we should always pass a WebElement into setContext. Because:
        //
        // 1) The context of BasilElement "resolvedElement" is null
        // 2) The setContext() method treats "resolvedElement" as a BasilContext, it copies the null
        // 3) The invoke of this method indicates that this is an actual use of the element rather
        //    than the optimized find-element, this also indicates that the element is at the bottom
        //    of the element.findElement() chain; This BasilElement now should contain a valid
        //    context rather than a unresolved BasilElement to only serves as a context (chaining)
        //
        // Based on the investigation, please pay attention to the comments:
        if (resolvedElement != null) {
          if (resolvedElement.isResolved()) {
            setContext(resolvedElement); // Although logical, but this line may never be used.
            System.err.println("setContext(resolvedElement) is invoked!");
          } else {
            setContext(driver().findElement(getLocator())); // We should believe that the locator
                                                            // has been perfectly chained.
          }
        } else {
          System.err.println("The element failed to resolve");
          System.err.println("ResolveWhen: " + when);
          System.err.println("ResolveFrom: " + from);
          System.err.println("ResolveBy: " + by);
          throw new NullPointerException("The element failed to resolve.");
        }
      }
      return (WebElement) getContext();
    }

    // Slightly tweaks the findElement logic in BasilContext, mostly to avoid element
    // resolution caused by invoking the locator().getConfident(). This is useful and
    // uses less resources when incoming By is a Basil.hasXPath() compatible. Because
    // you can just concatenate the XPaths.

    @Override
    public List<WebElement> findElements(By by) {
      System.err.println("[BasilElement#findElements] This is: " + getLocator());
      By unconcatenatedBy = by;
      if (Basil.from(by).hasXPath()) {
        if (getLocator().hasXPath()) {
          by = getLocator().concat(by);
        } else {
          by = getConfidentLocator().concat(by);
        }
//        return driver().findElements(by);
        if (Basil.from(by).isConfident()) { // When the current context is a nested-context, the
          return driver().findElements(by); // getLocator() returns only its locator which may not
        } else { // be confident since nested-context are usually assigned with locator like
          // //tr[@class='headerRow']
//          return context.findElements(unconcatenatedBy);
          return driver().findElements(getConfidentLocator().concat(unconcatenatedBy));
        }
      }
      return context().get().findElements(by);
    }

    @Override
    public BasilElement findElement(By by) {
      By unconcatenatedBy = by;
      if (Basil.from(by).hasXPath() && !Basil.from(by).isConfident()) {
        if (getLocator().hasXPath()) {
          by = getLocator().concat(by);
        } else {
          by = getConfidentLocator().concat(by);
        }
      }
      if (Basil.from(by).isConfident()) {
        return BasilElement.create(driver(), by);
      }
      // Current BasilElement locator is:
      //     //div[label[text()='Share option']]//table[contains(@class, 'dijitSelect')]
      // And the by after concatenation is:
      //     //div[label[text()='Share option']]//table[contains(@class, 'dijitSelect')]//.//td
      // This causes the code
      //     BasilElement.create(context().get(), by);
      // to looking up an element with very wrong xpath:
      //     //div[label[text()='Share option']]//table[contains(@class, 'dijitSelect')]//div[label[text()='Share option']]//table[contains(@class, 'dijitSelect')]//.//td
      // And when BasilElement regenerates the locator, you see in console:
      //     //table[@id='uniqName_21_0_updSelect']//div[label[text()='Share option']]//table[contains(@class, 'dijitSelect')]//.//td
      return BasilElement.create(context().get(), unconcatenatedBy);
    }

  }

}
