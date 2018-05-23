package nf36_example_with_depinject.util;

import kz.greetgo.db.worker.DbParameters;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.depinject.testng.AbstractDepinjectTestNg;

public abstract class ParentDbTests extends AbstractDepinjectTestNg {

  public BeanGetter<DbParameters> dbParameters;

  protected String t(String tableName) {
    if (tableName.startsWith("m_")) {
      return dbParameters.get().nf6prefix() + tableName.substring(2);
    }
    return tableName;
  }
}
