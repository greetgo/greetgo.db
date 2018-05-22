package kz.greetgo.db.worker.oracle;


import kz.greetgo.conf.sys_params.SysParams;
import kz.greetgo.db.worker.DbConfig;
import kz.greetgo.db.worker.utils.UtilsFiles;
import kz.greetgo.depinject.core.Bean;
import kz.greetgo.depinject.core.BeanGetter;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Bean
public class DbWorkerOracle {

  public BeanGetter<DbConfig> dbConfig;

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
        exec(con, "alter session set \"_ORACLE_SCRIPT\"=true");
      } catch (SQLException e) {
        //noinspection StatementWithEmptyBody
        if (e.getMessage().startsWith("ORA-02248:")) {
          //ignore
        } else throw e;
      }

      try {
        exec(con, "drop user " + dbConfig.get().username() + " cascade");
      } catch (SQLException e) {
        //noinspection StatementWithEmptyBody
        if (e.getMessage().startsWith("ORA-01918:")) {
          //ignore
        } else throw e;
      }

      exec(con, "create user " + dbConfig.get().username() + " identified by " + dbConfig.get().password());
      exec(con, "grant all privileges to " + dbConfig.get().username());

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
