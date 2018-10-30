package nf36_example_with_depinject.generated.impl.postgres.save.transaction;

import java.lang.String;
import java.util.function.Predicate;
import kz.greetgo.db.nf36.core.Nf36Saver;
import nf36_example_with_depinject.generated.faces.save.transaction.TransactionSave;

public class TransactionSaveImpl implements TransactionSave {
  private final Nf36Saver saver13;

  public TransactionSaveImpl(Nf36Saver saver) {
    this.saver13 = saver;
    saver.setNf3TableName("transaction");
    saver.setTimeFieldName("ts");
    saver.setAuthorFieldNames("created_by", "modified_by", "inserted_by");
    saver.addIdName("id");
    saver.addFieldName("memory_never_be_superfluous.transaction_description", "description");
  }

  private final description description = new description() {
    @Override
    public TransactionSave set(String value) {
      saver13.presetValue("description", value);
      return TransactionSaveImpl.this;
    }

    @Override
    public TransactionSave skipIf(Predicate<String>  predicate) {
      saver13.addSkipIf("description", predicate);
      return TransactionSaveImpl.this;
    }

    @Override
    public TransactionSave alias(String alias) {
      saver13.addAlias("description", alias);
      return TransactionSaveImpl.this;
    }

  };

  @Override
  public description description() {
    return description;
  }

  @Override
  public void save(Object objectWithData) {
    saver13.save(objectWithData);
  }
}
