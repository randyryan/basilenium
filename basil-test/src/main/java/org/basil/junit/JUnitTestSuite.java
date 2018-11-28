/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.junit;

import org.basil.test.TestConfig;
import org.basil.test.TestException;

import junit.framework.TestSuite;

/**
 * JUnit TestSuite
 *
 * @author ryan131
 * @since Oct 2, 2015, 2:11:32 PM
 */
public abstract class JUnitTestSuite extends TestSuite implements JUnitTest {

  public JUnitTestSuite() {
    super();
  }

  public JUnitTestSuite(String name) {
    super(name);
  }

  public JUnitTestSuite(Class<?> clazz) {
    super(clazz);
  }

  @Override
  public void setUp(TestConfig config) throws TestException {
  }

  @Override
  public TestConfig getTestConfig() {
    return null;
  }

  @Override
  public void tearDown() {
  }

}
