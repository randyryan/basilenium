/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.dojo.widget;

import org.basil.dojo.Dijit;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 * DijitComboBoxMenu
 *
 * @author ryan131
 * @since Jan 13, 2019, 7:16:25 PM
 */
public class DijitComboBoxMenu extends Dijit {

  private WebElement previous;
  private WebElement next;

  // Constructor

  public DijitComboBoxMenu(SearchContext context) {
    super(context, By.xpath("//*[contains(@class, 'dijitComboBoxMenu') and @widgetid]"));
  }

  public DijitComboBoxMenu(SearchContext context, String id) {
    super(context, By.xpath("//*[@id='" + id + "']"));
  }

  public DijitComboBoxMenu(SearchContext context, By locator) {
    super(context, locator);
  }

  public DijitComboBoxMenu(WebElement element) {
    super(element);
  }

  // Method

  public void selectPrevious() {
    if (previous == null) {
      previous = findByClass("dijitMenuPreviousButton");
    }
    previous.click();
  }

  public void selectNext() {
    if (next == null) {
      next = findByClass("dijitMenuNextButton");
    }
    next.click();
  }

  public void selectItem(int index) {
    findByXPath("//div[contains(@class, 'dijitMenuItem')][" + index + "]").click();
  }

  public void selectItem(String name) {
    findByXPath("//div[contains(@class, 'dijitMenuItem')][text()='" + name + "']").click();
  }

}
