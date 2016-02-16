package kz.greetgo.gbatis.util.callbacks;

import kz.greetgo.db.ConnectionExecutor;
import kz.greetgo.gbatis.futurecall.SqlViewer;
import kz.greetgo.gbatis.model.Result;
import kz.greetgo.gbatis.model.SqlWithParams;
import kz.greetgo.gbatis.util.OperUtil;

import java.sql.Connection;

public final class DeleteWhereCallback implements ConnectionExecutor<Integer> {

  public SqlViewer sqlViewer;
  private final String tableName;
  private final String where;
  private final Object[] values;

  public DeleteWhereCallback(String tableName, String where, Object... values) {
    this.tableName = tableName;
    this.where = where;
    this.values = values;
  }

  public DeleteWhereCallback(SqlViewer sqlViewer, String tableName, String where, Object... values) {
    this.sqlViewer = sqlViewer;
    this.tableName = tableName;
    this.where = where;
    this.values = values;
  }

  @Override
  public Integer execute(Connection con) throws Exception {
    StringBuilder sql = new StringBuilder();
    sql.append("delete from ").append(tableName);
    U.appendWhere(sql, where);

    SqlWithParams sqlp = SqlWithParams.modi(sql.toString(), values);
    return OperUtil.call(con, sqlp, Result.simple(Integer.class).with(sqlViewer));
  }
}
