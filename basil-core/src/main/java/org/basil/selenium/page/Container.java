/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.page;

import java.util.List;

import org.basil.selenium.base.BaseContext;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 * Container
 * 
 * Destiny: to be a class handling element lookup for a BasilContext/PageObject
 *          to pair up with TestContext
 *
 * @author ryan131
 * @since Sep 7, 2014, 1:10:35 PM
 */
@Deprecated
public abstract class Container extends BaseContext implements SearchContext {

  @Override
  public List<WebElement> findElements(By by) {
    return context.findElements(by);
  }

  @Override
  public WebElement findElement(By by) {
    return context.findElement(by);
  }

}

