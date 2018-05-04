package kz.greetgo.gbatis.futurecall;

import kz.greetgo.db.ConnectionCallback;
import kz.greetgo.db.Jdbc;
import kz.greetgo.gbatis.model.FutureCall;
import kz.greetgo.gbatis.model.Request;
import kz.greetgo.gbatis.model.SqlWithParams;
import kz.greetgo.gbatis.util.OperUtil;
import kz.greetgo.sqlmanager.gen.Conf;
import kz.greetgo.sqlmanager.model.Stru;
import kz.greetgo.util.db.DbTypeDetector;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

/**
 * Реализация получения отложенных данных
 * 
 * @see FutureCall
 * @author pompei
 */
public class FutureCallImpl<T> implements FutureCall<T> {
  
  private final Conf conf;
  private final Stru stru;
  private final Jdbc jdbc;
  public final Request request;
  private final Object[] args;
  
  public FutureCallImpl(Conf conf, Stru stru, Jdbc jdbc, Request request, Object[] args) {
    this.conf = conf;
    this.stru = stru;
    this.jdbc = jdbc;
    this.request = request;
    this.args = args == null ? null :args.clone();
  }
  
  @Override
  public T last() {
    return at(null, 0, 0);
  }
  
  @Override
  public T at(Date at) {
    return at(at, 0, 0);
  }
  
  @Override
  public T last(final int offset, final int pageSize) {
    return at(null, offset, pageSize);
  }
  
  @Override
  public T at(final Date at, final int offset, final int pageSize) {
    return jdbc.execute(new ConnectionCallback<T>() {
      @Override
      public T doInConnection(Connection con) throws Exception {
        try {
          return onPagedWithConnection(con, at, offset, pageSize);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    });
  }
  
  private T onPagedWithConnection(Connection con, Date at, int offset, int pageSize)
      throws Exception {
    
    SqlWithParams sql = PreparedSql.prepare(conf, stru, request, args, at,
        DbTypeDetector.detect(con), offset, pageSize);
    
    return OperUtil.callException(con, sql, request.result);
  }
  
}
