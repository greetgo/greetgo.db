package kz.greetgo.db.worker.util;

import org.testng.SkipException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static kz.greetgo.conf.sys_params.SysParams.*;

public class OracleUtil {
  public static void exec(Connection con, String sql) throws Exception {
    sql = sql.replace('\n', ' ').trim();
    if (sql.length() == 0) {
      return;
    }

    try (Statement statement = con.createStatement()) {
      System.out.println("Exec SQL: " + sql);
      statement.execute(sql);
    }
  }

  public static void recreateDb(String username, String password) throws Exception {
    try (Connection con = getOracleAdminConnection()) {

      try {
        exec(con, "alter session set \"_ORACLE_SCRIPT\"=true");
      } catch (SQLException e) {
        //noinspection StatementWithEmptyBody
        if (e.getMessage().startsWith("ORA-02248:")) {
          //ignore
        } else {
          throw e;
        }
      }


      try {
        exec(con, "drop user " + username + " cascade");
      } catch (SQLException e) {
        //noinspection StatementWithEmptyBody
        if (e.getMessage().startsWith("ORA-01918:")) {
          //ignore
        } else { throw e; }
      }


      exec(con, "create user " + username + " identified by " + password);
      exec(con, "grant all privileges to " + username);

    } catch (SQLException e) {
      throw new RuntimeException("SQL State = " + e.getSQLState() + " ---> " + e.getMessage(), e);
    }
  }

  public static Connection getConnection(String username, String password) {
    try {
      Class.forName("oracle.jdbc.driver.OracleDriver");
    } catch (ClassNotFoundException e) {
      throw new SkipException("" +
          "\n\tNo Oracle driver." +
          "\n\tPlease put Oracle driver into directory 'lib' in root project." +
          "\n\tSee README.md file in directory 'lib' in root project for details ");
    }

    String url = "jdbc:oracle:thin:@" + oracleAdminHost() + ":" + oracleAdminPort() + ":" + oracleAdminSid();

    try {
      return DriverManager.getConnection(url, username, password);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static Connection getOracleAdminConnection() throws Exception {
    return getConnection(oracleAdminUserid(), oracleAdminPassword());
  }

  public static boolean ping(String username, String password) {
    try {
      getConnection(username, password).close();
      return true;
    } catch (SkipException e) {
      throw e;
    } catch (Exception e) {
      return false;
    }
  }
}
