/**
 * Copyright (c) 2010-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.ui;

/**
 * CheckState
 *
 * @author ryan131
 * @since Dec 17, 2018, 5:07:18 PM
 */
public enum CheckState {

  UNAVAILABLE,
  UNCHECKED,
  MIXED,
  CHECKED;

  public static CheckState fromText(String text) {
    text = text.toLowerCase();
    if (text.equals("true") || text.equals("yes")) {
      return CheckState.CHECKED;
    }
    if (text.equals("false") || text.equals("no")) {
      return CheckState.UNCHECKED;
    }
    if (text.equals("mixed")) {
      return CheckState.MIXED;
    }
    if (text.equals("unavailable")) {
      return CheckState.UNAVAILABLE;
    }
    throw new IllegalArgumentException("CheckBox.State cannot parse from:\"" + text + "\".");
  }

  public static CheckState fromBoolean(Boolean bool) {
    if (bool) {
      return CheckState.CHECKED;
    } else {
      return CheckState.UNCHECKED;
    }
  }

}