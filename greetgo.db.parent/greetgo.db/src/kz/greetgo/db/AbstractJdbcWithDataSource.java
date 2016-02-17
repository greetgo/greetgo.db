package kz.greetgo.db;

import javax.sql.DataSource;
import java.sql.Connection;

public abstract class AbstractJdbcWithDataSource implements Jdbc {

  protected abstract DataSource getDataSource();

  protected abstract TransactionManager getTransactionManager();

  @Override
  public <T> T executeConnection(ConnectionCallback<T> connectionCallback) {
    final TransactionManager tm = getTransactionManager();
    final DataSource dataSource = getDataSource();

    if (dataSource == null) throw new NullPointerException("dataSource == null");

    if (tm == null) {
      try (Connection connection = dataSource.getConnection()) {
        return connectionCallback.doInConnection(connection);
      } catch (Exception e) {
        if (e instanceof RuntimeException) throw (RuntimeException) e;
        throw new RuntimeException(e);
      }
    }

    final Connection connection = tm.getConnection(dataSource);
    try {
      return connectionCallback.doInConnection(connection);
    } catch (Exception e) {
      if (e instanceof RuntimeException) throw (RuntimeException) e;
      throw new RuntimeException(e);
    } finally {
      tm.closeConnection(dataSource, connection);
    }
  }
}
