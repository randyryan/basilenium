/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.ui;

/**
 * Select widget
 *
 * @author ryan131
 * @since Feb 1, 2016, 7:55:14 PM
 */
public interface Select extends Widget {

  void selectOption(Option option);

  Option getSelectedOption();

  /**
   * Models a Select option.
   *
   * @author ryan131
   * @since Feb 4, 2016, 8:06:39 PM
   */
  public interface Option {

    int index();

    String value();

//    void select();
//
//    boolean isSelected();

  }

}
