package nf36_example_with_depinject.generated.impl.oracle.save.inner;

import java.lang.String;
import java.util.function.Predicate;
import kz.greetgo.db.nf36.core.Nf36Saver;
import nf36_example_with_depinject.generated.faces.save.inner.ChairSave;

public class ChairSaveImpl implements ChairSave {
  private final Nf36Saver saver13;

  public ChairSaveImpl(Nf36Saver saver) {
    this.saver13 = saver;
    saver.setNf3TableName("chair");
    saver.setTimeFieldName("ts");
    saver.setAuthorFieldNames("created_by", "modified_by", "inserted_by");
    saver.addIdName("id1");
    saver.addIdName("id2");
    saver.addFieldName("m_chair_name", "name");
    saver.addFieldName("m_chair_description", "description");
  }

  private final name name = new name() {
    @Override
    public ChairSave set(String value) {
      saver13.presetValue("name", value);
      return ChairSaveImpl.this;
    }

    @Override
    public ChairSave skipIf(Predicate<String>  predicate) {
      saver13.addSkipIf("name", predicate);
      return ChairSaveImpl.this;
    }

    @Override
    public ChairSave alias(String alias) {
      saver13.addAlias("name", alias);
      return ChairSaveImpl.this;
    }

  };

  @Override
  public name name() {
    return name;
  }

  private final description description = new description() {
    @Override
    public ChairSave set(String value) {
      saver13.presetValue("description", value);
      return ChairSaveImpl.this;
    }

    @Override
    public ChairSave skipIf(Predicate<String>  predicate) {
      saver13.addSkipIf("description", predicate);
      return ChairSaveImpl.this;
    }

    @Override
    public ChairSave alias(String alias) {
      saver13.addAlias("description", alias);
      return ChairSaveImpl.this;
    }

  };

  @Override
  public description description() {
    return description;
  }

  @Override
  public void save(Object objectWithData) {
    saver13.putUpdateToNow("mod_at");
    saver13.save(objectWithData);
  }
}
