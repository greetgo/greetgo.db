package nf36_example_with_depinject.generated.impl.postgres.save;

import java.lang.Boolean;
import java.lang.String;
import java.util.function.Predicate;
import kz.greetgo.db.nf36.core.Nf36Saver;
import nf36_example_with_depinject.generated.faces.save.StoneSave;

public class StoneSaveImpl implements StoneSave {
  private final Nf36Saver saver13;

  public StoneSaveImpl(Nf36Saver saver) {
    this.saver13 = saver;
    saver.setNf3TableName("stone");
    saver.setTimeFieldName("ts");
    saver.setAuthorFieldNames("created_by", "modified_by", "inserted_by");
    saver.addIdName("id");
    saver.addFieldName("memory_never_be_superfluous.stone_name", "name");
    saver.addFieldName("memory_never_be_superfluous.stone_actual", "actual");
  }

  private final name name = new name() {
    @Override
    public StoneSave set(String value) {
      saver13.presetValue("name", value);
      return StoneSaveImpl.this;
    }

    @Override
    public StoneSave skipIf(Predicate<String>  predicate) {
      saver13.addSkipIf("name", predicate);
      return StoneSaveImpl.this;
    }

    @Override
    public StoneSave alias(String alias) {
      saver13.addAlias("name", alias);
      return StoneSaveImpl.this;
    }

  };

  @Override
  public name name() {
    return name;
  }

  private final actual actual = new actual() {
    @Override
    public StoneSave set(boolean value) {
      saver13.presetValue("actual", value);
      return StoneSaveImpl.this;
    }

    @Override
    public StoneSave skipIf(Predicate<Boolean>  predicate) {
      saver13.addSkipIf("actual", predicate);
      return StoneSaveImpl.this;
    }

    @Override
    public StoneSave alias(String alias) {
      saver13.addAlias("actual", alias);
      return StoneSaveImpl.this;
    }

  };

  @Override
  public actual actual() {
    return actual;
  }

  @Override
  public void save(Object objectWithData) {
    saver13.putUpdateToNow("mod_at");
    saver13.save(objectWithData);
  }
}
