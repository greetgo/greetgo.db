package kz.greetgo.gbatis.util.callbacks.comment;

import kz.greetgo.db.ConnectionExecutor;
import kz.greetgo.gbatis.futurecall.SqlViewer;
import kz.greetgo.gbatis.model.Result;
import kz.greetgo.gbatis.model.SqlWithParams;
import kz.greetgo.gbatis.util.OperUtil;
import kz.greetgo.gbatis.util.model.ObjectComment;
import kz.greetgo.gbatis.util.sqls.SqlSrc;

import java.sql.Connection;
import java.util.List;

public class TableCommentListCallback implements ConnectionExecutor<List<ObjectComment>> {
  public SqlViewer sqlViewer;

  private final String tableName;

  public TableCommentListCallback(String tableName) {
    this.tableName = tableName;
  }

  public TableCommentListCallback(SqlViewer sqlViewer, String tableName) {
    this(tableName);
    this.sqlViewer = sqlViewer;
  }

  @Override
  public List<ObjectComment> execute(Connection con) throws Exception {
    String sqlStr = SqlSrc.get(con).sql("comment/tableCommentList");
    SqlWithParams sql = SqlWithParams.select(sqlStr, tableName);
    return OperUtil.call(con, sql, Result.listOf(ObjectComment.class).with(sqlViewer));
  }
}
