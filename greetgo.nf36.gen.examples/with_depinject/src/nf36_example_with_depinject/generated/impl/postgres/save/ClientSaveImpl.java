package nf36_example_with_depinject.generated.impl.postgres.save;

import java.lang.Long;
import java.lang.String;
import java.util.function.Predicate;
import kz.greetgo.db.nf36.core.Nf36Saver;
import nf36_example_with_depinject.generated.faces.save.ClientSave;

public class ClientSaveImpl implements ClientSave {
  private final Nf36Saver saver13;

  public ClientSaveImpl(Nf36Saver saver) {
    this.saver13 = saver;
    saver.setNf3TableName("client");
    saver.setTimeFieldName("ts");
    saver.setAuthorFieldNames("created_by", "modified_by", "inserted_by");
    saver.addIdName("id");
    saver.addFieldName("memory_never_be_superfluous.client_surname", "surname");
    saver.addFieldName("memory_never_be_superfluous.client_name", "name");
    saver.addFieldName("memory_never_be_superfluous.client_patronymic", "patronymic");
    saver.addFieldName("memory_never_be_superfluous.client_charm_id", "charm_id");
    saver.addFieldName("memory_never_be_superfluous.client_long_description", "long_description");
    saver.addFieldName("memory_never_be_superfluous.client_my_chair_id1", "my_chair_id1");
    saver.addFieldName("memory_never_be_superfluous.client_my_chair_id1", "my_chair_id2");
    saver.addFieldName("memory_never_be_superfluous.client_his_chair_long_id", "his_chair_long_id");
    saver.addFieldName("memory_never_be_superfluous.client_his_chair_long_id", "his_chair_str_id");
  }

  private final surname surname = new surname() {
    @Override
    public ClientSave set(String value) {
      saver13.presetValue("surname", value);
      return ClientSaveImpl.this;
    }

    @Override
    public ClientSave skipIf(Predicate<String>  predicate) {
      saver13.addSkipIf("surname", predicate);
      return ClientSaveImpl.this;
    }

    @Override
    public ClientSave alias(String alias) {
      saver13.addAlias("surname", alias);
      return ClientSaveImpl.this;
    }

  };

  @Override
  public surname surname() {
    return surname;
  }

  private final name name = new name() {
    @Override
    public ClientSave set(String value) {
      saver13.presetValue("name", value);
      return ClientSaveImpl.this;
    }

    @Override
    public ClientSave skipIf(Predicate<String>  predicate) {
      saver13.addSkipIf("name", predicate);
      return ClientSaveImpl.this;
    }

    @Override
    public ClientSave alias(String alias) {
      saver13.addAlias("name", alias);
      return ClientSaveImpl.this;
    }

  };

  @Override
  public name name() {
    return name;
  }

  private final patronymic patronymic = new patronymic() {
    @Override
    public ClientSave set(String value) {
      saver13.presetValue("patronymic", value);
      return ClientSaveImpl.this;
    }

    @Override
    public ClientSave skipIf(Predicate<String>  predicate) {
      saver13.addSkipIf("patronymic", predicate);
      return ClientSaveImpl.this;
    }

    @Override
    public ClientSave alias(String alias) {
      saver13.addAlias("patronymic", alias);
      return ClientSaveImpl.this;
    }

  };

  @Override
  public patronymic patronymic() {
    return patronymic;
  }

  private final charmId charmId = new charmId() {
    @Override
    public ClientSave set(String value) {
      saver13.presetValue("charm_id", value);
      return ClientSaveImpl.this;
    }

    @Override
    public ClientSave skipIf(Predicate<String>  predicate) {
      saver13.addSkipIf("charm_id", predicate);
      return ClientSaveImpl.this;
    }

    @Override
    public ClientSave alias(String alias) {
      saver13.addAlias("charm_id", alias);
      return ClientSaveImpl.this;
    }

  };

  @Override
  public charmId charmId() {
    return charmId;
  }

  private final longDescription longDescription = new longDescription() {
    @Override
    public ClientSave set(String value) {
      saver13.presetValue("long_description", value);
      return ClientSaveImpl.this;
    }

    @Override
    public ClientSave skipIf(Predicate<String>  predicate) {
      saver13.addSkipIf("long_description", predicate);
      return ClientSaveImpl.this;
    }

    @Override
    public ClientSave alias(String alias) {
      saver13.addAlias("long_description", alias);
      return ClientSaveImpl.this;
    }

  };

  @Override
  public longDescription longDescription() {
    return longDescription;
  }

  private final myChairId1 myChairId1 = new myChairId1() {
    @Override
    public ClientSave set(Long value) {
      saver13.presetValue("my_chair_id1", value);
      return ClientSaveImpl.this;
    }

    @Override
    public ClientSave skipIf(Predicate<Long>  predicate) {
      saver13.addSkipIf("my_chair_id1", predicate);
      return ClientSaveImpl.this;
    }

    @Override
    public ClientSave alias(String alias) {
      saver13.addAlias("my_chair_id1", alias);
      return ClientSaveImpl.this;
    }

  };

  @Override
  public myChairId1 myChairId1() {
    return myChairId1;
  }

  private final myChairId2 myChairId2 = new myChairId2() {
    @Override
    public ClientSave set(String value) {
      saver13.presetValue("my_chair_id2", value);
      return ClientSaveImpl.this;
    }

    @Override
    public ClientSave skipIf(Predicate<String>  predicate) {
      saver13.addSkipIf("my_chair_id2", predicate);
      return ClientSaveImpl.this;
    }

    @Override
    public ClientSave alias(String alias) {
      saver13.addAlias("my_chair_id2", alias);
      return ClientSaveImpl.this;
    }

  };

  @Override
  public myChairId2 myChairId2() {
    return myChairId2;
  }

  private final hisChairLongId hisChairLongId = new hisChairLongId() {
    @Override
    public ClientSave set(Long value) {
      saver13.presetValue("his_chair_long_id", value);
      return ClientSaveImpl.this;
    }

    @Override
    public ClientSave skipIf(Predicate<Long>  predicate) {
      saver13.addSkipIf("his_chair_long_id", predicate);
      return ClientSaveImpl.this;
    }

    @Override
    public ClientSave alias(String alias) {
      saver13.addAlias("his_chair_long_id", alias);
      return ClientSaveImpl.this;
    }

  };

  @Override
  public hisChairLongId hisChairLongId() {
    return hisChairLongId;
  }

  private final hisChairStrId hisChairStrId = new hisChairStrId() {
    @Override
    public ClientSave set(String value) {
      saver13.presetValue("his_chair_str_id", value);
      return ClientSaveImpl.this;
    }

    @Override
    public ClientSave skipIf(Predicate<String>  predicate) {
      saver13.addSkipIf("his_chair_str_id", predicate);
      return ClientSaveImpl.this;
    }

    @Override
    public ClientSave alias(String alias) {
      saver13.addAlias("his_chair_str_id", alias);
      return ClientSaveImpl.this;
    }

  };

  @Override
  public hisChairStrId hisChairStrId() {
    return hisChairStrId;
  }

  @Override
  public void saveAll(Object objectWithData) {
    saver13.putUpdateToNow("mod_at");
    saver13.save(objectWithData);
  }
}
