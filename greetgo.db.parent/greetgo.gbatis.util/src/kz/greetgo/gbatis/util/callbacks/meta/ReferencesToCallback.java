package kz.greetgo.gbatis.util.callbacks.meta;

import kz.greetgo.db.ConnectionExecutor;
import kz.greetgo.gbatis.futurecall.SqlViewer;
import kz.greetgo.gbatis.model.Result;
import kz.greetgo.gbatis.model.SqlWithParams;
import kz.greetgo.gbatis.util.OperUtil;
import kz.greetgo.gbatis.util.model.ForeignKey;
import kz.greetgo.gbatis.util.sqls.SqlSrc;

import java.sql.Connection;
import java.util.Set;

public class ReferencesToCallback implements ConnectionExecutor<Set<ForeignKey>> {
  public SqlViewer sqlViewer;

  private final String tableName;

  public ReferencesToCallback(String tableName) {
    this.tableName = tableName;
  }

  public ReferencesToCallback(SqlViewer sqlViewer, String tableName) {
    this(tableName);
    this.sqlViewer = sqlViewer;
  }

  @Override
  public Set<ForeignKey> execute(Connection con) throws Exception {
    String sqlStr = SqlSrc.get(con).sql("meta/referencesTo");
    SqlWithParams sql = SqlWithParams.select(sqlStr, tableName);
    return OperUtil.call(con, sql, Result.setOf(ForeignKey.class).with(sqlViewer));
  }
}
