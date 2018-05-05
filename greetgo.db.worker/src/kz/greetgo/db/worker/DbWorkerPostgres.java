package kz.greetgo.db.worker;


import kz.greetgo.conf.sys_params.SysParams;
import kz.greetgo.db.worker.utils.UtilsFiles;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DbWorkerPostgres {

  private DbConfig dbConfig;

  public DbWorkerPostgres(DbConfig dbConfig) {
    this.dbConfig = dbConfig;
  }

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
      exec(con, "drop database if exists " + dbConfig.dbName());
      exec(con, "drop user if exists " + dbConfig.username());
      exec(con, "create user " + dbConfig.username() + " encrypted password '" + dbConfig.password() + "'");
      exec(con, "create database " + dbConfig.dbName() + " with owner = '" + dbConfig.username() + "'");
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

  public Connection getConnection() throws Exception {
    Class.forName("org.postgresql.Driver");
    return DriverManager.getConnection(dbConfig.url(), dbConfig.username(), dbConfig.password());
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
