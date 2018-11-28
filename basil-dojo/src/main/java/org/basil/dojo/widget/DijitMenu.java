/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.dojo.widget;

import java.util.Map;

import org.basil.dojo.Dijit;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import com.google.common.collect.Maps;

/**
 * DijitMenu
 *
 * @author ryan131
 * @since Sep 28, 2017, 5:47:14 PM
 */
public class DijitMenu extends Dijit {

  private Map<String, WebElement> menuItems = Maps.newHashMap();

  // Constructor

  public DijitMenu(SearchContext context) {
    super(context, By.xpath("//*[contains(@class, 'dijitMenu') and @widgetid]"));
  }

  public DijitMenu(SearchContext context, By locator) {
    super(context, locator);
  }

  public DijitMenu(WebElement element) {
    super(element);
  }

  // Method

  public void selectItem(int index) {
    findByXPath("//tr[contains(@class, 'dijitMenuItem')][" + index + "]").click();
  }

  public void selectItem(String name) {
    findByXPath(
        "//tr[contains(@class, 'dijitMenuItem')]" +
        "[td[contains(@class, 'dijitMenuItemLabel') and text()='" + name + "']]"
    ).click();
  }

  public String getSelectedItem() {
    return findByXPath(
        "//tr[contains(@class, 'dijitSelectSelectedOption')]" +
        "/td[contains(@class, 'dijitMenuItemLabel')]"
    ).getText();
  }

}
