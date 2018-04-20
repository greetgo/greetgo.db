package kz.greetgo.db.nf36.gen.example.preparator;


import kz.greetgo.conf.sys_params.SysParams;
import kz.greetgo.db.nf36.gen.example.env.DbParams;
import org.postgresql.util.PSQLException;
import org.postgresql.util.ServerErrorMessage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbWorker {

  public static void main(String[] args) throws Exception {
    new DbWorker().recreate();
  }

  public void recreate() throws Exception {

    try (Connection con = getPostgresAdminConnection()) {

      try (Statement stt = con.createStatement()) {
        System.out.println("drop database " + DbParams.dbName);
        stt.execute("drop database if exists " + DbParams.dbName);
      } catch (PSQLException e) {
        System.err.println(e.getServerErrorMessage());
        throw e;
      }

      try (Statement stt = con.createStatement()) {
        System.out.println("drop user if exists " + DbParams.username);
        stt.execute("drop user if exists " + DbParams.username);
      } catch (SQLException e) {
        System.out.println(e.getMessage());
        //ignore
      }

      try (Statement stt = con.createStatement()) {
        System.out.println("create user " + DbParams.username + " encrypted password '" + DbParams.password + "'");
        stt.execute("create user " + DbParams.username + " encrypted password '" + DbParams.password + "'");
      } catch (PSQLException e) {
        System.out.println(e.getMessage());
        ServerErrorMessage sem = e.getServerErrorMessage();
        if ("CreateRole".equals(sem.getRoutine())) {
          throw new RuntimeException("Невозможно создать пользователя " + DbParams.username + ". Возможно кто-то" +
            " приконектился к базе данных под этим пользователем и поэтому он не удаляется." +
            " Попробуйте разорвать коннект с БД или перезапустить БД. После повторите операцию снова", e);
        }

        throw e;
      }

      try (Statement stt = con.createStatement()) {
        System.out.println("create database " + DbParams.dbName + " with owner = '" + DbParams.username + "'");
        stt.execute("create database " + DbParams.dbName + " with owner = '" + DbParams.username + "'");
      }
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
}
