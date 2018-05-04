package kz.greetgo.db;

import javax.sql.DataSource;
import java.sql.Connection;

public interface TransactionManager {
  Connection getConnection(DataSource dataSource);

  void upLevel(CallMeta isolationLevel);

  void downLevel(Throwable throwable);

  void closeConnection(DataSource dataSource, Connection connection);
}
