package kz.greetgo.db.example.register_impl.jdbc;

import kz.greetgo.db.ConnectionCallback;
import kz.greetgo.db.example.utils.TableAlreadyExists;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Exec implements ConnectionCallback<Void> {

  private final String sql;

  public Exec(String sql) {
    this.sql = sql;
  }

  @Override
  public Void doInConnection(Connection con) throws Exception {

    try (Statement statement = con.createStatement()) {
      statement.execute(sql);
      System.out.println("Exec sql : " + sql);
    } catch (SQLException e) {
      if ("42P07".equals(e.getSQLState())) throw new TableAlreadyExists(e);
      throw new RuntimeException("SQL state = " + e.getSQLState(), e);
    }

    return null;
  }
}
