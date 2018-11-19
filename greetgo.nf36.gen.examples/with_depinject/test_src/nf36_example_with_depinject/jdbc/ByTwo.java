package nf36_example_with_depinject.jdbc;

import kz.greetgo.db.ConnectionCallback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static nf36_example_with_depinject.util.CorrectionUtil.correctTypeForJava;

@SuppressWarnings("unused")
public class ByTwo<T> implements ConnectionCallback<T> {

  private final String idName1;
  private final Object idValue1;
  private final String idName2;
  private final Object idValue2;
  private final String tableName;
  private final String field;

  public ByTwo(String idName1, Object idValue1, String idName2, Object idValue2, String tableName, String field) {
    this.idName1 = idName1;
    this.idValue1 = idValue1;
    this.idName2 = idName2;
    this.idValue2 = idValue2;
    this.tableName = tableName;
    this.field = field;
  }

  @Override
  public T doInConnection(Connection con) throws Exception {

    try (PreparedStatement ps = con.prepareStatement(
        "select " + field + " from " + tableName + " where " + idName1 + " = ? and " + idName2 + " = ?")) {

      ps.setObject(1, idValue1);
      ps.setObject(2, idValue2);

      try (ResultSet rs = ps.executeQuery()) {

        if (!rs.next()) {
          throw new RuntimeException("No record with " + idName1 + " = " + idValue1 + " and "
              + idName2 + " = " + idValue2 + " in table " + tableName);
        }

        return correctTypeForJava(rs.getObject(1), con);

      }
    }
  }
}
