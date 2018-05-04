package kz.greetgo.db.example.register_impl.jdbc;

import kz.greetgo.db.ConnectionCallback;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class SetWiringValue implements ConnectionCallback<Void> {

  private final String wiringId;
  private long value;

  public SetWiringValue(String wiringId, long value) {
    this.wiringId = wiringId;
    this.value = value;
  }

  @Override
  public Void doInConnection(Connection con) throws Exception {
    try (PreparedStatement ps = con.prepareStatement("update wiring set value = ? where id = ?")) {

      ps.setLong(1, value);
      ps.setString(2, wiringId);

      if (ps.executeUpdate() == 0) throw new RuntimeException("No wiring with id = " + wiringId);

      return null;
    }
  }
}
