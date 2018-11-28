/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.ui;

import org.openqa.selenium.SearchContext;

import com.google.common.base.Function;

/**
 * ExtendedCondition
 *
 * @author ryan131
 * @since Dec 16, 2015, 7:48:31 PM
 */
public interface ExtendedCondition<T> extends Function<SearchContext, T> {}
