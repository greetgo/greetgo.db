package kz.greetgo.db.worker.oracle;


import kz.greetgo.db.worker.db.DbConfig;
import kz.greetgo.db.worker.util.OracleUtil;
import kz.greetgo.db.worker.utils.UtilsFiles;
import kz.greetgo.depinject.core.Bean;
import kz.greetgo.depinject.core.BeanGetter;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

@Bean
public class DbWorkerOracle {

  public BeanGetter<DbConfig> dbConfig;

  public void recreateDb() throws Exception {
    OracleUtil.recreateDb(dbConfig.get().username(), dbConfig.get().password());
  }

  public Connection getConnection() throws Exception {
    Class.forName("oracle.jdbc.driver.OracleDriver");
    return DriverManager.getConnection(dbConfig.get().url(), dbConfig.get().username(), dbConfig.get().password());
  }

  public void applySqlFile(File sqlFile) {
    try (Connection con = getConnection()) {
      for (String sql : UtilsFiles.fileToStr(sqlFile).split(";;")) {
        OracleUtil.exec(con, sql);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
