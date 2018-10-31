package nf36_example_with_depinject.jdbc;

import kz.greetgo.db.ConnectionCallback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CountWhere implements ConnectionCallback<Integer> {

  private final String tableName;
  private final String where;
  private final Object[] values;

  public CountWhere(String tableName, String where, Object... values) {
    this.tableName = tableName;
    this.where = where;
    this.values = values;
  }

  @Override
  public Integer doInConnection(Connection con) throws Exception {

    try (PreparedStatement ps = con.prepareStatement(
        "select count(1) from " + tableName + (where == null ? "" : " where " + where))) {

      {
        int index = 1;
        for (Object value : values) {
          ps.setObject(index++, value);
        }
      }

      try (ResultSet rs = ps.executeQuery()) {

        rs.next();

        return rs.getInt(1);

      }
    }
  }
}
