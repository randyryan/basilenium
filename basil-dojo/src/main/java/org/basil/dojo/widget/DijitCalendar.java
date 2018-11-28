/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.dojo.widget;

import java.util.Calendar;

import org.basil.dojo.Dijit;
import org.basil.selenium.ui.Pessimistically;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 * DijitCalendar
 *
 * @author ryan131
 * @since Sep 30, 2017, 10:45:25 AM
 */
public class DijitCalendar extends Dijit {

  private Year year;
  private Month month;
  private Day day;

  // Constructor

  public DijitCalendar(SearchContext context) {
    super(context, By.xpath("//*[contains(@class, 'dijitCalendar') and @widgetid]"));
  }

  public DijitCalendar(SearchContext context, By locator) {
    super(context, locator);
  }

  public DijitCalendar(WebElement element) {
    super(element);
  }

  // Method

  public Year year() {
    if (year == null) {
      year = new Year();
    }
    return year;
  }

  public Month month() {
    if (month == null) {
      month = new Month();
    }
    return month;
  }

  public Day day() {
    if (day == null) {
      day = new Day();
    }
    return day;
  }

  public void select(Calendar calendar) {
    year().select(calendar);
    month().select(calendar);
    day().select(calendar);
  }

  public Calendar getSelected() {
    Calendar.Builder calendar = new Calendar.Builder();
    calendar.set(Calendar.YEAR, year().getSelected());
    calendar.set(Calendar.MONTH, month().getSelected());
    calendar.set(Calendar.DAY_OF_MONTH, day().getSelected());
    return calendar.build();
  }

  public class Year {

    private WebElement previous;
    private WebElement current;
    private WebElement next;

    private Year() {
      previous = findByAttachPoint("previousYearLabelNode");
      current = findByAttachPoint("currentYearLabelNode");
      next = findByAttachPoint("nextYearLabelNode");
    }

    public void select(Calendar calendar) {
      select(calendar.get(Calendar.YEAR));
    }

    public void select(int year) {
      for (int current = getSelected(); current != year; current = getSelected()) {
        if (current == year) {
          return;
        }
        if (current > year) {
          selectPrevious();
        } else {
          selectNext();
        }
      }
    }

    public void selectPrevious() {
      previous.click();
    }

    public void selectNext() {
      next.click();
    }

    public int getSelected() {
      return Integer.parseInt(current.getText());
    }

  }

  public class Month {

    private WebElement dijitDownArrowButton;

    private Month() {
      dijitDownArrowButton = findByClass("dijitDownArrowButton");
    }

    private WebElement getMonthLabel(String popupId, int month) {
      return getDriver().findElement(By.xpath(
          String.format("//div[@id='%s']//div[@month='%d']", popupId, month)));
    }

    private WebElement getMonthLabel(String popupId, String month) {
      return getDriver().findElement(By.xpath(
          String.format("//div[@id='%s']//div[text()='%s']", popupId, month)));
    }

    public void select(Calendar calendar) {
      select(calendar.get(Calendar.MONTH));
    }

    public void select(int month) {
      String popupId = Pessimistically.clickGetAttribute(dijitDownArrowButton, "aria-owns");
      getMonthLabel(popupId, month).click();
    }

    public void select(String month) {
      String popupId = Pessimistically.clickGetAttribute(dijitDownArrowButton, "aria-owns");
      getMonthLabel(popupId, month).click();
    }

    public int getSelected() {
      switch (findByClass("dijitCalendarCurrentMonthLabel").getText()) {
      case "January":
        return Calendar.JANUARY;
      case "Febuary":
        return Calendar.FEBRUARY;
      case "March":
        return Calendar.MARCH;
      case "April":
        return Calendar.APRIL;
      case "May":
        return Calendar.MAY;
      case "June":
        return Calendar.JUNE;
      case "July":
        return Calendar.JULY;
      case "August":
        return Calendar.AUGUST;
      case "September":
        return Calendar.SEPTEMBER;
      case "October":
        return Calendar.OCTOBER;
      case "November":
        return Calendar.NOVEMBER;
      case "December":
        return Calendar.DECEMBER;
      }
      throw new IllegalArgumentException("Cannot determine current month.");
    }

  }

  public class Day {

    public void select(Calendar calendar) {
      select(calendar.get(Calendar.DAY_OF_MONTH));
    }

    public void select(int day) {
      findByXPath("//td[contains(@class, 'CurrentMonth')][span[text()='" + day + "']]").click();
    }

    public int getSelected() {
      return Integer.parseInt(findByXPath("//td[contains(@class, 'CurrentDate')]").getText());
    }

  }

}
