package kz.greetgo.db.nf36.util;

import java.sql.SQLException;

public class SqlError extends RuntimeException {
  public final SqlErrorType type;

  public SqlError(SQLException e) {
    super("SQLState = " + e.getSQLState() + " : " + e.getMessage(), e);
    type = defineType(e);
  }

  private static SqlErrorType defineType(SQLException e) {
    if ("42P01".equals(e.getSQLState())) {
      return SqlErrorType.DROP_TABLE;
    }
    if (e.getMessage().startsWith("ORA-00942:")) {
      return SqlErrorType.DROP_TABLE;
    }
    return SqlErrorType.UNKNOWN;
  }
}
