package kz.greetgo.gbatis.util.callbacks;

import java.sql.Connection;
import java.sql.SQLException;

import kz.greetgo.gbatis.futurecall.SqlViewer;
import kz.greetgo.gbatis.model.Result;
import kz.greetgo.gbatis.model.SqlWithParams;
import kz.greetgo.gbatis.util.OperUtil;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;

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
  public Integer doInConnection(Connection con) throws SQLException, DataAccessException {
    StringBuilder sql = new StringBuilder();
    sql.append("select count(1) from ").append(tableName);
    U.appendWhere(sql, where);
    
    SqlWithParams sqlp = SqlWithParams.select(sql.toString(), values);
    return OperUtil.call(con, sqlp, Result.simple(Integer.class));
  }
}