package nf36_example_with_depinject.beans.oracle;

import kz.greetgo.db.ConnectionCallback;
import kz.greetgo.db.Jdbc;
import kz.greetgo.depinject.core.Bean;
import nf36_example_with_depinject.env.DbParamsOracle;

import java.sql.Connection;
import java.sql.DriverManager;

@Bean
public class JdbcOracle implements Jdbc {
  @Override
  public <T> T execute(ConnectionCallback<T> connectionCallback) {

    try {
      Class.forName("oracle.jdbc.driver.OracleDriver");

      try (Connection connection = DriverManager.getConnection(
        DbParamsOracle.url, DbParamsOracle.username, DbParamsOracle.password
      )) {
        return connectionCallback.doInConnection(connection);
      }

    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }
}
