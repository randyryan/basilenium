/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.test;

import java.util.Set;

/**
 * A test configuration object used to pass information to a test during initialization.
 *
 * @author ryan131
 * @since Sep 19, 2015, 4:01:53 PM
 */
public interface TestConfig {

  /**
   * Returns the name of the test instance.
   */
  String getTestName();

  /**
   * Returns the description of the test instance.
   */
  String getTestDescription();

  /**
   * Returns the {@link TestContext} in which the test is executing.
   */
  TestContext getTestContext();

  /**
   * Returns the value of the requested property.
   */
  String getProperty(String key);

  /**
   * Returns a {@link Set} of property names.
   */
  Set<String> propertyKeySet();

}
