package nf36_example_with_depinject.errors;

import java.sql.SQLException;

public class SqlError extends RuntimeException {

  public final String sqlState;

  public SqlError(SQLException e) {
    super("SqlState = " + e.getSQLState() + " : " + e.getMessage(), e);
    sqlState = e.getSQLState();
  }
}
