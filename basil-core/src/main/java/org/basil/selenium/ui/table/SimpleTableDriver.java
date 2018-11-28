/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.ui.table;

import static org.spearmint.collect.Lists.keyList;
import static org.spearmint.collect.Lists.valueList;
import static org.spearmint.collect.Maps.indexToKeyMap;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;

import org.basil.selenium.BasilElement;
import org.basil.selenium.service.XPathUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spearmint.collect.AbstractTableDriver;
import org.spearmint.collect.TableAccessException;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Simple (Web) TableDriver implementation
 *
 * @author ryan131
 * @since Jun 28, 2016, 10:12:02 AM
 */
public class SimpleTableDriver extends AbstractTableDriver<String, String, String> {

  private static final Logger logger = LoggerFactory.getLogger(SimpleTableDriver.class);

  private SimpleTableModel table;
  private SimpleConfig config;
  private SimpleSnapshot snapshot;

  // Constructors

  public SimpleTableDriver(WebElement table) {
    this(table, "");
  }

  public SimpleTableDriver(WebElement table, By.ByXPath xpathToTable) {
    this(table, XPathUtil.getXPath(xpathToTable));
  }

  public SimpleTableDriver(WebElement table, String xpathToTable) {
    String xpath = XPathUtil.getXPath(table, xpathToTable);

    this.table = new SimpleTableModel(xpath);
    this.config = new SimpleConfig(table, xpath);
    this.snapshot = new SimpleSnapshot();
  }

  // XXX Row

  public SimpleRow row(int rowIndex) {
    return table.row(rowIndex);
  }

  public SimpleRow row(String rowKey) {
    return table.row(config().rowIndex(rowKey));
  }

  public SimpleRow row(int columnIndex, String value) {
    return table.row(columnIndex, value);
  }

  public SimpleRow row(String columnKey, String value) {
    return table.row(config.columnIndex(columnKey), value);
  }

  public SimpleRow row(int columnIndex1, String value1, int columnIndex2, String value2) {
    return table.row(columnIndex1, value1, columnIndex2, value2);
  }

  public SimpleRow row(String columnKey1, String value1, String columnKey2, String value2) {
    return table.row(config.columnIndex(columnKey1), value1, config.columnIndex(columnKey2), value2);
  }

  public List<SimpleRow> rows(int... rowIndexes) {
    return null;
  }

  public List<SimpleRow> rows(String rowKeys) {
    return null;
  }

  // XXX Column

  public SimpleColumn column(int columnIndex) {
    return table.column(columnIndex);
  }

  public SimpleColumn column(String columnKey) {
    return table.column(config().columnIndex(columnKey));
  }

  public SimpleColumn column(int rowIndex, String value) {
    return table.column(table.row(rowIndex).values().indexOf(value));
  }

  public SimpleColumn column(String rowKey, String value) {
    return table.column(table.row(config().rowIndex(rowKey)).values().indexOf(value));
  }

  public ImmutableList<SimpleColumn> columns(int... columnIndexes) {
    return null;
  }

  public ImmutableList<SimpleColumn> columns(String... columnKeys) {
    return null;
  }

  // XXX Need update

  @Override
  public SimpleConfig config() {
    return config;
  }

  private Snapshot.Mode snapshotMode() {
    return config().snapshotMode();
  }

  @Override
  public Snapshot snapshot() {
    return snapshot;
  }

  @Override
  public SimpleTableModel model() {
    return table;
  }

  // XXX TableModel

  public class SimpleTableModel extends AbstractTableModel {

    private String rootXPath;

    private SimpleTableModel(String xpath) {
      this.rootXPath = xpath;
    }

    @Override
    public SimpleRow row(int rowIndex) {
      return new SimpleRow(this, rowIndex);
    }

    @Override
    public SimpleRow row(int columnIndex, String value) {
      return new SimpleRow(this, columnIndex, value);
    }

    @Override
    public SimpleRow row(int columnIndex1, String value1, int columnIndex2, String value2) {
      return new SimpleRow(this, columnIndex1, value1, columnIndex2, value2);
    }

    public SimpleRow row(String xpath) {
      return new SimpleRow(this, xpath);
    }

    @Override
    public int rowCount() {
      return config.rowCount();
    }

    @Override
    public SimpleColumn column(int columnIndex) {
      return new SimpleColumn(this, columnIndex);
    }

    @Override
    public int columnCount() {
      return config.columnCount();
    }

    public String xpath() {
      return rootXPath;
    }

    @Noninternal
    public WebElement element() {
      return config().searchContext().findElement(By.xpath(rootXPath));
    }

    @Override
    public int rowIndex(String rowKey) {
      return config().rowIndex(rowKey);
    }

    @Override
    public String rowKey(int rowIndex) {
      return config().rowKey(rowIndex);
    }

    @Override
    public int columnIndex(String columnKey) {
      return config().columnIndex(columnKey);
    }

    @Override
    public String columnKey(int columnIndex) {
      return config().columnKey(columnIndex);
    }

  }

  public class SimpleRow extends AbstractRow {

    private String rootXPath;
    private int rowIndex;    // By specifying row index

