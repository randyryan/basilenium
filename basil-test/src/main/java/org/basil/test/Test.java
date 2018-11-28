/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.test;

/**
 * Defines the basic lifecycle of a test.
 * 
 * @author ryan131
 * @since Sep 19, 2015, 3:13:39 PM
 */
public interface Test {

  /**
   * Called by the test runner to set up the test prior to execution.
   */
  void setUp(TestConfig config) throws TestException;

  /**
   * Returns the {@link TestConfig} which contains properties for this test.
   */
  TestConfig getTestConfig();

  /**
   * Called by the test runner to allow the test to clean up after execution.
   */
  void tearDown();

}