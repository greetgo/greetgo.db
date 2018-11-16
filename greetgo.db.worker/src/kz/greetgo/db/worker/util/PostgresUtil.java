package kz.greetgo.db.worker.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import kz.greetgo.conf.sys_params.SysParams;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static kz.greetgo.db.worker.utils.DbUtils.changeUrlDbName;

public class PostgresUtil {

  public static Connection getAdminConnection() throws Exception {
    Class.forName("org.postgresql.Driver");
    return DriverManager.getConnection(
        SysParams.pgAdminUrl(),
        SysParams.pgAdminUserid(),
        SysParams.pgAdminPassword()
    );
  }

  public static Connection getConnection(String dbName, String username, String password) {
    try {
      Class.forName("org.postgresql.Driver");
      return DriverManager.getConnection(changeUrlDbName(SysParams.pgAdminUrl(), dbName), username, password);
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static DataSource createDataSource(String dbName, String username, String password) {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(changeUrlDbName(SysParams.pgAdminUrl(), dbName));
    config.setUsername(username);
    config.setPassword(password);

    return new HikariDataSource(config);
  }

  public static boolean ping(String dbName, String username, String password) {
    try {
      getConnection(dbName, username, password).close();
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private static void exec(Connection con, String sql) throws Exception {
    sql = sql.replace('\n', ' ').trim();
    if (sql.length() == 0) { return; }

    try (Statement statement = con.createStatement()) {
      System.out.println("Exec SQL: " + sql);
      statement.execute(sql);
    }
  }

  public static void recreateDb(String dbName, String username, String password) throws Exception {
    try (Connection con = PostgresUtil.getAdminConnection()) {
      exec(con, "drop database if exists " + dbName);
      exec(con, "drop user if exists " + username);
      exec(con, "create user " + username + " encrypted password '" + password + "'");
      exec(con, "create database " + dbName + " with owner = '" + username + "'");
    }
  }
}
