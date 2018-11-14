package kz.greetgo.db.worker.util;

import kz.greetgo.conf.sys_params.SysParams;

import java.sql.Connection;
import java.sql.DriverManager;

import static kz.greetgo.db.worker.utils.DbUrlUtils.changeUrlDbName;

public class PostgresUtil {

  public static Connection getAdminConnection() throws Exception {
    Class.forName("org.postgresql.Driver");
    return DriverManager.getConnection(
        SysParams.pgAdminUrl(),
        SysParams.pgAdminUserid(),
        SysParams.pgAdminPassword()
    );
  }

  public static Connection getConnection(String dbName, String username, String password) throws Exception {
    Class.forName("org.postgresql.Driver");
    return DriverManager.getConnection(
        changeUrlDbName(SysParams.pgAdminUrl(), dbName), username, password
    );
  }
}
