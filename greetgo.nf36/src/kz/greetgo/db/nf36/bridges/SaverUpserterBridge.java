package kz.greetgo.db.nf36.bridges;

import kz.greetgo.db.nf36.core.Nf36Saver;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

  private final List<IdField> idFieldList = new ArrayList<>();

  @Override
  public void addIdName(String idName) {
    idFieldList.add(new IdField(idName));
  }

  private final List<DataField> dataFieldList = new ArrayList<>();

  @Override
  public void addFieldName(String nf6TableName, String fieldName) {
    dataFieldList.add(new DataField(nf6TableName, fieldName));
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

  private final SkipList skipList = new SkipList();

  @Override
  public void addSkipIf(String fieldName, Predicate<Object> predicate) {
    skipList.addSkipIf(fieldName, predicate);
  }

  private final AliasMapper aliasMapper = new AliasMapper();

  @Override
  public void addAlias(String fieldName, String alias) {
    aliasMapper.addAlias(fieldName, alias);
  }

  private static final ClassAccessorStorage classAccessorStorage = new ClassAccessorStorage();

  @Override
  public void save(Object objectWithData) {
    Objects.requireNonNull(objectWithData);

    applyAliases();

    putIdFields(objectWithData);

    putDataFields(objectWithData);

    upserter.commit();
  }

  private void putIdFields(Object objectWithData) {
    ClassAccessor classAccessor = classAccessorStorage.get(objectWithData.getClass());

    for (IdField f : idFieldList) {
      Object idValue = classAccessor.extractValue(f.name(), objectWithData);
      upserter.putId(f.name(), idValue);
    }
  }

  private void putDataFields(Object objectWithData) {
    ClassAccessor classAccessor = classAccessorStorage.get(objectWithData.getClass());

    for (DataField f : dataFieldList) {

      if (classAccessor.isAbsent(f.fieldName())) {
        continue;
      }

      Object fieldValue = classAccessor.extractValue(f.fieldName(), objectWithData);

      if (skipList.needSkip(f.fieldName(), fieldValue)) {
        continue;
      }

      upserter.putField(f.nf6TableName, f.fieldName(), fieldValue);
    }
  }

  private void applyAliases() {
    for (DataField f : dataFieldList) {
      f.applyConverter(aliasMapper::convert);
    }
    for (IdField f : idFieldList) {
      f.applyConverter(aliasMapper::convert);
    }
  }
}
