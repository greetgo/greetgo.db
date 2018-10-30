package nf36_example_with_depinject.generated.impl.oracle.save.inner;

import java.lang.String;
import java.util.function.Predicate;
import kz.greetgo.db.nf36.core.Nf36Saver;
import nf36_example_with_depinject.generated.faces.save.inner.WowSave;

public class WowSaveImpl implements WowSave {
  private final Nf36Saver saver13;

  public WowSaveImpl(Nf36Saver saver) {
    this.saver13 = saver;
    saver.setNf3TableName("wow");
    saver.setTimeFieldName("ts");
    saver.setAuthorFieldNames("created_by", "modified_by", "inserted_by");
    saver.addIdName("wow_id");
    saver.addIdName("wow_id2");
    saver.addFieldName("m_wow_hello", "hello");
  }

  private final hello hello = new hello() {
    @Override
    public WowSave set(String value) {
      saver13.presetValue("hello", value);
      return WowSaveImpl.this;
    }

    @Override
    public WowSave skipIf(Predicate<String>  predicate) {
      saver13.addSkipIf("hello", predicate);
      return WowSaveImpl.this;
    }

    @Override
    public WowSave alias(String alias) {
      saver13.addAlias("hello", alias);
      return WowSaveImpl.this;
    }

  };

  @Override
  public hello hello() {
    return hello;
  }

  @Override
  public void save(Object objectWithData) {
    saver13.save(objectWithData);
  }
}
