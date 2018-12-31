/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.service;

import org.basil.selenium.WAUT;
import org.basil.selenium.base.DriverFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * ClickerTest
 *
 * @author ryan131
 * @since Dec 24, 2018, 1:39:26 PM
 */
public class ClickerTest {

  private static WAUT waut;

  @BeforeClass
  public static void setUpClass() {
    waut = new WAUT(DriverFactory.getWebDriver());
    waut.start();
  }

  @Test(expected=IllegalStateException.class)
  public void untilShouldSetEitherTimesOrTimeout() {
    WebElement formTab = waut.findElement(By.id("nav-form-tab"));
    WebElementUtil.clickRepeatedly(formTab).until(
        ExpectedConditions.visibilityOfElementLocated(By.id("nav-form")));
  }

  @Test(expected=IllegalStateException.class)
  public void untilShouldNotSetBothTimesAndTimeout() {
    WebElement formTab = waut.findElement(By.id("nav-form-tab"));
    WebElementUtil.clickRepeatedly(formTab).times(8).timeout(2000).until(
        ExpectedConditions.visibilityOfElementLocated(By.id("nav-form")));
  }

  @Test(expected=IllegalStateException.class)
  public void untilShouldSetInterval() {
    WebElement formTab = waut.findElement(By.id("nav-form-tab"));
    WebElementUtil.clickRepeatedly(formTab).times(8).until(
        ExpectedConditions.visibilityOfElementLocated(By.id("nav-form")));
  }

  @Test(expected=IllegalArgumentException.class)
  public void untilShouldUseConditionCompatibleInput() {
    WebElement formTab = waut.findElement(By.id("nav-form-tab"));
    WebElementUtil.clickRepeatedly(formTab).times(8).every(125).until(
        ExpectedConditions.visibilityOfElementLocated(By.id("nav-form")), null);
  }

  @Test
  public void untilShouldWork() {
    WebElement formTab = waut.findElement(By.id("nav-form-tab"));
    WebElement form = WebElementUtil.clickRepeatedly(formTab).times(8).every(125).until(
        ExpectedConditions.visibilityOfElementLocated(By.id("nav-form")));

    Assert.assertTrue(form.isDisplayed());
  }

  @After
  public void tearDown() {
    waut.findElement(By.id("nav-intervals-tab")).click();
    waut.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-intervals")));
  }

  @AfterClass
  public static void tearDownClass() {
    waut.exit();
  }

}
