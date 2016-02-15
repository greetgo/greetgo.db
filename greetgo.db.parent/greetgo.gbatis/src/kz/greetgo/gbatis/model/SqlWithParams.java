package kz.greetgo.gbatis.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import kz.greetgo.gbatis.util.SqlUtil;
import kz.greetgo.util.db.DbType;

public class SqlWithParams {
  public RequestType type;
  public String sql;
  public final List<Object> params = new ArrayList<>();
  
  public static SqlWithParams select(String sql, Object... params) {
    SqlWithParams ret = new SqlWithParams();
    ret.type = RequestType.Sele;
    ret.sql = sql;
    for (Object p : params) {
      ret.params.add(p);
    }
    return ret;
  }
  
  public static SqlWithParams selectWith(String sql, Collection<Object> params) {
    SqlWithParams ret = new SqlWithParams();
    ret.type = RequestType.Sele;
    ret.sql = sql;
    ret.params.addAll(params);
    return ret;
  }
  
  public static SqlWithParams call(String sql, Object... params) {
    SqlWithParams ret = new SqlWithParams();
    ret.type = RequestType.Call;
    ret.sql = sql;
    for (Object p : params) {
      ret.params.add(p);
    }
    return ret;
  }
  
  public static SqlWithParams callWith(String sql, Collection<Object> params) {
    SqlWithParams ret = new SqlWithParams();
    ret.type = RequestType.Call;
    ret.sql = sql;
    ret.params.addAll(params);
    return ret;
  }
  
  public static SqlWithParams modi(String sql, Object... params) {
    SqlWithParams ret = new SqlWithParams();
    ret.type = RequestType.Modi;
    ret.sql = sql;
    for (Object p : params) {
      ret.params.add(p);
    }
    return ret;
  }
  
  public static SqlWithParams modiWith(String sql, Collection<Object> params) {
    SqlWithParams ret = new SqlWithParams();
    ret.type = RequestType.Modi;
    ret.sql = sql;
    ret.params.addAll(params);
    return ret;
  }
  
  public SqlWithParams page(DbType dbType, int offset, int count) {
    try {
      sql = SqlUtil.preparePagedSql(dbType, sql, offset, count, params);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return this;
  }
}
