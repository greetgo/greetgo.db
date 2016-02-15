package kz.greetgo.gbatis.util.callbacks.meta;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import kz.greetgo.gbatis.futurecall.SqlViewer;
import kz.greetgo.gbatis.model.Result;
import kz.greetgo.gbatis.model.SqlWithParams;
import kz.greetgo.gbatis.util.OperUtil;
import kz.greetgo.gbatis.util.sqls.SqlSrc;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;

public class KeyNamesCallback implements ConnectionCallback<List<String>> {
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
  public List<String> doInConnection(Connection con) throws SQLException, DataAccessException {
    String sqlStr = SqlSrc.get(con).sql("meta/keyNames");
    SqlWithParams sql = SqlWithParams.select(sqlStr, tableName);
    return OperUtil.call(con, sql, Result.listOf(String.class).with(sqlViewer));
  }
}
