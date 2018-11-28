/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.page;


import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.List;

import org.basil.selenium.ui.ExtendedCondition;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;

/**
 * Find
 *
 * @author ryan131
 * @since Apr 18, 2015, 1:16:36 PM
 */
public class Find {

  // Step 1: Define the input, it can be SearchContext, WebDriver, WebElement, BasilContext,
  // BasilElement, and PageObject.

  public static Find in(SearchContext context) {
    return new Find(context);
  }

  private SearchContext input;

  private Find(SearchContext input) {
    this.input = input;
  }

  // Step 2: Start with primitive By types

  public Result byId(String id) {
    return Result.from(findAll(By.id(id)), input);
  }

  public Result byLinkText(String linkText) {
    return Result.from(findAll(By.linkText(linkText)), input);
  }

  public Result byPartialLinkText(final String linkText) {
    return Result.from(findAll(By.partialLinkText(linkText)), input);
  }

  public Result byName(final String name) {
    return Result.from(findAll(By.name(name)), input);
  }

  public Result byTagName(String name) {
    return Result.from(findAll(By.tagName(name)), input);
  }

  public Result byXpath(String xpathExpression) {
    return Result.from(findAll(By.xpath(xpathExpression)), input);
  }

  public Result byClassName(String className) {
    return Result.from(findAll(By.className(className)), input);
  }

  public Result byCssSelector(String cssSelector) {
    return Result.from(findAll(By.cssSelector(cssSelector)), input);
  }

  // Step 3: Get the element or proceed with filtering

  public static class Result { // A Result from find can be further processed

    public static Result from(ExtendedCondition<List<WebElement>> search, SearchContext input) {
      List<WebElement> resultElements = search.apply(input);
      if (resultElements.isEmpty()) {
        throw new NoSuchElementException("Unable to find elements with: " + search);
      }
      return new Result(resultElements);
    }

    private List<WebElement> resultElements;

    private Result(List<WebElement> resultElements) {
      this.resultElements = resultElements;
    }

    public WebElement get() {
      return get(0);
    }

    public WebElement get(int index) {
      try {
        return resultElements.get(index);
      } catch (ArrayIndexOutOfBoundsException aioobe) {
        int n = resultElements.size();
        throw new NoSuchElementException("The search only resulted " + n + " elements.");
      }
    }

    public Result filter(FilterCondition<List<WebElement>> fc) {
      List<WebElement> resultElements = fc.apply(this.resultElements);
      if (resultElements.isEmpty()) {
        throw new NoSuchElementException("Unable to find elements with: " + fc);
      }
      return new Result(resultElements);
    }

    public Result filter(Filter filter) {
      return new Result(filter.filterElements(resultElements));
    }

  }

  private static final ExtendedCondition<List<WebElement>> findAll(final By locator) {
    return new ExtendedCondition<List<WebElement>>() {
      @Override
      public List<WebElement> apply(SearchContext input) {
        return input.findElements(locator);
      }
      @Override
      public String toString() {
        return locator.toString();
      }
    };
  }

  // FilterConditions

  private interface FilterCondition<T> extends Function<List<WebElement>, T> {}

  public static class FilterConditions {

    public static FilterCondition<List<WebElement>> withText(final String text) {
      return new FilterCondition<List<WebElement>>() {
        @Override
        public List<WebElement> apply(List<WebElement> input) {
          ImmutableList.Builder<WebElement> elements = ImmutableList.builder();
          for (WebElement element : input) {
            if (element.getText().equals(text)) {
              elements.add(element);
            }
          }
          return elements.build();
        }
        @Override
        public String toString() {
          return "Filter by text: \"" + text + "\".";
        }
      };
    }

    public static FilterCondition<List<WebElement>> withPartialText(final String partialText) {
      return new FilterCondition<List<WebElement>>() {
        @Override
        public List<WebElement> apply(List<WebElement> input) {
          ImmutableList.Builder<WebElement> elements = ImmutableList.builder();
          for (WebElement element : input) {
            if (element.getText().contains(partialText)) {
              elements.add(element);
            }
          }
          return elements.build();
        }
        @Override
        public String toString() {
          return "Filter by partial text: \"" + partialText + "\".";
        }
      };
    }

    public static FilterCondition<List<WebElement>> withAttribute(final String attribute) {
      return new FilterCondition<List<WebElement>>() {
        @Override
        public List<WebElement> apply(List<WebElement> input) {
          ImmutableList.Builder<WebElement> elements = ImmutableList.builder();
          for (WebElement element : input) {
            if (element.getAttribute(attribute) != null) {
              elements.add(element);
            }
          }
          return elements.build();
        }
        @Override
        public String toString() {
          return "Filter by attribute: \"" + attribute + "\".";
        }
      };
    }

    public static FilterCondition<List<WebElement>> withAttributeValue(
        final String attribute, final String value) {
      return new FilterCondition<List<WebElement>>() {
        @Override
        public List<WebElement> apply(List<WebElement> input) {
          ImmutableList.Builder<WebElement> elements = ImmutableList.builder();
          for (WebElement element : input) {
            if (element.getAttribute(attribute).equals(value)) {
              elements.add(element);
            }
          }
          return elements.build();
        }
        @Override
        public String toString() {
          return "Filter by attribute: \"" + attribute + "\", value: \"" + value + "\".";
        }
      };
    }

  }

