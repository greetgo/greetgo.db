package nf36_example_with_depinject.env;

import kz.greetgo.conf.sys_params.SysParams;

import static nf36_example_with_depinject.util.DbUrlUtils.changeUrlDbName;

public class DbParamsPostgres {
  public static final String username = System.getProperty("user.name") + "_nf36_example";
  public static final String password = System.getProperty("user.name") + "_nf36_example_123";
  public static final String dbName = username;
  public static final String url = changeUrlDbName(SysParams.pgAdminUrl(), username);
}
