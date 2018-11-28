/**
 * Copyright (c) 2010-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil;

import java.net.URL;
import java.util.List;

import org.basil.selenium.base.BrowserType;
import org.basil.selenium.base.DriverType;
import org.basil.selenium.base.DriverUtil;
import org.basil.selenium.page.PageObject.TimerStyle;
import org.basil.selenium.ui.ExtendedConditions.Precondition;
import org.basil.selenium.ui.SearchContextWait.NotFound;
import org.openqa.selenium.Dimension;
import org.spearmint.config.app.PropertyUtil;
import org.spearmint.io.URLs;
import org.spearmint.primitive.Booleans;

/**
 * Basil configuration
 *
 * @author ryan131
 * @since Apr 25, 2013, 7:43:33 PM
 */
public interface Config {

  public interface Key {

    // Browser

    String BROWSER_TYPE = "browser.type";

    String BROWSER_WINDOW_MAXIMIZED = "browser.window.maximized";

    String BROWSER_WINDOW_DIMENSION = "browser.window.dimension";

    // WebDriver

    String WEB_DRIVER_TYPE = "web.driver.type";

    String WEB_DRIVER_REMOTE_URL = "web.driver.remote.url";

    String WEB_DRIVER_EXECUTABLE_CHROME = "web.driver.executable.chrome";

    String WEB_DRIVER_EXECUTABLE_IE = "web.driver.executable.ie";

    // WebDriver > Wait

    String WEB_DRIVER_WAIT_TIMEOUT = "web.driver.wait.timeout";

    String WEB_DRIVER_WAIT_POLL = "web.driver.wait.poll";

    String WEB_DRIVER_WAIT_UTILITY = "web.driver.wait.utility";

    // WebElement

    String WEB_ELEMENT_ENABLE_LATENCY = "web.element.enable.latency";

    String WEB_ELEMENT_VALIDATION_EXCEPTION = "web.element.validation.exception";

    String WEB_ELEMENT_VALIDATION_IGNORED_TYPES = "web.element.ignored.types";

    String WEB_ELEMENT_INTERACTIBILITY_PRECONDITION = "web.element.interactibility.precondition";

    String WEB_ELEMENT_LOADING_UNAVAILABLE_AS_IDLE = "web.element.loading.unavailable.as.idle";

    // PageObject

    String PAGE_OBJECT_LOCATE_TIMEOUT = "page.object.locate.timeout";

    String PAGE_OBJECT_LOCATE_BY_ID = "page.object.locate.by.id";

    String PAGE_OBJECT_TIMER_STYLE = "page.object.timer.style";

    String PAGE_OBJECT_TIMER_31S = "page.object.timer.31s";

    // SearchContextWait

    String WAIT_INTERVAL = "wait.interval";

    String WAIT_TIMEOUT = "wait.timeout";

    String WAIT_NOT_FOUND = "wait.not.found";

  }

  public static class Value {

    // Browser

    public static final BrowserType BROWSER_TYPE = DriverUtil.getBrowserType(PropertyUtil.getString(Key.BROWSER_TYPE));
 
    public static boolean BROWSER_WINDOW_MAXIMIZED = PropertyUtil.getBoolean(Key.BROWSER_WINDOW_MAXIMIZED);

    public static Dimension BROWSER_WINDOW_DIMENSION = DriverUtil.getBrowserSize(PropertyUtil.getIntArray(Key.BROWSER_WINDOW_DIMENSION));

    // WebDriver

    public static final DriverType WEB_DRIVER_TYPE = DriverUtil.getDriverType(PropertyUtil.getString(Key.WEB_DRIVER_TYPE));

    public static final URL WEB_DRIVER_REMOTE_URL = URLs.fromStringSilently(PropertyUtil.getString(Key.WEB_DRIVER_REMOTE_URL));

    public static final String WEB_DRIVER_EXECUTABLE_CHROME = PropertyUtil.getString(Key.WEB_DRIVER_EXECUTABLE_CHROME);

    public static final String WEB_DRIVER_EXECUTABLE_IE = PropertyUtil.getString(Key.WEB_DRIVER_EXECUTABLE_IE);

    // WebDriver > Wait

    public static long WEB_DRIVER_WAIT_TIMEOUT = PropertyUtil.getLong(Key.WEB_DRIVER_WAIT_TIMEOUT);

    public static long WEB_DRIVER_WAIT_POLL = PropertyUtil.getLong(Key.WEB_DRIVER_WAIT_POLL);

    public static final long WEB_DRIVER_WAIT_UTILITY = PropertyUtil.getLong(Key.WEB_DRIVER_WAIT_UTILITY);

    // WebElement

    public static final long WEB_ELEMENT_ENABLE_LATENCY = PropertyUtil.getLong(Key.WEB_ELEMENT_ENABLE_LATENCY);

    public static final boolean WEB_ELEMENT_VALIDATION_EXCEPTION = Booleans.fromString(PropertyUtil.getString(Key.WEB_ELEMENT_VALIDATION_EXCEPTION));

    public static final List<String> WEB_ELEMENT_VALIDATION_IGNORED_TYPES = PropertyUtil.getStringList(Key.WEB_ELEMENT_VALIDATION_IGNORED_TYPES);

    public static final Precondition WEB_ELEMENT_INTERACTIBILITY_PRECONDITION = Precondition.fromString(PropertyUtil.getString(Key.WEB_ELEMENT_INTERACTIBILITY_PRECONDITION));

    public static final boolean WEB_ELEMENT_LOADING_UNAVAILABLE_AS_IDLE = Booleans.fromString(PropertyUtil.getString(Key.WEB_ELEMENT_LOADING_UNAVAILABLE_AS_IDLE));

    // PageObject

    public static final long PAGE_OBJECT_LOCATE_TIMEOUT = PropertyUtil.getLong(Key.PAGE_OBJECT_LOCATE_TIMEOUT);

    public static final boolean PAGE_OBJECT_LOCATE_BY_ID = PropertyUtil.getBoolean(Key.PAGE_OBJECT_LOCATE_BY_ID);

    public static final TimerStyle PAGE_OBJECT_TIMER_STYLE = TimerStyle.fromText(PropertyUtil.getString(Key.PAGE_OBJECT_TIMER_STYLE));

    public static final boolean PAGE_OBJECT_TIMER_31S = Booleans.fromString(PropertyUtil.getString(Key.PAGE_OBJECT_TIMER_31S));

    // SearchContextWait

    public static final long WAIT_INTERVAL = PropertyUtil.getLong(Key.WAIT_INTERVAL);

    public static final long WAIT_TIMEOUT = PropertyUtil.getLong(Key.WAIT_TIMEOUT);

    public static final NotFound WAIT_NOT_FOUND = NotFound.fromString(PropertyUtil.getString(Key.WAIT_NOT_FOUND));

  }

}
