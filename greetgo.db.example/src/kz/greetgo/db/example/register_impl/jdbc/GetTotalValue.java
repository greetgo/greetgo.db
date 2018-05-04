package kz.greetgo.db.example.register_impl.jdbc;

import kz.greetgo.db.ConnectionCallback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GetTotalValue implements ConnectionCallback<Long> {
  @Override
  public Long doInConnection(Connection con) throws Exception {
    try (PreparedStatement ps = con.prepareStatement("select sum(value) from wiring")) {
      try (ResultSet rs = ps.executeQuery()) {
        if (!rs.next()) throw new RuntimeException("No records in wiring");
        return rs.getLong(1);
      }
    }
  }
}