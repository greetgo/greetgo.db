package kz.greetgo.gbatis.util.impl;

import kz.greetgo.gbatis.futurecall.SqlViewer;
import kz.greetgo.gbatis.util.SqlUtil;
import kz.greetgo.gbatis.util.callbacks.U;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class ModificationListApplierPostgres implements ModificationListApplier {
  private SqlViewer sqlViewer;

  public ModificationListApplierPostgres(SqlViewer sqlViewer) {
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

  private void merge(Connection connection, List<TableModification> list) throws Exception {
    TableModification tableModification = list.get(0);

    List<String> keys = new ArrayList<>();
    List<String> noKeys = new ArrayList<>();
    List<String> all = new ArrayList<>();

    all.addAll(tableModification.attributeValues.keySet());
    keys.addAll(tableModification.keys);

    noKeys.addAll(all);
    noKeys.removeAll(keys);

    Collections.sort(keys);
    Collections.sort(noKeys);
    all.clear();
    all.addAll(keys);
    all.addAll(noKeys);


    StringBuilder sql = new StringBuilder();

    sql.append("with n as (select ");
    boolean first = true;
    for (String col : all) {
      if (first) {
        first = false;
      } else {
        sql.append(", ");
      }
      sql.append("? as ").append(col);
    }
    sql.append(")\n");
    sql.append(",    u as (\n");
    if (noKeys.size() == 0) {
      sql.append("      select m.* from ").append(tableModification.tableName).append(" m, n\n");
    } else {
      sql.append("      update ").append(tableModification.tableName).append(" m\n");
      first = true;
      for (String col : noKeys) {
        if (first) {
          first = false;
          sql.append("         set ");
        } else {
          sql.append("           , ");
        }
        sql.append(col).append(" = n.").append(col).append('\n');
      }
      sql.append("      from n\n");
    }
    first = true;
    for (String key : keys) {
      if (first) {
        first = false;
        sql.append("      where ");
      } else {
        sql.append("      and   ");
      }
      sql.append("m.").append(key).append(" = n.").append(key).append('\n');
    }
    if (noKeys.size() > 0) {
      sql.append("      returning m.*\n");
    }
    sql.append(")\n");
    sql.append("insert into ").append(tableModification.tableName).append(" (");
    appendCommaSep(sql, all);
    sql.append(")\n");
    sql.append("select ");
    appendCommaSep(sql, all);
    sql.append('\n');
    sql.append("from n where not exists (select 1 from u\n");
    first = true;
    for (String key : keys) {
      if (first) {
        first = false;
        sql.append("      where ");
      } else {
        sql.append("      and   ");
      }
      sql.append("u.").append(key).append(" = n.").append(key).append('\n');
    }
    sql.append(")\n");

    long startedAt = System.currentTimeMillis();
    try {
      try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
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

  private List<Object> setParams(PreparedStatement ps,
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

  private void appendCommaSep(StringBuilder sql, List<String> cols) {
    boolean first1 = true;
    for (String col : cols) {
      if (first1) {
        first1 = false;
      } else {
        sql.append(", ");
      }
      sql.append(col);
    }
  }


}
