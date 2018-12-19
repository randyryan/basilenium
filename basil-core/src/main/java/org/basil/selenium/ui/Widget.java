/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.ui;

import org.basil.selenium.service.WebElementService;
import org.basil.selenium.service.WebElementUtil;
import org.openqa.selenium.WebElement;

import com.google.common.base.Strings;

/**
 * Widget - Mainly functions as a marker type with only few methods from WebElement. This
 *          object doesn't extends WebElement nor WrapsElement, for the sake of simplicity.
 *
 * @author ryan131
 * @since Dec 18, 2013, 9:02:40 PM
 */
public interface Widget {

  WebElement getWebElement();

  default void sendKeys(CharSequence... keysToSend) {
    WebElementUtil.sendKeys(getWebElement(), keysToSend);
  }

  default void clear() {
    WebElementUtil.clear(getWebElement());
  }

  default String getAttribute(String name) {
    return WebElementUtil.getAttribute(getWebElement(), name);
  }

  default String getId() {
    return WebElementUtil.getId(getWebElement());
  }

  default String getName() {
    return getAttribute("name"); // TODO This method is a candidate for WebElementUtil
  }

  default boolean isDisabled() {
    return WebElementUtil.isDisabled(getWebElement());
  }

  default boolean hasValue() {
    return !Strings.isNullOrEmpty(getWebElement().getAttribute("value"));
  }

  default WebElementService.ValueConverter getValue() {
    return WebElementUtil.getValue(getWebElement());
  }

}
