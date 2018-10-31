package nf36_example_with_depinject.errors;

import java.sql.SQLException;

public class SqlError extends RuntimeException {

  public enum Type {
    DROP_TABLE, UNKNOWN;
  }

  public final Type type;

  public SqlError(SQLException e) {
    super("SqlState = " + e.getSQLState() + " : " + e.getMessage(), e);
    type = calculateType(e);
  }

  private static String takeMessage(Exception e) {
    if (e == null) {
      return "";
    }
    String message = e.getMessage();
    return message == null ? "" : message;
  }

  private static Type calculateType(SQLException e) {
    if ("42P01".equals(e.getSQLState())) {
      return Type.DROP_TABLE;
    }
    if (takeMessage(e).startsWith("ORA-00942:")) {
      return Type.DROP_TABLE;
    }
    return Type.UNKNOWN;
  }
}
