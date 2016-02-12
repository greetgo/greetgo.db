package kz.greetgo.db.multi_sessions;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DataSourcePreparation {

  protected final ComboPooledDataSource ds;

  public DataSourcePreparation() throws Exception {
    ds = new ComboPooledDataSource();
    ds.setDriverClass("org.postgresql.Driver");
    ds.setJdbcUrl("jdbc:postgresql://localhost:5432/probe");
    ds.setUser("probe");
    ds.setPassword("probe");

    ds.setMaxStatements(100);
    ds.setMaxConnectionAge(10 * 1000);
    ds.setMaxPoolSize(100);
  }
}