    private SimpleRow(SimpleTableModel table, int rowIndex) {
      this.rowIndex = rowIndex;
      this.rootXPath = String.format("%s/tr[%d]", table.xpath(), rowIndex);
    }

    private SimpleRow(SimpleTableModel table, int columnIndex, String value) {
      this.rowIndex = -1;
      this.rootXPath = String.format("%s/tr[td[%d][.='%s']]", table.xpath(), columnIndex, value);
    }

    private SimpleRow(SimpleTableModel table, int columnIndex1, String value1, int columnIndex2, String value2) {
      this.rowIndex = -1;
      this.rootXPath = String.format("%s/tr[td[%d][.='%s'] and td[%d][.='%s']]", table.xpath(), columnIndex1, value1, columnIndex2, value2);
    }

    public SimpleRow(SimpleTableModel table, String xpath) {
      this.rowIndex = -1;
      this.rootXPath = String.format("%s%s", table.xpath(), xpath);
    }

    @Override
    public SimpleCell cell(int columnIndex) {
      return new SimpleCell(this, columnIndex);
    }

    @Override
    public int index() {
      return rowIndex;
    }

    @Override
    public String key() {
      return config().rowKey(rowIndex);
    }

    public String xpath() {
      return rootXPath;
    }

    @Noninternal
    public BasilElement element() {
      return BasilElement.create(config().searchContext(), By.xpath(xpath()));
    }

    @Noninternal
    public List<String> values() {
      List<String> values = Lists.newArrayList();
      for (Cell<String> cell : cells()) {
        values.add(cell.value());
      }
      return values;
    }

    @Override
    public List<Cell<String>> cells(String... columnKeys) {
      return null;
    }

  }

  public class SimpleColumn extends AbstractColumn {
 
    String xpath;
    int columnIndex;

    SimpleColumn(SimpleTableModel table, int columnIndex) {
      this.columnIndex = columnIndex;
      this.xpath = table.xpath() + "/tr[%d]/td[" + columnIndex + "]";
    }

    @Override
    public SimpleCell cell(int rowIndex) {
      return new SimpleCell(this, rowIndex);
    }

    @Override
    public int index() {
      return columnIndex;
    }

    @Override
    public String key() {
      return config().columnKey(columnIndex);
    }

    public String xpath() {
      return xpath;
    }

    @Noninternal
    public WebElement element() {
      return config().searchContext().findElement(By.xpath(xpath));
    }

    @Noninternal
    public List<String> values() {
      List<String> values = Lists.newArrayList();
      for (Cell<String> cell : cells()) {
        values.add(cell.value());
      }
      return values;
    }

    @Override
    public List<Cell<String>> cells(String... rowKeys) {
      return null;
    }

  }

  /**
   * The simplest yet most important module in the application, SimpleCell uses XPath passed from
   * Row/Column to locate the web elements then registering them in the table snapshot for status
   * keeping.
   */
  public class SimpleCell extends AbstractCell {

    private String xpath;

    private SimpleCell(SimpleRow row, int columnIndex) {
      super(row, columnIndex);
      xpath = String.format("%s/td[%d]", row.xpath(), columnIndex);
    }

    private SimpleCell(SimpleColumn column, int rowIndex) {
      super(column, rowIndex);
      xpath = String.format(column.xpath(), rowIndex);
    }

    @Noninternal
    public BasilElement element() {
      return BasilElement.create(config().searchContext(), By.xpath(xpath));
    }

    @Override
    public String value() {
      if (snapshotMode().equals(Snapshot.Mode.DISABLE)) {
        return element().getText();
      }

      String value = snapshot().get(rowIndex, columnIndex);
      if (value == null) {
        value = element().getText();

        String rowKey = config().rowKey(rowIndex);
        String columnKey = config().columnKey(columnIndex);
        if (config().cachedRowKeys().contains(rowKey) && config().cachedColumnKeys().contains(columnKey)) {
          snapshot.set(rowIndex, columnIndex, value);
        }
      }

      return value;
    }

    @Override
    public String rowKey() {
      return config().rowKey(rowIndex);
    }

    @Override
    public SimpleRow row() {
      return null;
    }

    @Override
    public String columnKey() {
      return config().columnKey(columnIndex);
    }

    @Override
    public SimpleColumn column() {
      return null;
    }

    @Override
    public String xpath() {
      return xpath;
    }

  }

  // Simple implementations of Config and Snapshot.

  class BaseConfig extends AbstractConfig {

    private Map<String, Integer> rowKeyToIndex;
    private Map<String, Integer> columnKeyToIndex;

    BaseConfig() {
      rowKeyToIndex = Maps.newHashMap();
      columnKeyToIndex = Maps.newHashMap();
    }

    @Override
    public String rowKey(int rowIndex) {
      for (Map.Entry<String, Integer> rowKey : rowKeyToIndex.entrySet()) {
        if (rowKey.getValue().equals(rowIndex)) {
          return rowKey.getKey();
        }
      }
      throw new TableAccessException.NoSuchIndex();
    }

