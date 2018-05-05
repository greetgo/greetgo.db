package kz.greetgo.db.worker;


import kz.greetgo.conf.sys_params.SysParams;
import kz.greetgo.db.worker.utils.UtilsFiles;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbWorkerOracle {

  private DbConfig dbConfig;

  public DbWorkerOracle(DbConfig dbConfig) {
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
    try (Connection con = getOracleAdminConnection()) {

      try {
        exec(con, "drop user " + dbConfig.username() + " cascade");
      } catch (SQLException e) {
        //noinspection StatementWithEmptyBody
        if (e.getMessage().startsWith("ORA-01918:")) {
          //ignore
        } else throw e;
      }

      exec(con, "create user " + dbConfig.username() + " identified by " + dbConfig.password());
      exec(con, "grant all privileges to " + dbConfig.username());
    } catch (SQLException e) {
      throw new RuntimeException("SQL State = " + e.getSQLState() + " ---> " + e.getMessage(), e);
    }
  }

  public static Connection getOracleAdminConnection() throws Exception {
    Class.forName("oracle.jdbc.driver.OracleDriver");
    String url = "jdbc:oracle:thin:@"
      + SysParams.oracleAdminHost() + ":" + SysParams.oracleAdminPort() + ":" + SysParams.oracleAdminSid();
    return DriverManager.getConnection(
      url, SysParams.oracleAdminUserid(), SysParams.oracleAdminPassword()
    );
  }

  public Connection getConnection() throws Exception {
    Class.forName("oracle.jdbc.driver.OracleDriver");
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
