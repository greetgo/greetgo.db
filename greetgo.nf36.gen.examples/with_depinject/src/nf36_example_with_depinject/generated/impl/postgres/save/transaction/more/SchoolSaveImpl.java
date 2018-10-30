package nf36_example_with_depinject.generated.impl.postgres.save.transaction.more;

import java.lang.String;
import java.util.function.Predicate;
import kz.greetgo.db.nf36.core.Nf36Saver;
import nf36_example_with_depinject.generated.faces.save.transaction.more.SchoolSave;

public class SchoolSaveImpl implements SchoolSave {
  private final Nf36Saver saver13;

  public SchoolSaveImpl(Nf36Saver saver) {
    this.saver13 = saver;
    saver.setNf3TableName("school");
    saver.setTimeFieldName("ts");
    saver.setAuthorFieldNames("created_by", "modified_by", "inserted_by");
    saver.addIdName("id");
    saver.addFieldName("memory_never_be_superfluous.school_name", "name");
  }

  private final name name = new name() {
    @Override
    public SchoolSave set(String value) {
      saver13.presetValue("name", value);
      return SchoolSaveImpl.this;
    }

    @Override
    public SchoolSave skipIf(Predicate<String>  predicate) {
      saver13.addSkipIf("name", predicate);
      return SchoolSaveImpl.this;
    }

    @Override
    public SchoolSave alias(String alias) {
      saver13.addAlias("name", alias);
      return SchoolSaveImpl.this;
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
