/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.ui;

import org.openqa.selenium.WebElement;

import com.google.common.base.Function;

/**
 * Pessimistic
 *
 * @author ryan131
 * @since Jul 31, 2015, 9:46:58 AM
 */
public interface Pessimistic<T> extends Function<WebElement, T> {}
