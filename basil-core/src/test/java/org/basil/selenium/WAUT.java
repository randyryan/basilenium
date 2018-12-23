/**
 * Copyright (c) 2015-2016 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * WAUT
 *
 * @author ryan131
 * @since Oct 23, 2013, 6:09:52 PM
 */
public class WAUT implements SearchContext {

  private WebDriver driver;
  private WebDriverWait wait;

  public WAUT(WebDriver driver) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, 5);
  }

  public WebDriver getWebDriver() {
    return driver;
  }

  public WAUT start() {
    String url = null;
    try {
      url = Thread.currentThread().getContextClassLoader().getResource("WAUT.html")
          .toURI().toURL().toExternalForm();
    } catch (URISyntaxException use) {
    } catch (MalformedURLException mue) {
    }
    driver.get(url);

    return this;
  }

  public WAUT openFormTab() {
    driver.findElement(By.id("nav-form-tab")).click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-form")));

    return this;
  }

  @Override
  public List<WebElement> findElements(By by) {
    return driver.findElements(by);
  }

  @Override
  public WebElement findElement(By by) {
    return driver.findElement(by);
  }

  public void exit() {
    driver.close();
  }

}
