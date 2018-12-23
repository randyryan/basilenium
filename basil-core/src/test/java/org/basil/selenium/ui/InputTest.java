/**
 * Copyright (c) 2015-2016 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.ui;

import org.basil.selenium.WAUT;
import org.basil.selenium.base.DriverFactory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;

/**
 * InputTest
 *
 * @author ryan131
 * @since Jun 14, 2014, 3:36:31 PM
 */
public class InputTest {

  private static WAUT waut;

  @BeforeClass
  public static void setUpClass() {
    waut = new WAUT(DriverFactory.getWebDriver());
    waut.start();
  }

  @Test
  public void disabledAttribute() {
    Input input = Input.createTextBox(waut.findElement(By.id("input-attribute-disabled")));
    Assert.assertTrue(input.isDisabled());
  }

  @Test
  public void readonlyAttribute() {
    Input input = Input.createTextBox(waut.findElement(By.id("input-attribute-readonly")));
    Assert.assertTrue(input.isReadonly());
  }

  @Test
  public void requiredAttribute() {
    Input input = Input.createTextBox(waut.findElement(By.id("input-attribute-required")));
    Assert.assertTrue(input.isRequired());
  }

  @AfterClass
  public static void tearDown() {
    waut.exit();
  }

}
