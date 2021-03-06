package kz.greetgo.gbatis.util.callbacks.meta;

import kz.greetgo.db.ConnectionCallback;
import kz.greetgo.gbatis.futurecall.SqlViewer;
import kz.greetgo.gbatis.model.Result;
import kz.greetgo.gbatis.model.SqlWithParams;
import kz.greetgo.gbatis.util.OperUtil;
import kz.greetgo.gbatis.util.model.ColInfo;
import kz.greetgo.gbatis.util.sqls.SqlSrc;

import java.sql.Connection;
import java.util.List;

public class ColInfoCallback implements ConnectionCallback<List<ColInfo>> {
  public SqlViewer sqlViewer;

  private final String tableName;

  public ColInfoCallback(String tableName) {
    this.tableName = tableName;
  }

  public ColInfoCallback(SqlViewer sqlViewer, String tableName) {
    this(tableName);
    this.sqlViewer = sqlViewer;
  }

  @Override
  public List<ColInfo> doInConnection(Connection con) throws Exception {
    String sqlStr = SqlSrc.get(con).sql("meta/colInfo");
    SqlWithParams sql = SqlWithParams.select(sqlStr, tableName);
    return OperUtil.call(con, sql, Result.listOf(ColInfo.class).with(sqlViewer));
  }
}
