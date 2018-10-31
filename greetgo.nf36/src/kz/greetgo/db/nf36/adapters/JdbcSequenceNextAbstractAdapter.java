package kz.greetgo.db.nf36.adapters;

import kz.greetgo.db.Jdbc;
import kz.greetgo.db.nf36.core.SequenceNext;
import kz.greetgo.db.nf36.core.SqlLogAcceptor;
import kz.greetgo.db.nf36.model.SqlLog;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

abstract class JdbcSequenceNextAbstractAdapter implements SequenceNext {
  Jdbc jdbc;
  SqlLogAcceptor logAcceptor;

  private interface Taker<T> {
    T take(ResultSet rs) throws SQLException;
  }

  @Override
  public long nextLong(String sequenceName) {
    return next(sequenceName, rs -> rs.getLong(1));
  }

  @Override
  public int nextInt(String sequenceName) {
    return next(sequenceName, rs -> rs.getInt(1));
  }

  public <T> T next(String sequenceName, Taker<T> taker) {
    return jdbc.execute(con -> {
      long startedAt = System.nanoTime();
      String sql = selectSequence(sequenceName);
      try (PreparedStatement ps = con.prepareStatement(sql)) {
        try (ResultSet rs = ps.executeQuery()) {
          if (!rs.next()) {
            throw new RuntimeException("Left error");
          }
          T ret = taker.take(rs);
          logInfo(startedAt, sql);
          return ret;
        }
      } catch (Exception e) {
        throw logError(startedAt, sql, e);
      }
    });
  }

  private Exception logError(long startedAt, String sql, Exception e) {
    if (logAcceptor != null && logAcceptor.isErrorEnabled()) {
      logAcceptor.accept(new SqlLog(sql, new ArrayList<>(), e, System.nanoTime() - startedAt));
    }
    return e;
  }

  private void logInfo(long startedAt, String sql) {
    if (logAcceptor != null && logAcceptor.isTraceEnabled()) {
      logAcceptor.accept(new SqlLog(sql, new ArrayList<>(), null, System.nanoTime() - startedAt));
    }
  }

  protected abstract String selectSequence(String sequenceName);
}
