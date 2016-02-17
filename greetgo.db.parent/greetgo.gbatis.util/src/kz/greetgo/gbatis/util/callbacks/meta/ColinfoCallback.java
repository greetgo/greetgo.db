package kz.greetgo.gbatis.util.callbacks.meta;

import kz.greetgo.db.ConnectionCallback;
import kz.greetgo.gbatis.futurecall.SqlViewer;
import kz.greetgo.gbatis.model.Result;
import kz.greetgo.gbatis.model.SqlWithParams;
import kz.greetgo.gbatis.util.OperUtil;
import kz.greetgo.gbatis.util.model.Colinfo;
import kz.greetgo.gbatis.util.sqls.SqlSrc;

import java.sql.Connection;
import java.util.List;

public class ColinfoCallback implements ConnectionCallback<List<Colinfo>> {
  public SqlViewer sqlViewer;

  private final String tableName;

  public ColinfoCallback(String tableName) {
    this.tableName = tableName;
  }

  public ColinfoCallback(SqlViewer sqlViewer, String tableName) {
    this(tableName);
    this.sqlViewer = sqlViewer;
  }

  @Override
  public List<Colinfo> doInConnection(Connection con) throws Exception {
    String sqlStr = SqlSrc.get(con).sql("meta/colinfo");
    SqlWithParams sql = SqlWithParams.select(sqlStr, tableName);
    return OperUtil.call(con, sql, Result.listOf(Colinfo.class).with(sqlViewer));
  }
}
