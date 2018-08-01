package kz.greetgo.db;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractJdbcWithDataSource implements Jdbc {

  protected abstract DataSource getDataSource();

  protected abstract TransactionManager getTransactionManager();

  public class ConnectionDot implements AutoCloseable {
    final TransactionManager tm;
    final DataSource dataSource;

    final Connection connection;

    public ConnectionDot(TransactionManager tm, DataSource dataSource) {
      this.tm = tm;
      this.dataSource = dataSource;
      if (dataSource == null) throw new NullPointerException("dataSource == null");
      try {
        connection = tm == null ? dataSource.getConnection() : tm.getConnection(dataSource);
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    public void close() {
      if (tm == null) {
        try {
          connection.close();
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
      } else {
        tm.closeConnection(dataSource, connection);
      }
    }
  }

  protected ConnectionDot getConnectionDot() {
    return new ConnectionDot(getTransactionManager(), getDataSource());
  }

  @Override
  public <T> T execute(ConnectionCallback<T> connectionCallback) {

    try (ConnectionDot connectionDot = getConnectionDot()) {

      try {
        return connectionCallback.doInConnection(connectionDot.connection);
      } catch (RuntimeException e) {
        throw e;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }

    }

  }
}
