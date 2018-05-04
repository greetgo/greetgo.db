package kz.greetgo.db.example.register_impl.jdbc;

import kz.greetgo.db.ConnectionCallback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class JdbcAdd implements ConnectionCallback<Integer> {

  private final int a, b;

  public JdbcAdd(int a, int b) {
    this.a = a;
    this.b = b;
  }

  @Override
  public Integer doInConnection(Connection con) throws Exception {
    //noinspection SqlNoDataSourceInspection
    try (PreparedStatement ps = con.prepareStatement("with asd as (select ? as a, ? as b) select a + b from asd")) {
      ps.setInt(1, a);
      ps.setInt(2, b);
      try (ResultSet rs = ps.executeQuery()) {
        if (!rs.next()) throw new RuntimeException("asd");
        return rs.getInt(1);
      }
    }
  }
}
