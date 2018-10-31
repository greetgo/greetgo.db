package nf36_example_with_depinject.env;

import kz.greetgo.conf.sys_params.SysParams;

public class DbParamsOracle {
  public static final String username = System.getProperty("user.name") + "_nf36_example";
  public static final String password = System.getProperty("user.name") + "_nf36_example_123";
  public static final String url = "jdbc:oracle:thin:@" + SysParams.oracleAdminHost()
      + ":" + SysParams.oracleAdminPort()
      + ":" + SysParams.oracleAdminSid();
}
