/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.page;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.basil.selenium.base.DriverFactory;
import org.basil.selenium.page.Find.Filter;
import org.basil.selenium.page.Find.FilterConditions;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

/**
 * FindTest
 *
 * @author ryan131
 * @since Apr 18, 2015, 9:50:15 AM
 */
public class FindTest {

  private static WebDriver driver;

  @BeforeClass
  public static void setUpClass() {
    driver = DriverFactory.getWebDriver();

    String wautUrl = null;
    try {
      wautUrl = Thread.currentThread().getContextClassLoader().getResource("WAUT.html")
          .toURI().toURL().toExternalForm();
    } catch (URISyntaxException use) {
    } catch (MalformedURLException mue) {
    }

    driver.get(wautUrl);
  }

  @Test
  public void test() {
    Find.Result result = Find.in(driver).byTagName("a");
    Assert.assertEquals("Find me on GitHub",
        result.filter(FilterConditions.withPartialText("GitHub")).get().getText());
    Assert.assertEquals("Find me on GitHub",
        result.filter(Filter.withPartialText("GitHub")).get().getText());
    
  }

}
