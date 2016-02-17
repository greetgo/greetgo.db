package kz.greetgo.gbatis.util.callbacks;

import kz.greetgo.db.ConnectionCallback;
import kz.greetgo.gbatis.futurecall.SqlViewer;
import kz.greetgo.gbatis.model.Result;
import kz.greetgo.gbatis.model.SqlWithParams;
import kz.greetgo.gbatis.util.OperUtil;

import java.sql.Connection;

public final class CountWhereCallback implements ConnectionCallback<Integer> {
  public SqlViewer sqlViewer;

  private final String tableName;
  private final String where;
  private final Object[] values;

  public CountWhereCallback(String tableName, String where, Object... values) {
    this.tableName = tableName;
    this.where = where;
    this.values = values;
  }

  public CountWhereCallback(SqlViewer sqlViewer, String tableName, String where, Object... values) {
    this.sqlViewer = sqlViewer;
    this.tableName = tableName;
    this.where = where;
    this.values = values;
  }

  @Override
  public Integer doInConnection(Connection con) throws Exception {
    StringBuilder sql = new StringBuilder();
    sql.append("select count(1) from ").append(tableName);
    U.appendWhere(sql, where);

    SqlWithParams sqlp = SqlWithParams.select(sql.toString(), values);
    return OperUtil.call(con, sqlp, Result.simple(Integer.class));
  }
}