/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.test;

import java.io.Serializable;
import java.util.Set;

/**
 * An implementation-agnostic Test.
 *
 * @author ryan131
 * @since Sep 26, 2015, 11:38:29 AM
 */
public abstract class AbstractTest implements Test, TestConfig, Serializable {

  private static final long serialVersionUID = 1L;

  private transient TestConfig config;

  public AbstractTest() {}

  /**
   * A convenience method which can be overridden so that there's no need to
   * call <code>super.setup(config)</code>.
   */
  public void setup() throws TestException {}

  @Override
  public void setUp(TestConfig config) throws TestException {
    this.config = config;
    this.setup();
  }

  @Override
  public TestConfig getTestConfig() {
    return config;
  }

  @Override
  public String getTestName() {
    return getTestConfig().getTestName();
  }

  @Override
  public String getTestDescription() {
    return "";
  }

  @Override
  public TestContext getTestContext() {
    return getTestConfig().getTestContext();
  }

  @Override
  public String getProperty(String name) {
    return getTestConfig().getProperty(name);
  }

  @Override
  public Set<String> propertyKeySet() {
    return getTestConfig().propertyKeySet();
  }

  /**
   * Writes the specified message to a test log file, prepended by the
   * test's name.
   */
  public void log(String message) {
    getTestContext().log(getTestName() + ": " + message);
  }

  /**
   * Writes an explanatory message and a stack trace for a given
   * <code>Throwable</code> exception to the test log file, prepended by
   * the test's name.
   */
  public void log(String message, Throwable cause) {
    getTestContext().log(getTestName() + ": " + message, cause);
  }

  @Override
  public void tearDown() {}

}
