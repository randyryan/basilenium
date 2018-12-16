/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.ui;

import static org.basil.Config.WAIT_INTERVAL;
import static org.basil.Config.WAIT_NOT_FOUND;
import static org.basil.Config.WAIT_TIMEOUT;
import static org.basil.selenium.ui.SearchContextWait.FluentWaitAdapter.adapt;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.ui.Clock;
import org.openqa.selenium.support.ui.Duration;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Sleeper;
import org.openqa.selenium.support.ui.SystemClock;
import org.spearmint.Spearmint;

import com.google.common.base.Supplier;

/**
 * A specialization of {@link FluentWait} that uses SearchContext instances.
 *
 * @author ryan131
 * @since Dec 16, 2015, 8:05:07 PM
 */
public class SearchContextWait extends FluentWait<SearchContext> {

  // searchContext
  // searchContext,                 timeout (seconds)
  // searchContext,                 timeout (seconds), interval (milliseconds)
  // searchContext, clock, sleeper
  // searchContext, clock, sleeper, timeout (seconds), interval (milliseconds)

  public SearchContextWait(SearchContext context) {
    this(context, new SystemClock(), Sleeper.SYSTEM_SLEEPER, WAIT_TIMEOUT, WAIT_INTERVAL);
  }

  public SearchContextWait(SearchContext context, long timeout) {
    this(context, new SystemClock(), Sleeper.SYSTEM_SLEEPER, timeout, WAIT_INTERVAL);
  }

  public SearchContextWait(SearchContext context, long timeout, long interval) {
    this(context, new SystemClock(), Sleeper.SYSTEM_SLEEPER, timeout, interval);
  }

  public SearchContextWait(SearchContext context, Clock clock, Sleeper sleeper) {
    super(context, clock, sleeper);
  }

  public SearchContextWait(SearchContext context, Clock clock, Sleeper sleeper, long timeout, long interval) {
    super(context, clock, sleeper);
    withTimeout(timeout, TimeUnit.SECONDS);
    pollingEvery(interval, TimeUnit.MILLISECONDS);
    if (WAIT_NOT_FOUND.equals(NotFound.IGNORE)) {
      ignoring(NotFoundException.class);
    }
  }

  /**
   * Constructs a {@code SearchContextWait} from a {@link FluentWait} and alter its input to the
   * specified {@link SearchContext} object. <b>EXPERIMENTAL</b>.
   */
  public SearchContextWait(SearchContext context, FluentWait<? extends SearchContext> wait) {
    super(context, adapt(wait).clock(), adapt(wait).sleeper());
    withTimeout(adapt(wait).timeout().in(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).
    pollingEvery(adapt(wait).interval().in(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).
    ignoreAll(adapt(wait).ignoredExceptions()).withMessage(adapt(wait).messageSupplier());
  }

  /**
   * Constructs a {@code SearchContextWait} from a {@link FluentWait} object. <b>EXPERIMENTAL</b>.
   */
  public SearchContextWait(FluentWait<? extends SearchContext> wait) {
    this(adapt(wait).input(), wait);
  }

  static class FluentWaitAdapter<T> {
    private FluentWait<T> wait;
    FluentWaitAdapter(FluentWait<T> wait) {
      this.wait = wait;
    }
    T input() {
      return Spearmint.reflect(wait).field("input").get();
    }
    Clock clock() {
      return Spearmint.reflect(wait).field("clock").get();
    }
    Sleeper sleeper() {
      return Spearmint.reflect(wait).field("sleeper").get();
    }
    Duration timeout() {
      return Spearmint.reflect(wait).field("timeout").get();
    }
    Duration interval() {
      return Spearmint.reflect(wait).field("interval").get();
    }
    Supplier<String> messageSupplier() {
      return Spearmint.reflect(wait).field("messageSupplier").get();
    }
    List<Class<? extends Throwable>> ignoredExceptions() {
      return Spearmint.reflect(wait).field("ignoredExceptions").get();
    }
    static <T> FluentWaitAdapter<T> adapt(FluentWait<T> wait) {
      return new FluentWaitAdapter<T>(wait);
    }
  }

  public enum NotFound {

    IGNORE,
    THROW;

    public static NotFound fromString(String string) {
      for (NotFound notFound : values()) {
        if (string.equalsIgnoreCase(notFound.name())) {
          return notFound;
        }
      }
      throw new IllegalArgumentException("NotFound cannot be parsed from: \"" + string + "\".");
    }

  }

}
