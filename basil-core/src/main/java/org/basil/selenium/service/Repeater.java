/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.interactions.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spearmint.util.Counter;
import org.spearmint.util.Sleeper;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Sets;

/**
 * Repeater - A highly customized {@link org.openqa.selenium.support.ui.FluentWait} more focused on
 *            repeatedly performing {@link org.openqa.selenium.interactions.Action}.
 *
 * @author ryan131
 * @since Dec 25, 2018, 5:13:38 PM
 */
public class Repeater {

  private static final Logger logger = LoggerFactory.getLogger(Repeater.class);

  private final Action action;
  private SearchContext context;

  private int times;
  private Duration timeout;
  private Duration interval;

  private Set<Class<? extends Throwable>> ignoredExceptions = Sets.newHashSet();

  public Repeater(Action action) {
    this(action, null);
  }

  public Repeater(Action action, SearchContext context) {
    this.context = context;
    this.action = action;
  }

  public Repeater times(int times) {
    Preconditions.checkArgument(times > 0);
    this.times = times;
    return this;
  }

  public Repeater every(long millis) {
    Preconditions.checkArgument(millis > 0);
    this.interval = Duration.ofMillis(millis);
    return this;
  }

  public Repeater every(Duration interval) {
    this.interval = interval;
    return this;
  }

  public Repeater timeout(long millis) {
    Preconditions.checkArgument(millis > 0);
    this.timeout = Duration.ofMillis(millis);
    return this;
  }

  public Repeater timeout(Duration timeout) {
    this.timeout = timeout;
    return this;
  }

  public Repeater ignore(Class<? extends Throwable> ignoredException) {
    ignoredExceptions.add(ignoredException);
    return this;
  }

  private Throwable propagateIfUnignored(Throwable throwable) {
    for (Class<? extends Throwable> ignoredException : ignoredExceptions) {
      if (ignoredException.isInstance(throwable)) {
        return throwable;
      }
    }
    Throwables.throwIfUnchecked(throwable);
    throw new RuntimeException(throwable);
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
    Throwable lastException = null;
    while (true) {
      try {
        V value = condition.apply(input);
        if (value instanceof Boolean) {
          if (value.equals(Boolean.TRUE)) {
            return value;
          }
        } else if (value != null) {
          return value;
        }

        lastException = null;
      } catch (Throwable throwable) {
        if (input == null && NullPointerException.class.isInstance(throwable)) {
          throw new IllegalArgumentException(String.format(
              "The input %s cannot be used with condition %s, Please specify a condition does not " +
              "use input or specify a valid input for this condition.", input, condition));
        }
        lastException = propagateIfUnignored(throwable);
      }

      try {
        scheduler.interruptIfTimesOut();
      } catch (InterruptedException ie) {
        break;
      }

      Sleeper.sleepSilently(interval); // The action should be performed in the end because
      action.perform(); // it has already performed once before the creation of this Repeater.
    }
    throw new TimeoutException(lastException);
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
        if (count != null) {
          count.count();
        }
      }
    }

  }

}
