package nf36_example_with_depinject.util;

import kz.greetgo.db.ConnectionCallback;
import kz.greetgo.db.Jdbc;
import kz.greetgo.db.worker.DbParameters;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.depinject.testng.AbstractDepinjectTestNg;

import java.sql.PreparedStatement;

public abstract class ParentDbTests extends AbstractDepinjectTestNg {

  public BeanGetter<DbParameters> dbParameters;

  protected String t(String tableName) {
    if (tableName.startsWith("m_")) {
      return dbParameters.get().nf6prefix() + tableName.substring(2);
    }
    return tableName;
  }

  public BeanGetter<Jdbc> jdbc;

  protected void exec(String sql) {
    jdbc.get().execute((ConnectionCallback<Void>) con -> {
      try (PreparedStatement st = con.prepareStatement(sql)) {
        System.out.println("EXEC SQL " + sql);
        st.executeUpdate();
      }
      return null;
    });
  }
}
