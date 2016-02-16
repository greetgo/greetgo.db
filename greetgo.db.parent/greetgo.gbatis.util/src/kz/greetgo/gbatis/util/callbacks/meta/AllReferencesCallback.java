package kz.greetgo.gbatis.util.callbacks.meta;

import kz.greetgo.db.ConnectionExecutor;
import kz.greetgo.gbatis.futurecall.SqlViewer;
import kz.greetgo.gbatis.model.Result;
import kz.greetgo.gbatis.model.SqlWithParams;
import kz.greetgo.gbatis.util.OperUtil;
import kz.greetgo.gbatis.util.model.TableReference;
import kz.greetgo.gbatis.util.sqls.SqlSrc;

import java.sql.Connection;
import java.util.Set;


public class AllReferencesCallback implements ConnectionExecutor<Set<TableReference>> {
  public SqlViewer sqlViewer;

  public AllReferencesCallback() {
  }

  public AllReferencesCallback(SqlViewer sqlViewer) {
    this.sqlViewer = sqlViewer;
  }

  @Override
  public Set<TableReference> execute(Connection con) throws Exception {
    String sqlStr = SqlSrc.get(con).sql("meta/allReferences");
    SqlWithParams sql = SqlWithParams.select(sqlStr);
    return OperUtil.call(con, sql, Result.setOf(TableReference.class).with(sqlViewer));
  }
}
