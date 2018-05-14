package kz.greetgo.db.nf36.gen.example.beans.oracle;

import kz.greetgo.db.ConnectionCallback;
import kz.greetgo.db.Jdbc;
import kz.greetgo.db.nf36.gen.example.env.DbParamsOracle;
import kz.greetgo.db.nf36.gen.example.env.DbParamsPostgres;
import kz.greetgo.depinject.core.Bean;

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
