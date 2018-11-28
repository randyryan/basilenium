/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.ui;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.basil.Config.Value.WEB_ELEMENT_LOADING_UNAVAILABLE_AS_IDLE;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 * Loading - represents a loading indicator.
 *
 * @author ryan131
 * @since Dec 6, 2016, 4:35:24 PM
 */
public class Loading {

  protected By locator;
  protected WebElement loading;

  private Status status;
  private SearchContextWait wait;

  public Loading(SearchContext searchContext, By locator, long timeoutInSeconds) {
    this.locator = checkNotNull(locator);

    status = new Status();
    wait = new SearchContextWait(checkNotNull(searchContext), timeoutInSeconds);
  }

  public Loading(SearchContext searchContext, WebElement loading, long timeoutInSeconds) {
    this.loading = checkNotNull(loading);
 
    status = new Status();
    wait = new SearchContextWait(checkNotNull(searchContext), timeoutInSeconds);
  }

  /**
   * Use settings from another loading instance.
   */
  protected void use(Loading anotherLoading) {
    this.locator = anotherLoading.locator;
    this.loading = anotherLoading.loading;
    this.wait = anotherLoading.wait;
  }

  // Wait methods

  public void setTimeout(long timeoutInSeconds) {
    wait.withTimeout(timeoutInSeconds, TimeUnit.SECONDS);
  }

  private WebElement waitUntilBegin() {
    wait.pollingEvery(50, TimeUnit.MILLISECONDS);
    if (status.isUnavailable()) {
      if (WEB_ELEMENT_LOADING_UNAVAILABLE_AS_IDLE) {
        return loading = wait.until(ExtendedConditions.visibilityOfElementLocated(locator));
      } else {
        throw new NoSuchElementException("The loading is unavailable.");
      }
    }
    if (status.isIdle()) {
      return wait.until(ExtendedConditions.visibilityOf(loading));
    }
    // status.isInProgress
    return loading;
  }

  private WebElement waitUntilFinish() {
    return wait.pollingEvery(500, TimeUnit.MILLISECONDS).until(ExtendedConditions.invisibilityOf(loading));
  }

  // Smart waits

  /**
   * Performs a wait only when needed (when it's actually loading)
   */
  public WebElement waitImplicitly() {
    return waitImplicitlyInSeconds(0);
  }

  public WebElement waitImplicitlyInSeconds(int seconds) {
    if (status.isUnavailable()) {
      if (WEB_ELEMENT_LOADING_UNAVAILABLE_AS_IDLE) {
        return null;
      } else {
        throw new NoSuchElementException("The loading is unavailable.");
      }
    }
    if (status.isIdle()) {
      return loading;
    }
    // status.isInProgress
    if (seconds > 0) {
      try {
        Thread.sleep(seconds * 1000);
      } catch (InterruptedException ie) {}
    }
    return waitUntilFinish();
  }

  /**
   * Performs a explicit wait on begin then wait for loading to finish
   */
  public WebElement waitExplicitly() {
    waitUntilBegin();
    return waitUntilFinish();
  }

  public boolean isUnavailable() {
    return status.isUnavailable();
  }

  public boolean isIdle() {
    return status.isIdle();
  }

  public boolean isInProgress() {
    return status.isInProgress();
  }

  /**
   * Determine the current status of the loading indication element.
   */
  private class Status {

    /**
     * The element is yet to be present in the HTML document.
     */
    private boolean isUnavailable() {
      return loading == null;
    }

    /**
     * The element is present in the HTML document but not visible
     */
    private boolean isIdle() {
      return !loading.isDisplayed();
    }

    /**
     * The element is present in the HTML document and is visible
     */
    private boolean isInProgress() {
      return loading.isDisplayed();
    }

  }

}
