/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.page;

import org.basil.selenium.BasilContext;

/**
 * PageObjectAnnotation
 *
 * @author ryan131
 * @since Dec 25, 2014, 1:15:26 PM
 */
public abstract class PageObjectAnnotation extends BasilContext {

  /**
   * Allows the locator facility to recalculate the page object's locator.
   */
  protected boolean locatorRegeneration() {
    return true;
  }

  /**
   * Allows WebElements to be initialized on-the-fly, this is useful when the current
   * page object needed to be initialized by another page object that contains it.
   */
  public boolean initializeOnTheFly() {
    return false;
  }

}
