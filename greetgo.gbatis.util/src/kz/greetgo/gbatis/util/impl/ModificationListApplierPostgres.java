package kz.greetgo.gbatis.util.impl;

import kz.greetgo.gbatis.futurecall.SqlViewer;

public class ModificationListApplierPostgres extends AbstractModificationListApplier {
  public ModificationListApplierPostgres(SqlViewer sqlViewer) {
    super(sqlViewer);
  }

  @Override
  protected String createSql() {
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
      sql.append("      select m.* from ").append(tableName).append(" m, n\n");
    } else {
      sql.append("      update ").append(tableName).append(" m\n");
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
    sql.append("insert into ").append(tableName).append(" (");
    appendCommaSeparated(sql, "", all);
    sql.append(")\n");
    sql.append("select ");
    appendCommaSeparated(sql, "", all);
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
    return sql.toString();
  }

}
