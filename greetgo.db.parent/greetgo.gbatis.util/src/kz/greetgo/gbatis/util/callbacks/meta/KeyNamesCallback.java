package kz.greetgo.gbatis.util.callbacks.meta;

import kz.greetgo.db.ConnectionExecutor;
import kz.greetgo.gbatis.futurecall.SqlViewer;
import kz.greetgo.gbatis.model.Result;
import kz.greetgo.gbatis.model.SqlWithParams;
import kz.greetgo.gbatis.util.OperUtil;
import kz.greetgo.gbatis.util.sqls.SqlSrc;

import java.sql.Connection;
import java.util.List;

public class KeyNamesCallback implements ConnectionExecutor<List<String>> {
  public SqlViewer sqlViewer;

  private final String tableName;

  public KeyNamesCallback(String tableName) {
    this.tableName = tableName;
  }

  public KeyNamesCallback(SqlViewer sqlViewer, String tableName) {
    this(tableName);
    this.sqlViewer = sqlViewer;
  }

  @Override
  public List<String> execute(Connection con) throws Exception {
    String sqlStr = SqlSrc.get(con).sql("meta/keyNames");
    SqlWithParams sql = SqlWithParams.select(sqlStr, tableName);
    return OperUtil.call(con, sql, Result.listOf(String.class).with(sqlViewer));
  }
}
