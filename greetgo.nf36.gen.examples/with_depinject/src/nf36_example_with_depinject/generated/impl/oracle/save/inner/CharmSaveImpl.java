package nf36_example_with_depinject.generated.impl.oracle.save.inner;

import java.lang.String;
import java.util.function.Predicate;
import kz.greetgo.db.nf36.core.Nf36Saver;
import nf36_example_with_depinject.generated.faces.save.inner.CharmSave;

public class CharmSaveImpl implements CharmSave {
  private final Nf36Saver saver13;

  public CharmSaveImpl(Nf36Saver saver) {
    this.saver13 = saver;
    saver.setNf3TableName("charm");
    saver.setTimeFieldName("ts");
    saver.setAuthorFieldNames("created_by", "modified_by", "inserted_by");
    saver.addIdName("id");
    saver.addFieldName("m_charm_name", "name");
  }

  private final name name = new name() {
    @Override
    public CharmSave set(String value) {
      saver13.presetValue("name", value);
      return CharmSaveImpl.this;
    }

    @Override
    public CharmSave skipIf(Predicate<String>  predicate) {
      saver13.addSkipIf("name", predicate);
      return CharmSaveImpl.this;
    }

    @Override
    public CharmSave alias(String alias) {
      saver13.addAlias("name", alias);
      return CharmSaveImpl.this;
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
