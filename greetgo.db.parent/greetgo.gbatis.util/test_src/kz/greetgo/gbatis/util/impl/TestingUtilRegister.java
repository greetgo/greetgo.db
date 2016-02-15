package kz.greetgo.gbatis.util.impl;

import java.sql.Connection;

import kz.greetgo.gbatis.futurecall.SqlViewer;
import kz.greetgo.util.db.ConnectionDataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public class TestingUtilRegister extends AbstractUtilRegister {
  
  private JdbcTemplate jdbc;
  
  @Override
  protected JdbcTemplate jdbc() {
    return jdbc;
  }
  
  public void setConnection(final Connection con) {
    jdbc = new JdbcTemplate(new ConnectionDataSource(con));
  }
  
  public SqlViewer sqlViewer;
  
  @Override
  protected SqlViewer sqlViewer() {
    return sqlViewer;
  }
  
}
