/**
 * Copyright (c) 2015-2016 Ryan Li Wan. All rights reserved.
 */

package org.basil.dojo;

import org.openqa.selenium.SearchContext;

import com.google.common.base.Function;
import com.google.common.base.Strings;

/**
 * DijitCondition
 *
 * @author ryan131
 * @since Jan 16, 2019, 10:45:32 AM
 */
public interface DijitCondition<V> extends Function<SearchContext, V> {

  public static DijitCondition<Boolean> textToBeEmpty(final Dijit.TextInput<?> textInput) {
    return new DijitCondition<Boolean>() {
      @Override
      public Boolean apply(SearchContext input) {
        if (Strings.isNullOrEmpty(textInput.getValue().toString())) {
          return Boolean.TRUE;
        }
        return Boolean.FALSE;
      }
    };
  }

  public static DijitCondition<Boolean> textToBe(final Dijit.TextInput<?> textInput, final String text) {
    return new DijitCondition<Boolean>() {
      @Override
      public Boolean apply(SearchContext input) {
        if (textInput.getValue().toString().equals(text)) {
          return Boolean.TRUE;
        }
        return Boolean.FALSE;
      }
    };
  }

}
