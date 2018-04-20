package kz.greetgo.db.nf36.gen.example.env;

import kz.greetgo.conf.sys_params.SysParams;

import static kz.greetgo.db.nf36.gen.example.preparator.DbUrlUtils.changeUrlDbName;

public class DbParams {
  public static final String username = "nf36_example";
  public static final String password = "nf36_example_123";
  public static final String dbName = username;
  public static final String url = changeUrlDbName(SysParams.pgAdminUrl(), username);
}
