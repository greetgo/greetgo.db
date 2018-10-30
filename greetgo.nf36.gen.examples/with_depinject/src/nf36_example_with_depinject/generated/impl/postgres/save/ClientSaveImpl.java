package nf36_example_with_depinject.generated.impl.postgres.save;

import kz.greetgo.db.nf36.core.Nf36Saver;
import nf36_example_with_depinject.generated.faces.save.ClientSave;

import java.util.function.Predicate;

public class ClientSaveImpl implements ClientSave {
  private final Nf36Saver saver1;

  public ClientSaveImpl(Nf36Saver saver) {
    this.saver1 = saver;
    saver.setNf3TableName("client");
    saver.setTimeFieldName("ts");
    saver.setAuthorFieldNames("created_by", "modified_by", "inserted_by");
    saver.addIdName("id");
    saver.addFieldName("memory_never_be_superfluous.client_surname", "surname");
    saver.addFieldName("memory_never_be_superfluous.client_myChairId1", "myChairId1");
  }

  private final surname surname = new surname() {
    @Override
    public ClientSave set(String value) {
      saver1.presetValue("surname", value);
      return ClientSaveImpl.this;
    }

    @Override
    public ClientSave skipIf(Predicate<String> predicate) {
      saver1.addSkipIf("surname", predicate);
      return ClientSaveImpl.this;
    }

    @Override
    public ClientSave alias(String alias) {
      saver1.addAlias("surname", alias);
      return ClientSaveImpl.this;
    }
  };

  @Override
  public surname surname() {
    return surname;
  }

  private final myChairId1 myChairId1 = new myChairId1() {
    @Override
    public ClientSave set(Long value) {
      return ClientSaveImpl.this;
    }

    @Override
    public ClientSave skipIf(Predicate<Long> predicate) {
      return ClientSaveImpl.this;
    }

    @Override
    public ClientSave alias(String alias) {
      return ClientSaveImpl.this;
    }
  };

  @Override
  public myChairId1 myChairId1() {
    return myChairId1;
  }

  @Override
  public void save(Object objectWithData) {
    saver1.save(objectWithData);
  }
}
