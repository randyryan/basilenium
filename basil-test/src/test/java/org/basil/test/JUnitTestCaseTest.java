/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.test;

import org.basil.junit.JUnitTestCase;
import org.basil.test.TestConfigFactory;
import org.junit.Before;

/**
 * JUnitTestCase Test
 *
 * @author ryan131
 * @since Apr 28, 2016, 10:58:26 AM
 */
public class JUnitTestCaseTest extends JUnitTestCase {

  @Before
  public void setUp() {
    super.setUp(TestConfigFactory.getTestContig(getClass()));
  }

  public void testJUnitTestCase() {
    System.out.println(getClass().getSimpleName());
  }

}
