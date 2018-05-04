package kz.greetgo.gbatis.gen;

import kz.greetgo.db.Jdbc;
import kz.greetgo.gbatis.gen.gbatis.ZNF3;
import kz.greetgo.gbatis.t.DbAccessInfo;
import kz.greetgo.sqlmanager.gen.Conf;
import kz.greetgo.sqlmanager.model.Stru;

public class TestDbAccessInfo implements DbAccessInfo {
  public TestDbAccessInfo() {
  }

  @Override
  public Conf conf() {
    return ZNF3.getConf();
  }

  @Override
  public Stru stru() {
    try {
      return ZNF3.struGen().stru;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Jdbc jdbc() {
    return null;
  }
}
