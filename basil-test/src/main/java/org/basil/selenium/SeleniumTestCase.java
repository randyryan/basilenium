/**
 * Copyright (c) 2015-2016 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium;

import java.util.concurrent.TimeUnit;

import org.basil.junit.JUnitTestCase;
import org.basil.selenium.page.ElementLookup;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Add Selenium WebDriver to the JUnit Testcase. The WebDriver, WebDriverWait
 * objects are used as global variables.
 *
 * @author ryan131
 * @since Sep 27, 2015, 2:11:14 PM
 */
public abstract class SeleniumTestCase extends JUnitTestCase {

  private static final Logger logger = LoggerFactory.getLogger(SeleniumTestCase.class);

  // Default variables for all subclass test cases

  protected WebDriver driver;
  protected WebDriverWait wait;
  protected ElementLookup lookup;

  public SeleniumTestCase() {
    super();
  }

  public SeleniumTestCase(String name) {
    super(name);
  }

  // Methods for assisting test case

  @Deprecated
  protected void setWaitTimeoutInSeconds(long timeOutInSeconds) {
    SeleniumTestUtil.temp_setWaitTimeout(timeOutInSeconds, TimeUnit.SECONDS);
  }

  @Deprecated
  protected void resetWaitTimeout() {
    SeleniumTestUtil.temp_setWaitTimeout(576, TimeUnit.SECONDS);
  }

  @Deprecated
  protected void pauseTest() {
    pauseTest(2000, false);
  }

  @Deprecated
  protected void pauseTest(long interval, boolean countdown) {
    SeleniumTestUtil.pauseTest(getClass().getSimpleName(), interval, countdown);
  }

  @Deprecated
  protected void printMessage(String message) {
    SeleniumTestUtil.temp_log(getClass().getSimpleName(), message);
  }

  @Deprecated
  public String getTimestamp() {
    return SeleniumTestUtil.temp_getTimestamp();
  }

  // Provide setUpClass, setUp, tearDown, and tearDownClass methods used by
  // JUnit and their default implementation.

  @BeforeClass
  public void setUpClass() {
  }

  @Before
  public void setUp() {
    driver = SeleniumTestUtil.getDriver();
    wait = SeleniumTestUtil.getWait();
    lookup = SeleniumTestUtil.getLookup();

    logger.info(getClass().getSimpleName() + " begin");
  }

  @After
  public void tearDown() {
    logger.info(getClass().getSimpleName() + " ended");

    // Pause before executing the next test

    // pauseTest(1000, true);
    // DriverFactory.totalWaitedTime += 1000;
  }

  @AfterClass
  public void tearDownClass() {
  }

}
