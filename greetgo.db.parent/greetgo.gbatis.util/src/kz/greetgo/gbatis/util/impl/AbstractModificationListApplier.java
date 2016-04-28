package kz.greetgo.gbatis.util.impl;

import kz.greetgo.gbatis.futurecall.SqlViewer;
import kz.greetgo.gbatis.util.SqlUtil;
import kz.greetgo.gbatis.util.callbacks.U;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public abstract class AbstractModificationListApplier implements ModificationListApplier {
  protected SqlViewer sqlViewer;

  public AbstractModificationListApplier(SqlViewer sqlViewer) {
    this.sqlViewer = sqlViewer;
  }

  @Override
  public void apply(Connection connection, List<TableModification> tableModificationList) throws Exception {

    Map<String, List<TableModification>> groupedByKeyUniqueness = new HashMap<>();

    for (TableModification tableModification : tableModificationList) {
      String keyUniqueness = tableModification.keyUniqueness();
      List<TableModification> localList = groupedByKeyUniqueness.get(keyUniqueness);
      if (localList == null) {
        localList = new ArrayList<>();
        groupedByKeyUniqueness.put(keyUniqueness, localList);
      }
      localList.add(tableModification);
    }

    for (List<TableModification> list : groupedByKeyUniqueness.values()) {
      merge(connection, list);
    }
  }

  protected final List<String> keys = new ArrayList<>();
  protected final List<String> noKeys = new ArrayList<>();
  protected final List<String> all = new ArrayList<>();
  protected String tableName;

  private void merge(Connection connection, List<TableModification> list) throws Exception {
    TableModification tableModification = list.get(0);
    tableName = tableModification.tableName;

    keys.clear();
    noKeys.clear();
    all.clear();

    all.addAll(tableModification.attributeValues.keySet());
    keys.addAll(tableModification.keys);

    noKeys.addAll(all);
    noKeys.removeAll(keys);

    Collections.sort(keys);
    Collections.sort(noKeys);
    all.clear();
    all.addAll(keys);
    all.addAll(noKeys);

    String sql = createSql();

    long startedAt = System.currentTimeMillis();
    try {

      try (PreparedStatement ps = connection.prepareStatement(sql)) {
        if (list.size() == 1) {
          List<Object> params = setParams(ps, all, tableModification);
          ps.executeUpdate();
          if (U.need(sqlViewer)) U.view(startedAt, sqlViewer, null, sql, params);
        } else {
          List<Object> params = new ArrayList<>();
          for (TableModification modification : list) {
            params.addAll(setParams(ps, all, modification));
            ps.addBatch();
          }
          ps.executeBatch();
          if (U.need(sqlViewer)) U.view(startedAt, sqlViewer, null, sql, params);
        }
      }
    } catch (Exception e) {
      if (e instanceof BatchUpdateException) {
        e = ((BatchUpdateException) e).getNextException();
      }
      if (U.need(sqlViewer)) U.view(startedAt, sqlViewer, e, sql, null);
      throw e;
    }
  }

  protected abstract String createSql();

  protected List<Object> setParams(PreparedStatement ps,
                                   List<String> cols,
                                   TableModification tableModification) throws SQLException {
    List<Object> params = new ArrayList<>();
    int index = 0;
    for (String col : cols) {
      Object param = SqlUtil.forSql(tableModification.attributeValues.get(col));
      params.add(param);
      ps.setObject(++index, param);
    }
    return params;
  }

  protected static void appendCommaSeparated(StringBuilder sql, String prefix, List<String> cols) {
    boolean first1 = true;
    for (String col : cols) {
      if (first1) {
        first1 = false;
      } else {
        sql.append(", ");
      }
      sql.append(prefix).append(col);
    }
  }

}
