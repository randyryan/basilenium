/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.openqa.selenium.remote;

import org.basil.selenium.BasilContext;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.internal.FindsByClassName;
import org.openqa.selenium.internal.FindsByCssSelector;
import org.openqa.selenium.internal.FindsById;
import org.openqa.selenium.internal.FindsByLinkText;
import org.openqa.selenium.internal.FindsByName;
import org.openqa.selenium.internal.FindsByTagName;
import org.openqa.selenium.internal.FindsByXPath;

/**
 * A dummy RemoteWebDriver for BasilWebDriver to extend. It also helps BasilWebDriver to cheat the
 * type as RemoteWebDriver so it can be accepted by other class uses RemoteWebDriver explicitly.
 */
@Augmentable
public abstract class RemoteWebDriver extends BasilContext implements WebDriver, JavascriptExecutor,
    FindsById, FindsByClassName, FindsByLinkText, FindsByName,
    FindsByCssSelector, FindsByTagName, FindsByXPath,
    HasInputDevices, HasCapabilities, TakesScreenshot {
}
