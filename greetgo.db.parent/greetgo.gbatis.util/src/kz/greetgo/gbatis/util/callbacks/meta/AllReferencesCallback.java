package kz.greetgo.gbatis.util.callbacks.meta;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import kz.greetgo.gbatis.futurecall.SqlViewer;
import kz.greetgo.gbatis.model.Result;
import kz.greetgo.gbatis.model.SqlWithParams;
import kz.greetgo.gbatis.util.OperUtil;
import kz.greetgo.gbatis.util.model.TableReference;
import kz.greetgo.gbatis.util.sqls.SqlSrc;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;

public class AllReferencesCallback implements ConnectionCallback<Set<TableReference>> {
  public SqlViewer sqlViewer;
  
  public AllReferencesCallback() {}
  
  public AllReferencesCallback(SqlViewer sqlViewer) {
    this.sqlViewer = sqlViewer;
  }
  
  @Override
  public Set<TableReference> doInConnection(Connection con) throws SQLException,
      DataAccessException {
    String sqlStr = SqlSrc.get(con).sql("meta/allReferences");
    SqlWithParams sql = SqlWithParams.select(sqlStr);
    return OperUtil.call(con, sql, Result.setOf(TableReference.class).with(sqlViewer));
  }
}
