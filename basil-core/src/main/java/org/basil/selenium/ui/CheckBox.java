/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.ui;


/**
 * CheckBox
 *
 * @author ryan131
 * @since Jun 14, 2014, 1:34:35 PM
 */
public interface CheckBox {

  public enum State {

    UNAVAILABLE,
    UNCHECKED,
    MIXED,
    CHECKED;

    public static State fromText(String text) {
      text = text.toLowerCase();
      if (text.equals("true") || text.equals("yes")) {
        return State.CHECKED;
      }
      if (text.equals("false") || text.equals("no")) {
        return State.UNCHECKED;
      }
      if (text.equals("mixed")) {
        return State.MIXED;
      }
      if (text.equals("unavailable")) {
        return State.UNAVAILABLE;
      }
      throw new IllegalArgumentException("CheckBox.State cannot parse from:\"" + text + "\".");
    }

    public static State fromBoolean(Boolean bool) {
      if (bool) {
        return State.CHECKED;
      } else {
        return State.UNCHECKED;
      }
    }

  }

}
