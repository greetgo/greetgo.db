package kz.greetgo.db.nf36.adapters;

import kz.greetgo.db.Jdbc;
import kz.greetgo.db.nf36.core.Nf36HistorySelector;
import kz.greetgo.db.nf36.core.SqlLogAcceptor;

import java.util.Date;
import java.util.function.Consumer;

abstract class JdbcNf36HistorySelectorAbstractAdapter implements Nf36HistorySelector {
  boolean selectAuthor = false;
  Jdbc jdbc;
  SqlLogAcceptor logAcceptor = null;

  @Override
  public void setNf3TableName(String nf3TableName) {

  }

  @Override
  public Nf36HistorySelector field(String nf6TableName, String dbFieldName, String aliasName) {
    return null;
  }

  @Override
  public void peekSql(Consumer<String> sqlConsumer) {

  }

  @Override
  public void putTo(Object destinationObject) {

  }

  @Override
  public void addId(String idName) {

  }

  @Override
  public void addIdAlias(String idName, String idAlias) {

  }

  @Override
  public void at(Date date) {

  }
}
