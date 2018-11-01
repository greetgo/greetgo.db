package kz.greetgo.db.nf36.bridges;

import kz.greetgo.db.nf36.core.Nf36Saver;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class SaverUpserterBridge implements Nf36Saver {
  private final Nf36Upserter upserter;

  public SaverUpserterBridge(Nf36Upserter upserter) {
    this.upserter = upserter;
  }

  @Override
  public void setNf3TableName(String nf3TableName) {
    upserter.setNf3TableName(nf3TableName);
  }

  @Override
  public void setTimeFieldName(String timeFieldName) {
    upserter.setTimeFieldName(timeFieldName);
  }

  @Override
  public void setAuthorFieldNames(String nf3CreatedBy, String nf3ModifiedBy, String nf6InsertedBy) {
    upserter.setAuthorFieldNames(nf3CreatedBy, nf3ModifiedBy, nf6InsertedBy);
  }

  private final List<String> idNameList = new ArrayList<>();

  @Override
  public void addIdName(String idName) {
    idNameList.add(idName);
  }

  private static class TableField {
    final String nf6TableName;
    final String fieldName;

    public TableField(String nf6TableName, String fieldName) {
      this.nf6TableName = nf6TableName;
      this.fieldName = fieldName;
    }
  }

  private final List<TableField> tableFieldList = new ArrayList<>();

  @Override
  public void addFieldName(String nf6TableName, String fieldName) {
    tableFieldList.add(new TableField(nf6TableName, fieldName));
  }

  @Override
  public void putUpdateToNow(String timestampFieldName) {
    upserter.putUpdateToNow(timestampFieldName);
  }

  @Override
  public void setAuthor(Object author) {
    upserter.setAuthor(author);
  }

  @Override
  public void presetValue(String fieldName, Object value) {
    throw new NotImplementedException();
  }

  private static class Skip {
    final String fieldName;
    final Predicate<?> predicate;

    public Skip(String fieldName, Predicate<?> predicate) {
      this.fieldName = fieldName;
      this.predicate = predicate;
    }
  }

  private final List<Skip> skipList = new ArrayList<>();

  @Override
  public void addSkipIf(String fieldName, Predicate<?> predicate) {
    skipList.add(new Skip(fieldName, predicate));
  }

  private final Map<String, String> aliasMap = new HashMap<>();

  @Override
  public void addAlias(String fieldName, String alias) {
    aliasMap.put(fieldName, alias);
  }

  @Override
  public void save(Object objectWithData) {
    throw new NotImplementedException();
  }
}