  // By-like filter

  public static abstract class Filter {

    // Static concrete implementations creation methods

    public static Filter withText(final String text) {
      return new WithText(checkNotNull(text, "Cannot filter elements when the text is null."));
    }

    public static Filter withPartialText(final String text) {
      return new WithPartialText(
          checkNotNull(text, "Cannot filter element when the text is null."));
    }

    public static Filter withAttribute(final String attribute) {
      return new WithAttribute(
          checkNotNull(attribute, "Cannot filter element when the attribute is null."));
    }

    public static Filter withAttributeValue(final String attribute, final String value) {
      return new WithAttributeValue(
          checkNotNull(attribute, "Cannot filter element when the attribute is null."),
          checkNotNull(value, "Cannot filter element when the value is null."));
    }

    // filterElements and filterElement method

    public abstract List<WebElement> filterElements(List<WebElement> elements);

    public WebElement filterElement(List<WebElement> elements) {
      List<WebElement> allElements = filterElements(elements);
      if (allElements == null || allElements.isEmpty()) {
        throw new NoSuchElementException("Cannot filter out an element using " + toString());
      }
      return allElements.get(0);
    }

    // java.lang.Object methods

    @Override
    public boolean equals(Object object) {
      if (object == null) {
        return false;
      }
      if (object == this) {
        return true;
      }
      if (object.getClass().equals(getClass())) {
        Filter another = (Filter) object;
        return another.toString().equals(toString());
      }
      return false;
    }

    @Override
    public int hashCode() {
      return toString().hashCode();
    }

    @Override
    public String toString() {
      throw new UnsupportedOperationException("The toString() method must be overridden.");
    }

    public static class WithText extends Filter implements Serializable {

      private static final long serialVersionUID = 609442762077140863L;

      private final String text;

      private WithText(String text) {
        this.text = text;
      }

      @Override
      public List<WebElement> filterElements(List<WebElement> elements) {
        ImmutableList.Builder<WebElement> resultElements = ImmutableList.builder();
        for (WebElement element : elements) {
          if (element.getText().equals(text)) {
            resultElements.add(element);
          }
        }
        return resultElements.build();
      }

      @Override
      public WebElement filterElement(List<WebElement> elements) {
        for (WebElement element : elements) {
          if (element.getText().equals(text)) {
            return element;
          }
        }
        throw new NoSuchElementException("Cannot filter out elements with text: \"" + text + "\".");
      }

      @Override
      public String toString() {
        return "Filter.withText: " + text;
      }

    }

    public static class WithPartialText extends Filter implements Serializable {

      private static final long serialVersionUID = 5457937314522515099L;

      private final String text;

      private WithPartialText(String text) {
        this.text = text;
      }

      @Override
      public List<WebElement> filterElements(List<WebElement> elements) {
        ImmutableList.Builder<WebElement> resultElements = ImmutableList.builder();
        for (WebElement element : elements) {
          if (element.getText().contains(text)) {
            resultElements.add(element);
          }
        }
        return resultElements.build();
      }

      @Override
      public WebElement filterElement(List<WebElement> elements) {
        for (WebElement element : elements) {
          if (element.getText().contains(text)) {
            return element;
          }
        }
        throw new NoSuchElementException("Cannot filter out elements with text: \"" + text + "\".");
      }

      @Override
      public String toString() {
        return "Filter.withPartialText: " + text;
      }

    }

    public static class WithAttribute extends Filter implements Serializable {

      private static final long serialVersionUID = 1492595891898591094L;

      private final String attribute;

      private WithAttribute(String attribute) {
        this.attribute = attribute;
      }

      @Override
      public List<WebElement> filterElements(List<WebElement> elements) {
        ImmutableList.Builder<WebElement> resultElements = ImmutableList.builder();
        for (WebElement element : elements) {
          if (element.getAttribute(attribute) != null) {
            resultElements.add(element);
          }
        }
        return resultElements.build();
      }

      @Override
      public WebElement filterElement(List<WebElement> elements) {
        for (WebElement element : elements) {
          if (element.getAttribute(attribute) != null) {
            return element;
          }
        }
        throw new NoSuchElementException(
            "Cannot filter out elements with attribute: \"" + attribute + "\".");
      }

      @Override
      public String toString() {
        return "Filter.withAttribute: " + attribute;
      }

    }

    public static class WithAttributeValue extends Filter implements Serializable {

      private static final long serialVersionUID = -7162093578467609781L;

      private final String attribute;
      private final String value;

      private WithAttributeValue(String attribute, String value) {
        this.attribute = attribute;
        this.value = value;
      }

      @Override
      public List<WebElement> filterElements(List<WebElement> elements) {
        ImmutableList.Builder<WebElement> resultElements = ImmutableList.builder();
        for (WebElement element : elements) {
          if (element.getAttribute(attribute).equals(value)) {
            resultElements.add(element);
          }
        }
        return resultElements.build();
      }

      @Override
      public WebElement filterElement(List<WebElement> elements) {
        for (WebElement element : elements) {
          if (element.getAttribute(attribute).equals(value)) {
            return element;
          }
        }
        throw new NoSuchElementException(
            "Cannot filter out elements with attribute: \"" + attribute + "\"=" + value + "\".");
      }

      @Override
      public String toString() {
        return "Filter.withAttributeValue: " + attribute + "=" + value;
      }

    }

  }

}