    @Override
    public String columnKey(int columnIndex) {
      for (Map.Entry<String, Integer> columnKey : columnKeyToIndex.entrySet()) {
        if (columnKey.getValue().equals(columnIndex)) {
          return columnKey.getKey();
        }
      }
      throw new TableAccessException.NoSuchIndex();
    }

    @Override
    public int rowIndex(String rowKey) {
      for (Map.Entry<String, Integer> rowIndex : rowKeyToIndex.entrySet()) {
        if (rowIndex.getKey().equals(rowKey)) {
          return rowIndex.getValue();
        }
      }
      throw new TableAccessException.NoSuchKey(rowKey.toString());
    }

    @Override
    public int columnIndex(String columnKey) {
      for (Map.Entry<String, Integer> columnIndex : columnKeyToIndex.entrySet()) {
        if (columnIndex.getKey().equals(columnKey)) {
          return columnIndex.getValue();
        }
      }
      throw new TableAccessException.NoSuchKey(columnKey);
    }

    // Management

    @Override
    protected void setRowKey(String rowKey, int rowIndex) {
      rowKeyToIndex.put(rowKey, rowIndex);
    }

    @Override
    public ImmutableList<String> rowKeys() {
      return ImmutableList.copyOf(keyList(rowKeyToIndex));
    }

    @Override
    protected void setColumnKey(String columnKey, int columnIndex) {
      columnKeyToIndex.put(columnKey, columnIndex);
    }

    @Override
    public ImmutableList<String> columnKeys() {
      return ImmutableList.copyOf(keyList(columnKeyToIndex));
    }

  }

  public class SimpleConfig extends BaseConfig {

    private SearchContext searchContext; // A superior search context other than the table itself,
    // it is recommended to set this to the WebDriver instance to alleviate selenium server loads.

    public SimpleConfig(WebElement table, String xpath) {
      setSearchContext(table);
      // Row and column count
      setRowCount(searchContext.findElements(By.xpath(xpath + "/tr")).size());
      if (rowCount() > 0) {
        setColumnCount(
            columnCount(searchContext.findElements(By.xpath(xpath + "/tr[1]/td"))));
        String message =
            String.format("Table (%d x %d) detected at location %s.", rowCount(), columnCount(), xpath);
        logger.info(message);
      } else {
        logger.warn("Unable to detect column size because there are no rows in the table.");
      }
    }

    private int columnCount(List<WebElement> rowCells) {
      int columnCount = 0;
      for (WebElement rowCell : rowCells) {
        String colspan = rowCell.getAttribute("colspan");
        if (colspan != null) {
          columnCount += Integer.parseInt(colspan);
        } else {
          columnCount += 1;
        }
      }
      return columnCount;
    }

    // SearchContext methods are not declared in the Config interface

    public SearchContext searchContext() {
      return searchContext;
    }

    public void setSearchContext(SearchContext searchContext) {
      this.searchContext = searchContext;
    }

  }

  /**
   * A Simple Snapshot implementation.
   *
   * @author ryan131
   * @since Nov 18, 2015, 7:41:37 PM
   */
  public class SimpleSnapshot extends AbstractSnapshot {

    private com.google.common.collect.Table<Integer, Integer, String> table;

    // Constructor

    SimpleSnapshot() {
      invalidate();
    }

    // Cell

    @Override
    public String get(int rowIndex, int columnIndex) {
      return table.get(rowIndex, columnIndex);
    }

    @Override
    public void set(int rowIndex, int columnIndex, String value) {
      table.put(rowIndex, columnIndex, value);
    }

    // Row

    @Override
    public ImmutableList<String> getRow(int rowIndex) {
      return ImmutableList.copyOf(valueList(table.row(rowIndex)));
    }

    @Override
    public void setRow(int rowIndex, List<String> row) {
      Map<Integer, String> indexMap = indexToKeyMap(row);
      BasilTable.create(table).putRow(rowIndex, indexMap);
    }

    // Column

    @Override
    public ImmutableList<String> getColumn(int columnIndex) {
      return ImmutableList.copyOf(valueList(table.column(columnIndex)));
    }

    @Override
    public void setColumn(int columnIndex, List<String> column) {
      Map<Integer, String> indexToKeyMap = indexToKeyMap(column);
      BasilTable.create(table).putColumn(columnIndex, indexToKeyMap);
    }

    // invalidate and equals

    @Override
    public void invalidate() {
      table = HashBasedTable.create(config().rowCount(), config().columnCount());
    }

    @Override
    public SimpleSnapshot copy() {
      SimpleSnapshot shallowCopy = new SimpleSnapshot();
      shallowCopy.table = HashBasedTable.create(table);
      // String is immutable so it's safe to use shallow copy.
      return shallowCopy;
    }

    @Override
    public boolean equals(Object object) {
      if (this == object) {
        return true;
      }
      if (object instanceof SimpleSnapshot) {
        return table.equals(((SimpleSnapshot) object).table);
      }
      return false;
    }

  }

  @Documented
  @Target({ElementType.METHOD, ElementType.TYPE})
  @Retention(RetentionPolicy.CLASS)
  public @interface Noninternal {}

}
