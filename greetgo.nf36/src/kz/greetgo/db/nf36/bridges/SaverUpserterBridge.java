package kz.greetgo.db.nf36.bridges;

import kz.greetgo.db.nf36.core.Nf36Saver;
import kz.greetgo.db.nf36.core.Nf36Upserter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static kz.greetgo.db.nf36.bridges.ClassAccessorStorage.classAccessorStorage;

public class SaverUpserterBridge implements Nf36Saver {
  private final Nf36Upserter upserter;

  public SaverUpserterBridge(Nf36Upserter upserter) {
    this.upserter = upserter;
  }

  @Override
  public Nf36Saver setNf3TableName(String nf3TableName) {
    upserter.setNf3TableName(nf3TableName);
    return this;
  }

  @Override
  public Nf36Saver setTimeFieldName(String timeFieldName) {
    upserter.setTimeFieldName(timeFieldName);
    return this;
  }

  @Override
  public Nf36Saver setAuthorFieldNames(String nf3CreatedBy, String nf3ModifiedBy, String nf6InsertedBy) {
    upserter.setAuthorFieldNames(nf3CreatedBy, nf3ModifiedBy, nf6InsertedBy);
    return this;
  }

  private final List<IdField> idFieldList = new ArrayList<>();

  @Override
  public Nf36Saver addIdName(String idName) {
    idFieldList.add(new IdField(idName));
    return this;
  }

  private final List<DataField> dataFieldList = new ArrayList<>();

  @Override
  public Nf36Saver addFieldName(String nf6TableName, String fieldName) {
    dataFieldList.add(new DataField(nf6TableName, fieldName));
    return this;
  }

  @Override
  public Nf36Saver putUpdateToNow(String timestampFieldName) {
    upserter.putUpdateToNow(timestampFieldName);
    return this;
  }

  @Override
  public Nf36Saver setAuthor(Object author) {
    upserter.setAuthor(author);
    return this;
  }

  private final PresetValues presetValues = new PresetValues();

  @Override
  public Nf36Saver presetValue(String fieldName, Object value) {
    presetValues.presetValue(fieldName, value);
    return this;
  }

  private final SkipList skipList = new SkipList();

  @Override
  public Nf36Saver addSkipIf(String fieldName, Predicate<?> predicate) {
    skipList.addSkipIf(fieldName, predicate);
    return this;
  }

  private final AliasMapper aliasMapper = new AliasMapper();

  @Override
  public Nf36Saver addAlias(String fieldName, String alias) {
    aliasMapper.addAlias(fieldName, alias);
    return this;
  }

  @Override
  public void save(Object objectWithData) {
    Objects.requireNonNull(objectWithData);

    applyAliases();

    putIdFields(objectWithData);

    putDataFields(objectWithData);

    upserter.commit();
  }

  private void putIdFields(Object objectWithData) {
    ClassAccessor classAccessor = classAccessorStorage().get(objectWithData.getClass());

    for (IdField f : idFieldList) {
      upserter.putId(f.name, classAccessor.extractValue(f.alias(), objectWithData));
    }
  }

  private void putDataFields(Object objectWithData) {
    ClassAccessor classAccessor = classAccessorStorage().get(objectWithData.getClass());

    for (DataField f : dataFieldList) {

      final Object fieldValue;

      if (presetValues.exists(f.fieldName)) {
        fieldValue = presetValues.getValue(f.fieldName);
      } else {

        if (classAccessor.isAbsent(f.alias())) {
          continue;
        }

        fieldValue = classAccessor.extractValue(f.alias(), objectWithData);
      }

      if (skipList.needSkip(f.fieldName, fieldValue)) {
        continue;
      }

      upserter.putField(f.nf6TableName, f.fieldName, fieldValue);
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
