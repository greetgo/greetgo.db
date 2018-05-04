package kz.greetgo.db.example.register_impl.jdbc;

import kz.greetgo.db.ConnectionCallback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ExecParams implements ConnectionCallback<Integer> {

  private final String sql;
  private final Object[] params;

  public ExecParams(String sql, Object... params) {
    this.sql = sql;
    this.params = params;
  }

  @Override
  public Integer doInConnection(Connection con) throws Exception {

    System.out.println("Exec sql : " + sql + " ; with " + Arrays.stream(params).map(Object::toString)
      .collect(Collectors.joining(", ")));

    try (PreparedStatement ps = con.prepareStatement(sql)) {
      int index = 1;
      for (Object param : params) {
        ps.setObject(index++, param);
      }

      return ps.executeUpdate();
    }
  }
}
