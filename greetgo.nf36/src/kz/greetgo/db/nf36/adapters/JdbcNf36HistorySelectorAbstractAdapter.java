package kz.greetgo.db.nf36.adapters;

import kz.greetgo.db.Jdbc;
import kz.greetgo.db.nf36.core.Nf36HistorySelector;
import kz.greetgo.db.nf36.core.SqlLogAcceptor;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Date;
import java.util.function.Consumer;

abstract class JdbcNf36HistorySelectorAbstractAdapter implements Nf36HistorySelector {
  boolean selectAuthor = false;
  Jdbc jdbc;
  SqlLogAcceptor logAcceptor = null;

  @Override
  public Nf36HistorySelector setNf3TableName(String nf3TableName) {
    throw new NotImplementedException();
  }

  @Override
  public Nf36HistorySelector field(String nf6TableName, String dbFieldName, String aliasName) {
    throw new NotImplementedException();
  }


  @Override
  public boolean putTo(Object destinationObject) {
    throw new NotImplementedException();
  }

  @Override
  public Nf36HistorySelector addId(String idName) {
    throw new NotImplementedException();
  }

  @Override
  public void addIdAlias(String idName, String idAlias) {
    throw new NotImplementedException();
  }

  @Override
  public Nf36HistorySelector at(Date date) {
    throw new NotImplementedException();
  }

  @Override
  public Nf36HistorySelector onAbsent(Consumer<Object> destinationObjectConsumer) {
    throw new NotImplementedException();
  }
}
