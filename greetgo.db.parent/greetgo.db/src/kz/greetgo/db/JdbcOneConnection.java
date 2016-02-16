package kz.greetgo.db;

import java.sql.Connection;

public class JdbcOneConnection implements Jdbc {

  private final Connection connection;

  public JdbcOneConnection(Connection connection) {
    this.connection = connection;
  }

  @Override
  public <T> T executeConnection(ConnectionExecutor<T> connectionExecutor) {
    try {
      return connectionExecutor.execute(connection);
    } catch (Exception e) {
      if (e instanceof RuntimeException) throw (RuntimeException) e;
      throw new RuntimeException(e);
    }
  }
}
