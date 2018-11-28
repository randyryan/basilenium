/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium;

import org.basil.selenium.service.ServiceContext;
import org.openqa.selenium.WebDriver;

/**
 * Selenium Test
 *
 * @author ryan131
 * @since Oct 10, 2015, 11:16:55 AM
 */
public abstract class SeleniumContext extends ServiceContext {

  // -------------------------------------------------------------------------------------------- //
  // The TestContext, like ServiceContext is rarely used instantiated.                            //
  // -------------------------------------------------------------------------------------------- //

  @SuppressWarnings("deprecation")
  protected SeleniumContext() {
    super();
  }

  @SuppressWarnings("deprecation")
  protected SeleniumContext(WebDriver driver) {
    super(driver);
  }

}
