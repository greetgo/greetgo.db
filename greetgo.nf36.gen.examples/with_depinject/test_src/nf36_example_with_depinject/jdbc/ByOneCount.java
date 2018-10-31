package nf36_example_with_depinject.jdbc;

import kz.greetgo.db.ConnectionCallback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ByOneCount implements ConnectionCallback<Integer> {

  private final String idName;
  private final Object idValue;
  private final String tableName;

  public ByOneCount(String idName, Object idValue, String tableName) {
    this.idName = idName;
    this.idValue = idValue;
    this.tableName = tableName;
  }

  @Override
  public Integer doInConnection(Connection con) throws Exception {

    try (PreparedStatement ps = con.prepareStatement(
        "select count(1) from " + tableName + " where " + idName + " = ?")) {

      ps.setObject(1, idValue);

      try (ResultSet rs = ps.executeQuery()) {

        if (!rs.next()) {
          throw new RuntimeException("No record with " + idName + " = " + idValue + " in table " + tableName);
        }

        return rs.getInt(1);

      }
    }
  }
}
