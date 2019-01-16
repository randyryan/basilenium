/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.dojo.widget;

import org.basil.dojo.Dijit;
import org.basil.selenium.ui.Pessimistically;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 * DijitTab
 *
 * @author ryan131
 * @since Jan 13, 2019, 8:51:49 PM
 */
public class DijitTab extends Dijit {

  // Constructor

  public DijitTab(SearchContext context, String text) {
    this(context, "", text);
  }

  public DijitTab(SearchContext context, String rootXPath, String text) {
    super(context, By.xpath(rootXPath +
        "//*[contains(@class, 'dijitTab') and @widgetid]" +
        "[span[@class='tabLabel'][text()='" + text + "']]"));
  }

  public DijitTab(SearchContext context, By locator) {
    super(context, locator);
  }

  public DijitTab(WebElement element) {
    super(element);
  }

  // Methods

  public boolean isChecked() {
    return hasClass("dijitTabChecked");
  }

  public void check() {
    Pessimistically.clickAndHasClass(this, "dijitTabChecked");
  }

}
