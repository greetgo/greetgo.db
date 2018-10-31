package kz.greetgo.db.nf36.bridges;

import kz.greetgo.db.nf36.core.Nf36Saver;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.function.Predicate;

public class SaverUpserterBridge implements Nf36Saver {
  public static Nf36Saver newBridge(Nf36Upserter upserter) {
    return new SaverUpserterBridge(upserter);
  }

  public SaverUpserterBridge(Nf36Upserter upserter) {
    throw new NotImplementedException();
  }

  @Override
  public void setNf3TableName(String nf3TableName) {
    throw new NotImplementedException();
  }

  @Override
  public void setTimeFieldName(String timeFieldName) {
    throw new NotImplementedException();
  }

  @Override
  public void setAuthorFieldNames(String nf3CreatedBy, String nf3ModifiedBy, String nf6InsertedBy) {
    throw new NotImplementedException();
  }

  @Override
  public void addIdName(String idName) {
    throw new NotImplementedException();
  }

  @Override
  public void addFieldName(String nf6TableName, String fieldName) {
    throw new NotImplementedException();
  }

  @Override
  public void presetValue(String fieldName, Object value) {
    throw new NotImplementedException();
  }

  @Override
  public void addSkipIf(String fieldName, Predicate<?> predicate) {
    throw new NotImplementedException();
  }

  @Override
  public void addAlias(String fieldName, String alias) {
    throw new NotImplementedException();
  }

  @Override
  public void save(Object objectWithData) {
    throw new NotImplementedException();
  }
}
