/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.service;

/**
 * XPathBuilder
 *
 * @author ryan131
 * @since Oct 14, 2016, 3:15:25 PM
 */
public interface XPathBuilder {

  XPathBuilder ancester();

  XPathBuilder ancestor_or_self();

  XPathBuilder attribute();

  XPathBuilder child();

  XPathBuilder descendant();

  XPathBuilder descendant_or_self();

  XPathBuilder following();

  XPathBuilder following_sibling();

  XPathBuilder parent();

  XPathBuilder preceding();

  XPathBuilder preceding_sibling();

  XPathBuilder self();

}
