package kz.greetgo.db.nf36.gen.example.jdbc;

import kz.greetgo.db.ConnectionCallback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static kz.greetgo.db.nf36.gen.example.util.CorrectionUtil.correctType;

public class ByOne<T> implements ConnectionCallback<T> {

  private final String idName;
  private final Object idValue;
  private final String tableName;
  private final String field;

  public ByOne(String idName, Object idValue, String tableName, String field) {
    this.idName = idName;
    this.idValue = idValue;
    this.tableName = tableName;
    this.field = field;
  }

  @Override
  public T doInConnection(Connection con) throws Exception {

    try (PreparedStatement ps = con.prepareStatement(
      "select " + field + " from " + tableName + " where " + idName + " = ?")) {

      ps.setObject(1, idValue);

      try (ResultSet rs = ps.executeQuery()) {

        if (!rs.next()) {
          throw new RuntimeException("No record with " + idName + " = " + idValue + " in table " + tableName);
        }

        return correctType(rs.getObject(1));

      }
    }
  }
}
