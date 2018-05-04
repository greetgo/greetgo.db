package kz.greetgo.db;

import java.sql.SQLException;

public class SQLRuntimeException extends RuntimeException {

  public final SQLException wrappedException;

  public SQLRuntimeException(SQLException e) {
    super(e);
    wrappedException = e;
  }
}
