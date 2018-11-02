package nf36_example_with_depinject.generated.impl.oracle.save;

import java.lang.String;
import java.util.function.Predicate;
import kz.greetgo.db.nf36.core.Nf36Saver;
import nf36_example_with_depinject.generated.faces.save.EntityEnumAsIdSave;

public class EntityEnumAsIdSaveImpl implements EntityEnumAsIdSave {
  private final Nf36Saver saver13;

  public EntityEnumAsIdSaveImpl(Nf36Saver saver) {
    this.saver13 = saver;
    saver.setNf3TableName("entity_enum_as_id");
    saver.setTimeFieldName("ts");
    saver.setAuthorFieldNames("created_by", "modified_by", "inserted_by");
    saver.addIdName("id");
    saver.addFieldName("m_entity_enum_as_id_value", "value");
  }

  private final value value = new value() {
    @Override
    public EntityEnumAsIdSave set(String value) {
      saver13.presetValue("value", value);
      return EntityEnumAsIdSaveImpl.this;
    }

    @Override
    public EntityEnumAsIdSave skipIf(Predicate<String>  predicate) {
      saver13.addSkipIf("value", predicate);
      return EntityEnumAsIdSaveImpl.this;
    }

    @Override
    public EntityEnumAsIdSave alias(String alias) {
      saver13.addAlias("value", alias);
      return EntityEnumAsIdSaveImpl.this;
    }

  };

  @Override
  public value value() {
    return value;
  }

  @Override
  public void save(Object objectWithData) {
    saver13.putUpdateToNow("mod_at");
    saver13.save(objectWithData);
  }
}
