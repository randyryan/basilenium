/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.service;

import java.util.Arrays;

import org.basil.selenium.Basil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

/**
 * XPathServiceImpl
 *
 * @author ryan131
 * @since Oct 14, 2016, 3:14:55 PM
 */
public class XPathServiceImpl implements XPathService {

  private boolean isLocationPath(String expression) {
    return expression.startsWith("ancestor::")           ||
           expression.startsWith("ancestor-or-self::")   ||
           expression.startsWith("attribute::")          ||
           expression.startsWith("child::")              ||
           expression.startsWith("descendant::")         ||
           expression.startsWith("descendant-or-self::") ||
           expression.startsWith("following")            ||
           expression.startsWith("following-sibling")    ||
           expression.startsWith("namespace::")          ||
           expression.startsWith("parent::")             ||
           expression.startsWith("preceding::")          ||
           expression.startsWith("preceding-sibling")    ||
           expression.startsWith("self::");
  }

  private String abbreviate(String expression) {
    return expression.equals("attribute::") ? "@"   :
           expression.equals("descendant::") ? "//" :
           expression.equals("parent::*") ? "/.."   :
           expression.equals("self::*") ? "/."      :
           expression;
  }

  // XPath retrieval

  @Override
  public String getXPath(WebElement element) {
    String id = WebElementUtil.getId(element);
    String tag = WebElementUtil.getTag(element);
    if (Strings.isNullOrEmpty(id)) {
      int index = element.findElements(By.xpath("preceding-sibling::" + tag)).size() + 1;
      //return getXPath(element.findElement(By.xpath("parent::*"))) + "/" + tag + "[" + index + "]";
      return append(getXPath(element.findElement(By.xpath("parent::*"))), tag + "[" + index + "]");
    }
    return "//" + tag + "[@id='" + id + "']";
  }

  @Override
  public String getXPath(WebElement element, String xpathExpression) {
    return getXPath(element) + xpathExpression;
  }

  @Override
  public String getXPath(By locator) {
    return Basil.from(locator).getXPath();
  }

  @Override
  public String getXPath(By locator, String xpathExpression) {
    return getXPath(locator) + xpathExpression;
  }

  // Concatenation

  @Override
  public String append(String... xpathExpressions) {
    return append(Arrays.asList(xpathExpressions));
  }

  @Override
  public String append(Iterable<String> xpathExpressions) {
    StringBuilder sb = new StringBuilder();
    for (String xpathExpression : xpathExpressions) {
      if (isLocationPath(xpathExpression)) {
        sb.append("/" + xpathExpression);
      } else if (!xpathExpression.startsWith("/")) {
        sb.append("//" + xpathExpression);
      } else if (xpathExpression.startsWith("./") && !sb.toString().equals("")) {
        sb.append(xpathExpression.substring(xpathExpression.indexOf(".")));
      } else {
        sb.append(xpathExpression);
      }
    }
    return sb.toString();
  }

  @Override
  public String append(String xpathExpression, By... locators) {
    return xpathExpression + append(locators);
  }

  @Override
  public String append(By... locators) {
    return append(Lists.transform(Arrays.asList(locators), new Function<By, String>() {
      @Override
      public String apply(By locator) {
        return getXPath(locator);
      }
    }));
  }

  @Override
  public String append(By locator, String... xpathExpressions) {
    return getXPath(locator) + append(xpathExpressions);
  }

  @Override
  public By.ByXPath concat(String... xpathExpressions) {
    return (By.ByXPath) By.xpath(append(xpathExpressions));
  }

  @Override
  public By.ByXPath concat(String xpathExpression, By... locators) {
    return (By.ByXPath) By.xpath(append(xpathExpression, locators));
  }

  @Override
  public By.ByXPath concat(By... locators) {
    return (By.ByXPath) By.xpath(append(locators));
  }

  @Override
  public By.ByXPath concat(By locator, String... xpathExpressions) {
    return (By.ByXPath) By.xpath(append(locator, xpathExpressions));
  }

}
