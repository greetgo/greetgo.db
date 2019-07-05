package kz.greetgo.gbatis.spring.beans;

import kz.greetgo.conf.sys_params.SysParams;
import kz.greetgo.gbatis.util.AbstractWithDbTest;
import kz.greetgo.gbatis.util.DataSourceAbstract;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static kz.greetgo.gbatis.util.AbstractWithDbTest.changeDb;

@Component
public class DataSourceFab {

  @Bean
  public DataSource getDataSource() throws Exception {
    if (dataSource == null) createDataSource();
    return dataSource;
  }

  private DataSource dataSource;

  private void createDataSource() throws Exception {
    this.dataSource = new DataSourceAbstract() {
      @Override
      public Connection getConnection() throws SQLException {
        try {
          Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
          throw new RuntimeException(e);
        }
        return DriverManager.getConnection(changeDb(SysParams.pgAdminUrl(), AbstractWithDbTest.getUserid()),
          AbstractWithDbTest.getUserid(), AbstractWithDbTest.getUserid());
      }
    };
  }

  @Bean
  public JdbcTemplate getJdbcTemplate() throws Exception {
    JdbcTemplate ret = new JdbcTemplate();
    ret.setDataSource(getDataSource());
    return ret;
  }

}
