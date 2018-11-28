/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.ui.table;

import org.basil.selenium.ui.table.Selector.Type;

/**
 * FIXME Selectors
 *
 * @author ryan131
 * @since Aug 11, 2014, 3:40:41 PM
 */
public final class Selectors {
  private Selectors() {}

  public static IndexSelector index(final int index) {
    return new IndexSelector(index);
  }

  public static NameSelector name(final String name) {
    return new NameSelector(name);
  }

  public static StringValueSelector<Integer> valueOf(final int index) {
    return new StringValueSelector<Integer>(index, Type.INDEX);
  }

  public static StringValueSelector<String> valueOf(final String key) {
    return new StringValueSelector<String>(key, Type.STRING);
  }

  public static class KeySelector<K> extends Selector {

    private final K key;
    private final Type keyType;

    private KeySelector(K key, Type keyType) {
      super(Type.KEY);
      this.key = key;
      this.keyType = keyType;
      condition(Condition.UNSUPPORTED);
    }

    public K key() {
      return key;
    }

    public Type keyType() {
      return keyType;
    }

  }

  public static class IndexSelector extends KeySelector<Integer> {

    private IndexSelector(int index) {
      super(index, Type.INDEX);
      if (index < 0) {
        throw new IllegalArgumentException("Index cannot be less than 0.");
      }
    }

    public int index() {
      return key();
    }

  }

  public static class NameSelector extends KeySelector<String> {

    private NameSelector(String name) {
      super(name, Type.STRING);
    }

    public String name() {
      return key();
    }

  }

  public static class KeyValueSelector<K, V> extends Selector {

    private final K key;
    private final Type keyType;
    protected V value;
    private final Type valueType;

    private KeyValueSelector(K key, Type keyType, Type valueType) {
      super(Type.KEY_VALUE);
      this.key = key;
      this.keyType = keyType;
      this.valueType = valueType;
    }

    public K key() {
      return key;
    }

    public Type keyType() {
      return keyType;
    }

    void value(V value) {
      this.value = value;
    }

    public V value() {
      return value;
    }

    public Type valueType() {
      return valueType;
    }

  }

  public static class StringValueSelector<K> extends KeyValueSelector<K, String> {

    private boolean caseSensitive = true;

    private StringValueSelector(K key, Type keyType) {
      super(key, keyType, Type.STRING_VALUE);
    }

    @Override
    void value(String value) {
      this.value = value;
    }

    public StringValueSelector<K> equalz(String value) {
      condition(Condition.EQUAL);
      value(value);
      return this;
    }

    public StringValueSelector<K> notEqualz(String value) {
      condition(Condition.NOT_EQUAL);
      value(value);
      return this;
    }

    public StringValueSelector<K> contains(String value) {
      condition(Condition.CONTAINS);
      value(value);
      return this;
    }

    public StringValueSelector<K> notContains(String value) {
      condition(Condition.NOT_CONTAINS);
      value(value);
      return this;
    }

    public StringValueSelector<K> startsWith(String value) {
      condition(Condition.STARTS_WITH);
      value(value);
      return this;
    }

    public StringValueSelector<K> endsWith(String value) {
      condition(Condition.ENDS_WITH);
      value(value);
      return this;
    }

    public StringValueSelector<K> ignoreCase() {
      caseSensitive = false;
      return this;
    }

    boolean caseSensitive() {
      return caseSensitive;
    }

  }

  public static void main(String[] args) {
    Selectors.valueOf("Brand").equalz("Dell");
  }

}
