/**
 * Copyright (c) 2010-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.service;

import org.basil.selenium.BasilException;
import org.basil.selenium.BasilException.InvalidAttribute;
import org.basil.selenium.BasilException.InvalidElement;
import org.basil.selenium.BasilException.InvalidTagName;
import org.openqa.selenium.WebElement;

import com.google.common.base.Function;
import com.google.common.base.Strings;

/**
 * WebElement Validation Rule
 *
 * @author ryan131
 * @since Dec 2, 2013, 19:36:35 PM
 */
public interface ValidationRule extends Function<WebElement, BasilException.InvalidElement> {

  String getType();

  static ValidationRule isTag(final String tagName) {
    return new ValidationRule() {
      @Override
      public InvalidElement apply(WebElement input) {
        if (input.getTagName().equals(tagName)) {
          return null;
        }
        return new InvalidTagName("Tag name \"" + tagName + "\" is expected.");
      }

      @Override
      public String getType() {
        return "tag";
      }
    };
  }

  static ValidationRule hasAttribute(final String attributeName) {
    return new ValidationRule() {
      @Override
      public InvalidElement apply(WebElement input) {
        if (input.getAttribute(attributeName) != null) {
          return null;
        }
        return new InvalidAttribute("Attribute \"" + attributeName + "\" is expected.");
      }

      @Override
      public String getType() {
        return "attribute-exists";
      }
    };
  }

  static ValidationRule attributeIs(final String attributeName, final String attributeValue) {
    return new ValidationRule() {
      @Override
      public InvalidElement apply(WebElement input) {
        String value = input.getAttribute(attributeName);
        if (value == null && value == attributeValue) {
          return null;
        } else if (Strings.nullToEmpty(value).equals(attributeValue)) {
          return null;
        }
        return new InvalidAttribute(
            "Attribute \"" + attributeName + "=" + attributeValue + "\" is expected.");
      }

      @Override
      public String getType() {
        return "attribute-is";
      }
    };
  }

  static ValidationRule hasClass(final String className) {
    return new ValidationRule() {
      @Override
      public InvalidElement apply(WebElement input) {
        if (input.getAttribute("class").contains(className)) {
          return null;
        }
        return new InvalidAttribute("Class \"" + className + "\" is expected.");
      }

      @Override
      public String getType() {
        return "class-contains";
      }
    };
  }

  static ValidationRule isInput() {
    return new ValidationRule() {
      @Override
      public InvalidElement apply(WebElement input) {
        return isTag("input").apply(input);
      }

      @Override
      public String getType() {
        return "input";
      }
    };
  }

  static ValidationRule isInputType(String type) {
    return new ValidationRule() {
      @Override
      public InvalidElement apply(WebElement input) {
        InvalidElement isInput = isInput().apply(input);
        InvalidElement isType = attributeIs("type", type).apply(input);
        if (isInput != null) {
          return isInput;
        }
        return isType;
      }

      @Override
      public String getType() {
        return "input-type";
      }
    };
  }

  /**
   * A validation rule for checking if the element is <input type="button">
   */
  static ValidationRule isInputButton() {
    return new ValidationRule() {
      @Override
      public InvalidElement apply(WebElement input) {
        return isInputType("button").apply(input);
      }

      @Override
      public String getType() {
        return "input-button";
      }
    };
  }

  /**
   * A validation rule for checking if the element is <input type="checkbox">
   */
  static ValidationRule isInputCheckBox() {
    return new ValidationRule() {
      @Override
      public InvalidElement apply(WebElement input) {
        return isInputType("checkbox").apply(input);
      }

      @Override
      public String getType() {
        return "input-checkbox";
      }
    };
  }

  /**
   * A validation rule for checking if the element is <input type="file">
   */
  static ValidationRule isInputFile() {
    return new ValidationRule() {
      @Override
      public InvalidElement apply(WebElement input) {
        return isInputType("file").apply(input);
      }

      @Override
      public String getType() {
        return "input-file";
      }
    };
  }

  /**
   * A validation rule for checking if the element is <input type="number">
   */
  static ValidationRule isInputNumber() {
    return new ValidationRule() {
      @Override
      public InvalidElement apply(WebElement input) {
        return isInputType("number").apply(input);
      }

      @Override
      public String getType() {
        return "input-number";
      }
    };
  }

  /**
   * A validation rule for checking if the element is <input type="password">
   */
  static ValidationRule isInputPassword() {
    return new ValidationRule() {
      @Override
      public InvalidElement apply(WebElement input) {
        return isInputType("password").apply(input);
      }

      @Override
      public String getType() {
        return "input-password";
      }
    };
  }

  /**
   * A validation rule for checking if the element is <input type="radio">
   */
  static ValidationRule isInputRadioBox() {
    return new ValidationRule() {
      @Override
      public InvalidElement apply(WebElement input) {
        return isInputType("radio").apply(input);
      }

      @Override
      public String getType() {
        return "input-radio";
      }
    };
  }

  /**
   * A validation rule for checking if the element is <input type="reset">
   */
  static ValidationRule isInputReset() {
    return new ValidationRule() {
      @Override
      public InvalidElement apply(WebElement input) {
        return isInputType("reset").apply(input);
      }

      @Override
      public String getType() {
        return "input-reset";
      }
    };
  }

  /**
   * A validation rule for checking if the element is <input type="search">
   */
  static ValidationRule isInputSearch() {
    return new ValidationRule() {
      @Override
      public InvalidElement apply(WebElement input) {
        return isInputType("search").apply(input);
      }

      @Override
      public String getType() {
        return "input-search";
      }
    };
  }

  /**
   * A validation rule for checking if the element is <input type="submit">
   */
  static ValidationRule isInputSubmit() {
    return new ValidationRule() {
      @Override
      public InvalidElement apply(WebElement input) {
        return isInputType("submit").apply(input);
      }

      @Override
      public String getType() {
        return "input-submit";
      }
    };
  }

  static ValidationRule isInputTextBox() {
    return new ValidationRule() {
      @Override
      public InvalidElement apply(WebElement input) {
        return isInputType("text").apply(input);
      }

      @Override
      public String getType() {
        return "input-text";
      }
    };
  }

  static ValidationRule isTextarea() {
    return new ValidationRule() {
      @Override
      public InvalidElement apply(WebElement input) {
        return isTag("textarea").apply(input);
      }

      @Override
      public String getType() {
        return "textarea";
      }
    };
  }

  static ValidationRule isTextualInput() {
    return new ValidationRule() {
      @Override
      public InvalidElement apply(WebElement input) {
        if (isInputTextBox().apply(input) == null) {
          return null;
        }
        if (isInputNumber().apply(input) == null) {
          return null;
        }
        if (isInputPassword().apply(input) == null) {
          return null;
        }
        if (isTextarea().apply(input) == null) {
          return null;
        }
        return null;
      }

      @Override
      public String getType() {
        return "textual-input";
      }
    };
  }

  static ValidationRule isButton() {
    return new ValidationRule() {
      @Override
      public InvalidElement apply(WebElement input) {
        return isTag("button").apply(input);
      }

      @Override
      public String getType() {
        return "button";
      }
    };
  }

  static ValidationRule isLink() {
    return new ValidationRule() {
      @Override
      public InvalidElement apply(WebElement input) {
        return isTag("a").apply(input);
      }

      @Override
      public String getType() {
        return "link";
      }
    };
  }

}