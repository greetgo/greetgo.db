package nf36_example_with_depinject.jdbc;

import kz.greetgo.db.ConnectionCallback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ByTwoCount implements ConnectionCallback<Integer> {

  private final String idName1;
  private final Object idValue1;
  private final String idName2;
  private final Object idValue2;
  private final String tableName;

  public ByTwoCount(String idName1, Object idValue1, String idName2, Object idValue2, String tableName) {
    this.idName1 = idName1;
    this.idValue1 = idValue1;
    this.idName2 = idName2;
    this.idValue2 = idValue2;
    this.tableName = tableName;
  }

  @Override
  public Integer doInConnection(Connection con) throws Exception {

    try (PreparedStatement ps = con.prepareStatement(
        "select count(1) from " + tableName + " where " + idName1 + " = ? and " + idName2 + " = ?")) {

      ps.setObject(1, idValue1);
      ps.setObject(2, idValue2);

      try (ResultSet rs = ps.executeQuery()) {

        rs.next();

        return rs.getInt(1);

      }
    }
  }
}
