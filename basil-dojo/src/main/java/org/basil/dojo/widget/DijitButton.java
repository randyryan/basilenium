/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.dojo.widget;

import org.basil.dojo.Dijit;
import org.basil.selenium.page.PageObject;
import org.openqa.selenium.WebElement;

/**
 * DijitButton
 *
 * @author ryan131
 * @since Jul 30, 2017, 4:42:18 PM
 */
public class DijitButton extends Dijit {

  private boolean closeParentOnClick;
  private DefaultAction defaultAction;

  public DijitButton(WebElement element, boolean closeParentOnClick) {
    super(element);
    this.closeParentOnClick = closeParentOnClick;
    defaultAction = new DefaultAction();
    defaultAction.start();
  }

  // DijitElement

  @Override
  public boolean isEnabled() {
    defaultAction.abort();
    return !hasClass("dijitButtonDisabled");
  }

  @Override
  public boolean isDisabled() {
    defaultAction.abort();
    return hasClass("dijitButtonDisabled");
  }

  @Override
  public boolean isFocused() {
    defaultAction.abort();
    return hasClass("dijitButtonFocused");
  }

  // Close

  public void setCloseParentOnClick(boolean closeParentOnClick) {
    this.closeParentOnClick = closeParentOnClick;
  }

  @Override
  public void click() {
    super.click();
    if (closeParentOnClick) {
      ((PageObject) getParent()).waitUntilInvisible();
    }
  }

  public void test() {
    if (closeParentOnClick) {
      super.click();
    } else {
      throw new UnsupportedOperationException("This button does not trigger a validation.");
    }
  }

  // Default-action

  private class DefaultAction implements Runnable {

    private boolean enabled = false;
    private Thread thread;

    private DefaultAction() {
      if (enabled) {
        thread = new Thread(this);
      }
    }

    public void start() {
      if (enabled) {
        thread.start();
      }
    }

    @Override
    public void run() {
      try {
        Thread.sleep(250);
        click(); // The default action
      } catch (InterruptedException ie) {
      }
    }

    public void abort() {
      if (enabled) {
        thread.interrupt();
      }
    }

  }

}
