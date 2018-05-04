package kz.greetgo.gbatis.spring.beans;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import kz.greetgo.conf.SysParams;
import kz.greetgo.gbatis.util.AbstractWithDbTest;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

import static kz.greetgo.gbatis.util.AbstractWithDbTest.changeDb;

@Component
public class DataSourceFab {
  
  @Bean(destroyMethod = "close")
  public DataSource getDataSource() throws Exception {
    if (dataSource == null) createDataSource();
    return dataSource;
  }
  
  private DataSource dataSource;
  
  private void createDataSource() throws Exception {
    ComboPooledDataSource ds = new ComboPooledDataSource();
    ds.setDriverClass("org.postgresql.Driver");
    ds.setJdbcUrl(changeDb(SysParams.pgAdminUrl(), AbstractWithDbTest.getUserid()));
    ds.setUser(AbstractWithDbTest.getUserid());
    ds.setPassword(AbstractWithDbTest.getUserid());
    
    ds.setAcquireIncrement(1);
    ds.setMinPoolSize(1);
    ds.setMaxPoolSize(3);
    ds.setMaxIdleTime(120);
    
    this.dataSource = ds;
  }
  
  @Bean
  public JdbcTemplate getJdbcTemplate() throws Exception {
    JdbcTemplate ret = new JdbcTemplate();
    ret.setDataSource(getDataSource());
    return ret;
  }
  
}
