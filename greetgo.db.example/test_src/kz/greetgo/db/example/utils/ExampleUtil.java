package kz.greetgo.db.example.utils;

import kz.greetgo.conf.sys_params.SysParams;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ExampleUtil {

  private static String extractDbName(String url) {
    final int i = url.lastIndexOf('/');
    if (i < 0) throw new RuntimeException("Left url = " + url);
    return url.substring(i + 1);
  }

  public static Connection createPostgresAdminConnection() throws Exception {
    Class.forName("org.postgresql.Driver");
    return DriverManager.getConnection(
      SysParams.pgAdminUrl(),
      SysParams.pgAdminUserid(),
      SysParams.pgAdminPassword()
    );
  }

  private static void exec(Connection con, String sql) throws SQLException {
    try (Statement statement = con.createStatement()) {
      statement.execute(sql);
    }
  }

  public static void recreateDb(String url, String username, String password) throws Exception {
    final String dbName = extractDbName(url);

    try (Connection con = createPostgresAdminConnection()) {

      try {
        exec(con, "drop database " + dbName);
      } catch (SQLException e) {
        //noinspection StatementWithEmptyBody
        if ("3D000".equals(e.getSQLState())) ; //ignore
        else throw e;
      }

      try {
        exec(con, "drop user " + username);
      } catch (SQLException e) {
        //noinspection StatementWithEmptyBody
        if ("42704".equals(e.getSQLState())) ; //ignore
        else throw e;
      }

      exec(con, "create user " + username + " with password '" + password + "'");
      exec(con, "create database " + dbName + " with owner = " + username);

    } catch (SQLException e) {
      throw new RuntimeException("SQLState = " + e.getSQLState(), e);
    }
  }
}
