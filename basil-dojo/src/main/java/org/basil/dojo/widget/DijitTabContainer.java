/**
 * Copyright (c) 2015-2016 Ryan Li Wan. All rights reserved.
 */

package org.basil.dojo.widget;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import com.google.common.collect.Maps;

/**
 * DijitTabContainer
 *
 * @author ryan131
 * @since Jan 16, 2019, 5:18:39 PM
 */
public abstract class DijitTabContainer {
/*

div.dijitTabContainer.dijitTabContainerLeft
  div.dijitTabContainerLeft=tabs or div[role=tablist]
    div.dijitTab
  div.dijitTabPaneWrapper.dijitTabContainerLeft-container
    div.dijitTabContainerLeftChildWrapper or div[role=tabpanel] .dijitVisible.dijitHidden

div.dijitTabContainer.dijitTabContainerTop
  div.dijitTabContainerTop-tabs or div[role=tablist]
    div.dijitTab
  div.dijitTabPaneWrapper.dijitTabContainerTop-container
    div.dijitTabContainerTopChildWrapper or div[role=tabpanel] .dijitVisible.dijitHidden

*/

  protected Type type;
  protected Map<String, Tab> tabs;

  protected void init() {
    tabs = Maps.newHashMap();
  }

  // Constructor

  protected DijitTabContainer(SearchContext parent, Type type) {
    this(parent, type, "");
  }

  protected DijitTabContainer(SearchContext parent, Type type, String rootXPath) {
    this.type = type;
    By locator = By.xpath(rootXPath + type.xpath);
  }

  // Tab methods

  protected Tab findTab(String tabLabel) {
    Tab tab = new Tab(tabLabel);
    tabs.put(tabLabel, tab);
    return tab;
  }

  public Tab tab(String tabLabel) {
    return tabs.getOrDefault(tabLabel, findTab(tabLabel));
  }

  public <TP extends TabPanel> TP tabp(String tabLabel) {
    return null;
  }

  public class Tab {

    protected WebElement element;

    protected Tab(String tabLabel) {
      
    }

    public boolean isChecked() {
      return false;
    }

    public void check() {
      
    }

  }

  public abstract class TabPanel {

    protected Tab tab;

    public void switchTo() {
      tab.check();
    }

  }

  public enum Type {

    Top("//div[contains(@class, 'dijitTabContainer dijitTabContainerTop')]"),
    Left("//div[contains(@class, 'dijitTabContainer dijitTabContainerLeft')]");

    private String xpath;

    private Type(String xpath) {
      this.xpath = xpath;
    }

  }

}
