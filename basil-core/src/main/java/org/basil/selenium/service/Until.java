/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.service;

import java.time.Duration;
import java.time.Instant;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.interactions.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spearmint.util.Counter;
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

  private static final Logger logger = LoggerFactory.getLogger(Until.class);

  private final Action action;
  private SearchContext context;

  private int times;
  private Duration timeout;
  private Duration interval;

  public Until(Action action) {
    this(action, null);
  }

  public Until(Action action, SearchContext context) {
    this.context = context;
    this.action = action;
  }

  public Until times(int times) {
    Preconditions.checkArgument(times > 0);
    this.times = times;
    return this;
  }

  public Until every(long millis) {
    Preconditions.checkArgument(millis > 0);
    this.interval = Duration.ofMillis(millis);
    return this;
  }

  public Until every(Duration interval) {
    this.interval = interval;
    return this;
  }

  public Until timeout(long millis) {
    Preconditions.checkArgument(millis > 0);
    this.timeout = Duration.ofMillis(millis);
    return this;
  }

  public Until timeout(Duration timeout) {
    this.timeout = timeout;
    return this;
  }

  @SuppressWarnings("unchecked")
  public <S extends SearchContext, V> V until(Function<S, V> condition) {
    S input = null;
    try {
      input = (S) context;
    } catch (ClassCastException cce) {
      logger.warn(String.format(
          "The input %s isn't compatible with the condition %s. Defaulting the input to null.",
          context, condition));
    }
    return until(condition, input);
  }

  public <S extends SearchContext, V> V until(Function<S, V> condition, S input) {
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
      action.perform(); // The action should be performed after applying the condition because
      // there is already a clicking happened before the creation of the Until, at the
      // WebElementUtil.clickRepeatedly(); Placing it at the head will very likely to result
      // a double click.

      try {
        scheduler.interruptIfTimesOut();
      } catch (InterruptedException ie) {
        break;
      }
    }
    throw new TimeoutException();
  }

  public class Scheduler {

    private Instant start;
    private Counter count;

    private Scheduler() {
      checkState();
      if (timeout != null) {
        start = Instant.now();
      } else {
        count = Counter.countTo(times);
      }
    }

    private void checkState() {
      Preconditions.checkState(
          !(times == 0 && timeout == null), "Either times or timeout must be set.");
      Preconditions.checkState(
          !(times > 0 && timeout != null), "The times and timeout cannot be set at the same time.");
      Preconditions.checkState(interval != null, "The interval must be set.");
    }

    public void interruptIfTimesOut() throws InterruptedException {
      if ((start != null && Instant.now().isAfter(start.plus(timeout))) ||
          (count != null && count.isOnTarget())) {
        throw new InterruptedException();
      } else {
        count.count();
      }
    }

  }

}
