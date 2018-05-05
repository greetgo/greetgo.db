package kz.greetgo.db.nf36.gen.example.beans;

import kz.greetgo.db.ConnectionCallback;
import kz.greetgo.db.Jdbc;
import kz.greetgo.db.nf36.gen.example.env.DbParamsPostgres;
import kz.greetgo.depinject.core.Bean;

import java.sql.Connection;
import java.sql.DriverManager;

@Bean
public class JdbcBean implements Jdbc {
  @Override
  public <T> T execute(ConnectionCallback<T> connectionCallback) {

    try {
      Class.forName("org.postgresql.Driver");

      try (Connection connection = DriverManager.getConnection(
        DbParamsPostgres.url, DbParamsPostgres.username, DbParamsPostgres.password
      )) {
        return connectionCallback.doInConnection(connection);
      }

    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }
}
