/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.ui.table;

import java.util.Map;

import org.spearmint.collect.Maps;

import com.google.common.base.Preconditions;
import com.google.common.collect.ForwardingTable;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * BasilTable
 *
 * @author ryan131
 * @since Jan 5, 2014, 11:09:29 AM
 */
public class BasilTable<R, C, V> extends ForwardingTable<R, C, V> {

  public static <R, C, V> BasilTable<R, C, V> create() {
    Table<R, C, V> table = HashBasedTable.create();
    return new BasilTable<R, C, V>(table);
  }

  public static <R, C, V> BasilTable<R, C, V> create(Table<R, C, V> table) {
    return (table instanceof BasilTable<?, ?, ?>)
        ? new BasilTable<R, C, V>((BasilTable<R, C, V>) table)
        : new BasilTable<R, C, V>(table);
  }

  private Table<R, C, V> delegate;

  protected BasilTable() {
  }

  protected BasilTable(Table<R, C, V> table) {
    delegate(table);
  }

  protected BasilTable(BasilTable<R, C, V> table) {
    delegate(table.delegate);
  }

  protected void delegate(Table<R, C, V> delegate) {
    if (this.delegate != null) {
      throw new IllegalStateException();
    }
    this.delegate = Preconditions.checkNotNull(delegate);
  }

  @Override
  protected Table<R, C, V> delegate() {
    return (Table<R, C, V>) Preconditions.checkNotNull(delegate);
  }

  public Map<C, V> putRow(R rowKey, Map<C, V> row) {
    return Maps.putAll(delegate().row(rowKey), row);
  }

  public Map<C, V> removeRow(R rowKey) {
    return Maps.clear(delegate().row(rowKey));
  }

  public Map<R, V> putColumn(C columnKey, Map<R, V> column) {
    return Maps.putAll(delegate().column(columnKey), column);
  }

  public Map<R, V> removeColumn(C columnKey) {
    return Maps.clear(delegate().column(columnKey));
  }

}
