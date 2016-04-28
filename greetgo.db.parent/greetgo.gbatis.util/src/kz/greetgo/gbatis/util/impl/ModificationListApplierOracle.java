package kz.greetgo.gbatis.util.impl;

import kz.greetgo.gbatis.futurecall.SqlViewer;

public class ModificationListApplierOracle extends AbstractModificationListApplier {
  public ModificationListApplierOracle(SqlViewer sqlViewer) {
    super(sqlViewer);
  }

  @Override
  protected String createSql() {
    StringBuilder sql = new StringBuilder();

    sql.append("merge /*+ parallel(des, 4) */ into ").append(tableName).append(" des using (\n");
    boolean first = true;
    for (String col : all) {
      if (first) {
        first = false;
        sql.append("  select ");
      } else {
        sql.append(", ");
      }
      sql.append("? as ").append(col);
    }
    sql.append(" from dual\n");
    sql.append(") src ON (");

    first = true;
    for (String key : keys) {
      if (first) {
        first = false;
      } else {
        sql.append(" and ");
      }
      sql.append("des.").append(key).append(" = src.").append(key);
    }
    sql.append(")\n");

    if (noKeys.size() > 0) {
      sql.append("when matched then update\n");
      first = true;
      for (String col : noKeys) {
        if (first) {
          first = false;
          sql.append("  set ");
        } else {
          sql.append("  ,   ");
        }
        sql.append("des.").append(col).append(" = src.").append(col).append('\n');
      }
    }
    sql.append("when not matched then insert (\n  ");
    appendCommaSeparated(sql, "des.", all);
    sql.append("\n) values (\n  ");
    appendCommaSeparated(sql, "src.", all);
    sql.append("\n)");
    
    return sql.toString();
  }

}
