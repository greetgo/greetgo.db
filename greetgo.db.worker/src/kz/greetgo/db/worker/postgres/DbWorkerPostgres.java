package kz.greetgo.db.worker.postgres;


import kz.greetgo.db.worker.db.DbConfig;
import kz.greetgo.db.worker.util.PostgresUtil;
import kz.greetgo.db.worker.utils.UtilsFiles;
import kz.greetgo.depinject.core.Bean;
import kz.greetgo.depinject.core.BeanGetter;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@Bean
public class DbWorkerPostgres {

  public BeanGetter<DbConfig> dbConfig;
  public BeanGetter<DbParametersPostgres> dbParameters;

  private void exec(Connection con, String sql) throws Exception {
    sql = sql.replace('\n', ' ').trim();
    if (sql.length() == 0) return;

    try (Statement statement = con.createStatement()) {
      System.out.println("Exec SQL: " + sql);
      statement.execute(sql);
    }
  }

  public void recreateDb() throws Exception {
    try (Connection con = PostgresUtil.getAdminConnection()) {
      exec(con, "drop database if exists " + dbConfig.get().dbName());
      exec(con, "drop user if exists " + dbConfig.get().username());
      exec(con, "create user " + dbConfig.get().username() + " encrypted password '" + dbConfig.get().password() + "'");
      exec(con, "create database " + dbConfig.get().dbName() + " with owner = '" + dbConfig.get().username() + "'");
    }

    try (Connection con = getConnection()) {
      exec(con, "create schema " + dbParameters.get().schema());
    }
  }

  public Connection getConnection() throws Exception {
    Class.forName("org.postgresql.Driver");
    return DriverManager.getConnection(dbConfig.get().url(), dbConfig.get().username(), dbConfig.get().password());
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
