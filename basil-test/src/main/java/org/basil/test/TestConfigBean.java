/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.test;

import java.util.Map;
import java.util.Set;

/**
 * TestConfig (Implementation)
 *
 * @author ryan131
 * @since Apr 28, 2016, 4:46:16 PM
 */
public class TestConfigBean implements TestConfig {

  private String testName;

  private String testDescription;

  private TestContext testContext;

  private Map<String, String> property;

  @Override
  public String getTestName() {
    return testName;
  }

  void setTestName(String testName) {
    this.testName = testName;
  }

  @Override
  public String getTestDescription() {
    return testDescription;
  }

  void setTestDescription(String testDescription) {
    this.testDescription = testDescription;
  }

  @Override
  public TestContext getTestContext() {
    return testContext;
  }

  void setTestContext(TestContext testContext) {
    this.testContext = testContext;
  }

  @Override
  public String getProperty(String key) {
    return property.get(key);
  }

  void setPropertyMap(Map<String, String> property) {
    this.property = property;
  }

  @Override
  public Set<String> propertyKeySet() {
    return property.keySet();
  }

}
