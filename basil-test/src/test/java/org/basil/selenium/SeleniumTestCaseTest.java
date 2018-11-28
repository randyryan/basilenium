/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium;

import org.basil.selenium.SeleniumTestCase;

/**
 * SeleniumTestCase Test
 *
 * @author ryan131
 * @since Apr 28, 2016, 3:45:44 PM
 */
public class SeleniumTestCaseTest extends SeleniumTestCase {

  @Override
  public void setUp() {
    super.setUp();
  }

  public void test() {
    driver.quit();
  }

  @Override
  public void tearDown() {
    super.tearDown();
  }

}