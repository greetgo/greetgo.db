package nf36_example_with_depinject.generated.impl.oracle.save;

import java.lang.String;
import java.util.function.Predicate;
import kz.greetgo.db.nf36.core.Nf36Saver;
import nf36_example_with_depinject.generated.faces.save.StreetSave;
import nf36_example_with_depinject.structure.StreetType;

public class StreetSaveImpl implements StreetSave {
  private final Nf36Saver saver13;

  public StreetSaveImpl(Nf36Saver saver) {
    this.saver13 = saver;
    saver.setNf3TableName("street");
    saver.setTimeFieldName("ts");
    saver.setAuthorFieldNames("created_by", "modified_by", "inserted_by");
    saver.addIdName("id");
    saver.addFieldName("m_street_type", "type");
    saver.addFieldName("m_street_name", "name");
  }

  private final type type = new type() {
    @Override
    public StreetSave set(StreetType value) {
      saver13.presetValue("type", value);
      return StreetSaveImpl.this;
    }

    @Override
    public StreetSave skipIf(Predicate<StreetType>  predicate) {
      saver13.addSkipIf("type", predicate);
      return StreetSaveImpl.this;
    }

    @Override
    public StreetSave alias(String alias) {
      saver13.addAlias("type", alias);
      return StreetSaveImpl.this;
    }

  };

  @Override
  public type type() {
    return type;
  }

  private final name name = new name() {
    @Override
    public StreetSave set(String value) {
      saver13.presetValue("name", value);
      return StreetSaveImpl.this;
    }

    @Override
    public StreetSave skipIf(Predicate<String>  predicate) {
      saver13.addSkipIf("name", predicate);
      return StreetSaveImpl.this;
    }

    @Override
    public StreetSave alias(String alias) {
      saver13.addAlias("name", alias);
      return StreetSaveImpl.this;
    }

  };

  @Override
  public name name() {
    return name;
  }

  @Override
  public void save(Object objectWithData) {
    saver13.save(objectWithData);
  }
}
