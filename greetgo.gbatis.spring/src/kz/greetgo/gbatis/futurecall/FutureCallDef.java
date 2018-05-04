package kz.greetgo.gbatis.futurecall;

import kz.greetgo.db.ConnectionCallback;
import kz.greetgo.db.Jdbc;
import kz.greetgo.gbatis.model.FutureCall;
import kz.greetgo.gbatis.model.Request;
import kz.greetgo.sqlmanager.gen.Conf;
import kz.greetgo.sqlmanager.model.Stru;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

/**
 * Реализация получения отложенных данных
 *
 * @author pompei
 * @see FutureCall
 */
public class FutureCallDef<T> implements FutureCall<T> {

  private final FutureCallImpl<T> delegate;

  public FutureCallDef(Conf conf, Stru stru, final JdbcTemplate jdbcTemplate, Request request, Object[] args) {

    Jdbc jdbc = new Jdbc() {
      @Override
      public <T1> T1 execute(final ConnectionCallback<T1> connectionCallback) {
        return jdbcTemplate.execute(new org.springframework.jdbc.core.ConnectionCallback<T1>() {
          @Override
          public T1 doInConnection(Connection con) throws SQLException, DataAccessException {
            try {
              return connectionCallback.doInConnection(con);
            } catch (Exception e) {
              if (e instanceof DataAccessException) throw (DataAccessException) e;
              if (e instanceof SQLException) throw (SQLException) e;
              if (e instanceof RuntimeException) throw (RuntimeException) e;
              throw new RuntimeException(e);
            }
          }
        });
      }
    };

    delegate = new FutureCallImpl<T>(conf, stru, jdbc, request, args);
  }

  @Override
  public T last() {
    return delegate.last();
  }

  @Override
  public T last(int offset, int pageSize) {
    return delegate.last(offset, pageSize);
  }

  @Override
  public T at(Date at) {
    return delegate.at(at);
  }

  @Override
  public T at(Date at, int offset, int pageSize) {
    return delegate.at(at, offset, pageSize);
  }

  public void setSqlViewer(SqlViewer sqlViewer) {
    delegate.request.result.sqlViewer = sqlViewer;
  }
}
