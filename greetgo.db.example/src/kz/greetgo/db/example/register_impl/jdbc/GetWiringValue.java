package kz.greetgo.db.example.register_impl.jdbc;

import kz.greetgo.db.ConnectionCallback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GetWiringValue implements ConnectionCallback<Long> {

  private final String wiringId;

  public GetWiringValue(String wiringId) {
    this.wiringId = wiringId;
  }

  @Override
  public Long doInConnection(Connection con) throws Exception {
    try (PreparedStatement ps = con.prepareStatement("select value from wiring where id = ? for update")) {
      ps.setString(1, wiringId);
      try (ResultSet rs = ps.executeQuery()) {
        if (!rs.next()) throw new RuntimeException("No wiring with id = " + wiringId);
        return rs.getLong(1);
      }
    }
  }
}
