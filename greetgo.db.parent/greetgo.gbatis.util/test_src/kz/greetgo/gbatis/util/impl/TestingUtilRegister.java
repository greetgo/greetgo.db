package kz.greetgo.gbatis.util.impl;

import kz.greetgo.db.Jdbc;
import kz.greetgo.db.JdbcOneConnection;
import kz.greetgo.gbatis.futurecall.SqlViewer;

import java.sql.Connection;

public class TestingUtilRegister extends AbstractUtilRegister {

  private Jdbc jdbc;

  @Override
  protected Jdbc jdbc() {
    return jdbc;
  }

  public void setConnection(final Connection con) {
    jdbc = new JdbcOneConnection(con);
  }

  public SqlViewer sqlViewer;

  @Override
  protected SqlViewer sqlViewer() {
    return sqlViewer;
  }

}
