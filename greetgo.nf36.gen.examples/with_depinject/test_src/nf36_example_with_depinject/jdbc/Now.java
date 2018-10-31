package nf36_example_with_depinject.jdbc;

import kz.greetgo.db.ConnectionCallback;
import kz.greetgo.db.DbType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static kz.greetgo.db.nf36.utils.SqlConvertUtil.fromSql;

public class Now implements ConnectionCallback<Date> {

  @Override
  public Date doInConnection(Connection con) throws Exception {

    try (PreparedStatement ps = con.prepareStatement(selectCurrentTimestamp(con))) {
      try (ResultSet rs = ps.executeQuery()) {

        if (!rs.next()) {
          throw new RuntimeException("FATALITY");
        }

        return fromSql(rs.getObject(1), Date.class);

      }
    }
  }

  private String selectCurrentTimestamp(Connection con) throws SQLException {
    switch (DbType.detect(con)) {

      case Oracle:
        return "select localtimestamp from dual";

      case Postgres:
      case MySQL:
        return "select current_timestamp";

      default:
        throw new RuntimeException("Unknown such database");
    }
  }
}
