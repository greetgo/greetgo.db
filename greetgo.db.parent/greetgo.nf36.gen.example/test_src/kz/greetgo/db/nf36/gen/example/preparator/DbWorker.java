package kz.greetgo.db.nf36.gen.example.preparator;


import kz.greetgo.conf.sys_params.SysParams;
import kz.greetgo.db.nf36.UtilsFiles;
import kz.greetgo.db.nf36.gen.example.env.DbParams;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DbWorker {

  private void exec(Connection con, String sql) throws Exception {
    sql = sql.replace('\n', ' ').trim();
    if (sql.length() == 0) return;

    try (Statement statement = con.createStatement()) {
      System.out.println("Exec SQL: " + sql);
      statement.execute(sql);
    }
  }

  public void recreateDb() throws Exception {
    try (Connection con = getPostgresAdminConnection()) {
      exec(con, "drop database if exists " + DbParams.dbName);
      exec(con, "drop user if exists " + DbParams.username);
      exec(con, "create user " + DbParams.username + " encrypted password '" + DbParams.password + "'");
      exec(con, "create database " + DbParams.dbName + " with owner = '" + DbParams.username + "'");
    }
  }

  public static Connection getPostgresAdminConnection() throws Exception {
    Class.forName("org.postgresql.Driver");
    return DriverManager.getConnection(
      SysParams.pgAdminUrl(),
      SysParams.pgAdminUserid(),
      SysParams.pgAdminPassword()
    );
  }

  public static Connection getConnection() throws Exception {
    Class.forName("org.postgresql.Driver");
    return DriverManager.getConnection(DbParams.url, DbParams.username, DbParams.password);
  }

  public void applySqlFile(File sqlFile) {
    try (Connection con = getConnection()) {
      for (String sql : UtilsFiles.fileToStr(sqlFile).split(";;")) {
        exec(con, sql);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
