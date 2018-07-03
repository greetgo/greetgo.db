package kz.greetgo.db.nf36.adapters;

import kz.greetgo.db.Jdbc;
import kz.greetgo.db.nf36.core.SequenceNext;
import kz.greetgo.db.nf36.core.SqlLogAcceptor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
      try (PreparedStatement ps = con.prepareStatement(selectSequence(sequenceName))) {
        try (ResultSet rs = ps.executeQuery()) {
          if (!rs.next()) throw new RuntimeException("Left error");
          return taker.take(rs);
        }
      }
    });
  }

  protected abstract String selectSequence(String sequenceName);
}
