/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.ui.table;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Table;

/**
 * MultiTable
 *
 * @author ryan131
 * @since Jan 5, 2014, 3:28:36 PM
 */
public interface MultiTable<R, C, V> extends Table<R, C, V> {

//  boolean contains(@Nullable Object rowKey, @Nullable Object columnKey);
  boolean contains(int rowIndex, int columnIndex);

//  boolean containsRow(@Nullable Object rowKey);
  boolean containsRow(int rowIndex);

//  boolean containsColumn(@Nullable Object columnKey);
  boolean containsColumn(int columnIndex);

//  V get(@Nullable Object rowKey, @Nullable Object columnKey);
  V get(int rowIndex, int columnIndex);

//  @Nullable
//  V put(R rowKey, C columnKey, V value);
  V put(int rowIndex, int columnIndex, V value);

//  @Nullable
//  V remove(@Nullable Object rowKey, @Nullable Object columnKey);
  V remove(int rowIndex, int columnIndex);

//  Map<C, V> row(R rowKey);
  Map<Integer, V> row(int rowIndex);

//  Map<R, V> column(C columnKey);
  Map<Integer, V> column(int columnIndex);

//  Set<Table.Cell<R, C, V>> cellSet();
//  Set<Table.Cell<Integer, Integer, V>> cellSet();

//  Set<R> rowKeySet();
  List<Integer> rowKeyList();

//  Set<C> columnKeySet();
  List<Integer> columnKeyList();

//  Map<R, Map<C, V>> rowMap();
  List<Map<Integer, V>> rowList();

//  Map<C, Map<R, V>> columnMap();
  List<Map<Integer, V>> columnList();

  /**
   * Row key / column key / value triplet corresponding to a mapping in a table.
   *
   * @since 7.0
   */
  interface Cell<R, C, V> extends Table.Cell<R, C, V> {

//    @Nullable
//    R getRowKey();
    int getRowIndex();

//    @Nullable
//    C getColumnKey();
    int getColumnIndex();

  }

}
