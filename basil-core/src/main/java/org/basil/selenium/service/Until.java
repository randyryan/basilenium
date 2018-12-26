/**
 * Copyright (c) 2015-2016 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.service;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.slf4j.LoggerFactory;
import org.spearmint.util.Sleeper;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;

/**
 * Until - a customized Wait
 *
 * @author ryan131
 * @since Dec 25, 2018, 5:13:38 PM
 */
public class Until {

  private SearchContext context;
  private final WebElement element;
  private final Clicker clicker;

  private int times;
  private long timeout;
  private long interval;

  public Until(WebElement element, Clicker clicker) {
    this(element, clicker, null);
  }

  public Until(WebElement element, Clicker clicker, SearchContext context) {
    this.element = element;
    this.clicker = clicker;
    this.context = context;
  }

  public Until times(int times) {
    Preconditions.checkArgument(times > 0);
    this.times = times;
    return this;
  }

  public Until every(long millis) {
    Preconditions.checkArgument(millis > 0);
    this.interval = millis;
    return this;
  }

  public Until every(long duration, TimeUnit unit) {
    return every(unit.toMillis(duration));
  }

  public Until timeout(long millis) {
    Preconditions.checkArgument(millis > 0);
    this.timeout = millis;
    return this;
  }

  public Until timeout(long duration, TimeUnit unit) {
    return timeout(unit.toMillis(duration));
  }

  @SuppressWarnings("unchecked")
  public <S extends SearchContext, V> V until(final Function<S, V> condition) {
    S input = null;
    try {
      input = (S) context;
    } catch (ClassCastException cce) {
      LoggerFactory.getLogger(Until.class).warn(String.format(
          "The input %s isn't compatible with the condition %s. Defaulting the input to null.",
          context, condition));
    }
    return until(condition, input);
  }

  public <S extends SearchContext, V> V until(final Function<S, V> condition, final S input) {
    Scheduler scheduler = new Scheduler();
    // clicker.click(element); Moved to the bottom
    while (true) {
      V value = null;
      try {
        value = condition.apply(input);
      } catch (NullPointerException npe) {
        throw new IllegalArgumentException(String.format(
            "The input %s cannot be used with condition %s, specify a condition does not use " +
            "this input or specify a valid input for this condition",
            input, condition));
      }
      if (value instanceof Boolean) {
        if (value.equals(Boolean.TRUE)) {
          return value;
        }
      } else if (value != null) {
        return value;
      }
      Sleeper.sleepSilently(interval);

      try {
        scheduler.interruptIfTimesOut();
      } catch (InterruptedException ie) {
        break;
      }
    }
    clicker.click(element); // The clicking is placed after applying the condition because there
    // is already a clicking before the creation of the Until, at the WebElementUtil.click(),
    // placing it at the head will very likely to result a double click.
    throw new TimeoutException();
  }

  public class Scheduler {

    private long start;

    private Scheduler() {
      checkState();
      if (timeout > 0) {
        start = System.currentTimeMillis();
      } // No else needed. if the start was not set, it runs in times mode.
    }

    private void checkState() {
      Preconditions.checkState(
          !(times == 0 && timeout == 0), "Either times or timeout must be set.");
      Preconditions.checkState(
          !(times > 0 && timeout > 0), "The times and timeout cannot be set at the same time.");
      Preconditions.checkState(interval > 0, "The interval must be set.");
    }

    public void interruptIfTimesOut() throws InterruptedException {
      if ((start > 0 && System.currentTimeMillis() > start + timeout) ||
          (start == 0 && times == 0)) {
        throw new InterruptedException();
      } else {
        times--;
      }
    }

  }

}
