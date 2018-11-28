/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.junit;

import java.util.Set;

import org.basil.test.TestConfig;
import org.basil.test.TestContext;
import org.basil.test.TestException;

import junit.framework.TestCase;

/**
 * The JUnit TestCase is basically as same as <code>AbstractTest</code>, except it is
 * extending the <code>junit.framework.TestCase</code>. The class is tests with JUnit.
 *
 * @author ryan131
 * @since Oct 17, 2015, 4:04:12 PM
 */
public abstract class JUnitTestCase extends TestCase implements JUnitTest, TestConfig {

  private transient TestConfig config;

  public JUnitTestCase() {
    super();
  }

  public JUnitTestCase(String name) {
    super(name);
  }

  /**
   * A convenience method which can be overridden so that there's no need to
   * call <code>super.setup(config)</code>.
   */
  public void setUp() throws TestException {
  }

  @Override
  public void setUp(TestConfig config) throws TestException {
//    this.setUp();
    this.config = config;
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
   * Writes the specified message to a test log file, prepended by the test's
   * name.
   */
  public void log(String message) {
    getTestContext().log(getTestName() + ": " + message);
  }

  /**
   * Writes an explanatory message and a stack trace for a given
   * <code>Throwable</code> exception to the test log file, prepended by the
   * test's name.
   */
  public void log(String message, Throwable cause) {
    getTestContext().log(getTestName() + ": " + message, cause);
  }

  @Override
  public void tearDown() {
  }

}
