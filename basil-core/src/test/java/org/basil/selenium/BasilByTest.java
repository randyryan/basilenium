/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;

/**
 * BasilByTest
 *
 * @author ryan131
 * @since Apr 15, 2015, 11:51:01 PM
 */
public class BasilByTest {

  @Test
  public void test() {
    By id = By.id("element_by_id");
    Assert.assertEquals(".//*[@id='element_by_id']", Basil.from(id).getXPath());
    By name = By.name("element_by_name");
    Assert.assertEquals(".//*[@name='element_by_name']", Basil.from(name).getXPath());
    By tagName = By.tagName("img");
    Assert.assertEquals(".//img", Basil.from(tagName).getXPath());
    By className = By.className("content");
    Assert.assertEquals(".//*[contains(concat(' ', normalize-space(@class), ' '), ' content ')]",
        Basil.from(className).getXPath());
  }

}
