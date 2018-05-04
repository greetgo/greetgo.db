package kz.greetgo.db.example.configs;

import static kz.greetgo.conf.sys_params.SysParams.pgAdminUrl;
import static kz.greetgo.db.example.utils.AllUtils.replaceDbName;

public class PostgresConfig {
  public static String url() {
    return replaceDbName(pgAdminUrl(), dbName());
  }

  public static String dbName() {
    return username();
  }

  public static String username() {
    return System.getProperty("user.name") + "_db_example";
  }

  public static String password() {
    return "111";
  }
}
