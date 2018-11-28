/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.ui.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Selector
 *
 * @author ryan131
 * @since Aug 2, 2014, 11:22:48 AM
 */
public abstract class Selector {

  private final Type type;
  private Condition condition;

  private OuterCondition outerCondition;
  private Selector joinedSelector;

  Selector(Type type) {
    this.type = type;
    outerCondition = OuterCondition.NOT_NOT;
  }

  // Type and condition

  Type type() {
    return type;
  }

  void condition(Condition condition) {
    if (type.is(Type.KEY) && !condition.equals(Condition.UNSUPPORTED)) {
      throw new UnsupportedOperationException("Conditioning is not allowed on keyed selectors.");
    }
    this.condition = condition;
  }

  Condition condition() {
    return condition;
  }

  // Outer condition and joined selector

  OuterCondition outerCondition() {
    return outerCondition;
  }

  Selector joinedSelector() {
    return joinedSelector;
  }

  /**
   * Joins another condition like: condition1 && condition2
   */
  public void and(Selector selector) {
    outerCondition = OuterCondition.AND;
    joinedSelector = selector;
  }

  /**
   * Joins another condition like: condition1 && !condition2
   */
  public void andNot(Selector selector) {
    outerCondition = OuterCondition.AND_NOT;
    joinedSelector = selector.not();
  }

  /**
   * Joins another condition like: condition1 || condition2
   */
  public void or(Selector selector) {
    outerCondition = OuterCondition.OR;
    joinedSelector = selector;
  }

  /**
   * Joins another condition like: condition1 || !condition2
   */
  public void orNot(Selector selector) {
    outerCondition = OuterCondition.OR_NOT;
    joinedSelector = selector.not();
  }

  /**
   * Use the condition like: !condition, !!condition, or !!!condition, and so on
   */
  public Selector not() {
    if (outerCondition.equals(OuterCondition.NOT)) {
      outerCondition = OuterCondition.NOT_NOT;
    }
    if (outerCondition.equals(OuterCondition.NOT_NOT)) {
      outerCondition = OuterCondition.NOT;
    }
    return this;
  }

  // Schema

  /**
   * Types of Selector, written as a class to use inheritance. Structure:
   *
   *     Type
   *       │
   *       ├ Key
   *       │   │
   *       │   ├ Integer
   *       │   │   │
   *       │   │   └ Index
   *       │   │
   *       │   └ String
   *       │       │
   *       │       └ Name
   *       │
   *       └ KeyValue
   *
   * @author ryan131
   * @since Aug 2, 2014, 5:14:25 PM
   */
  static class Type {

    // Declarations

    public static final Key KEY = new Key();

    public static final Integer INTEGER = new Integer();
    public static final Index INDEX = new Index();

    public static final String STRING = new String();
    public static final Name NAME = new Name();

    public static final KeyValue KEY_VALUE = new KeyValue();

    public static final StringValue STRING_VALUE = new StringValue();

    // Definitions

    public static class Key extends Type {
      private Key() {
      }
    }

    public static class Integer extends Key {
      private Integer() {
      }
    }

    public static class Index extends Integer {
      private Index() {
      }
    }

    public static class String extends Key {
      private String() {
      }
    }

    public static class Name extends String {
      private Name() {
      }
    }

    public static class KeyValue extends Type {
      private KeyValue() {
      }
    }

    public static class StringValue extends KeyValue {
      private StringValue() {
      }
    }

    // Is

    boolean is(Type type) {
      List<Class<?>> thisClassTree = new ArrayList<>();
      Class<?> thisClass = this.getClass();
      while (!thisClass.equals(Type.class)) {
        thisClassTree.add(thisClass);
        thisClass = thisClass.getSuperclass();
      }
      Collections.reverse(thisClassTree);

      List<Class<?>> typeClassTree = new ArrayList<>();
      Class<?> typeClass = type.getClass();
      while (!typeClass.equals(Type.class)) {
        typeClassTree.add(typeClass);
        typeClass = typeClass.getSuperclass();
      }
      Collections.reverse(typeClassTree);

      
      int iteration = Math.max(thisClassTree.size(), typeClassTree.size());
      for (int index = 0; index < iteration; index++) {
        try {
          if (!thisClassTree.get(index).equals(typeClassTree.get(index))) {
            // This: Key.String
            // That: Key.Integer
            return false;
          }
        } catch (IndexOutOfBoundsException ioobe) {
          // This: Key.String.Name
          // That: Key.String
          return true;
        }
      }

      return true;
    }

  }

  static enum Condition {

    UNSUPPORTED,

    EQUAL,
    NOT_EQUAL,

    LESS_THAN,
    LESS_OR_EQUAL,
    GREATER_THAN,
    GREATER_OR_EQUAL,

    CONTAINS,
    NOT_CONTAINS,
    STARTS_WITH,
    ENDS_WITH;

  }

  static enum OuterCondition {

    AND,
    AND_NOT,
    OR,
    OR_NOT,
    NOT,
    NOT_NOT;

  }

}
