/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.test;

import org.basil.junit.JUnitTestSuite;

import junit.framework.Test;

/**
 * JUnitTestSuite Suite
 *
 * @author ryan131
 * @since Apr 28, 2016, 3:44:59 PM
 */
public class JUnitTestSuiteTest extends JUnitTestSuite {

  public JUnitTestSuiteTest(Class<?> clazz) {
    super(clazz);
  }

  public static Test suite() {
    return new JUnitTestSuiteTest(JUnitTestCaseTest.class);
  }

}

