/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.service;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;

/**
 * XPathServiceTest
 *
 * @author ryan131
 * @since Apr 15, 2015, 10:42:23 PM
 */
public class XPathServiceTest {

  @Test
  public void test() {
    Assert.assertEquals(
        "//a[@id='next']/preceding-sibling::a",
        XPathUtil.append(By.xpath("//a[@id='next']"), "preceding-sibling::a"));
  }

}
