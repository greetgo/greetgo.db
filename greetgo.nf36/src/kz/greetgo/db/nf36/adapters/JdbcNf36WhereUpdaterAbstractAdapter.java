package kz.greetgo.db.nf36.adapters;

import kz.greetgo.db.Jdbc;
import kz.greetgo.db.nf36.core.Nf36WhereUpdater;
import kz.greetgo.db.nf36.core.SqlLogAcceptor;

public abstract class JdbcNf36WhereUpdaterAbstractAdapter implements Nf36WhereUpdater {
  Jdbc jdbc;
  SqlLogAcceptor logAcceptor = null;

  @Override
  public void setNf3TableName(String tableName) {

  }

  @Override
  public void setAuthorFieldNames(String nf3CreatedBy, String nf3ModifiedBy, String nf6InsertedBy) {

  }

  @Override
  public Nf36WhereUpdater setAuthor(Object author) {
    return null;
  }

  @Override
  public void setTimeFieldName(String timeFieldName) {

  }

  @Override
  public void setIdFieldNames(String... idFieldNames) {

  }

  @Override
  public void updateFieldToNow(String fieldName) {

  }

  @Override
  public void setField(String nf6TableName, String fieldName, Object fieldValue) {

  }

  @Override
  public void where(String fieldName, Object fieldValue) {

  }

  @Override
  public void commit() {

  }
}
