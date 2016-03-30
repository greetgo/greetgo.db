package kz.greetgo.gbatis2.struct.model;

import kz.greetgo.gbatis2.struct.model.std.StdLong;
import kz.greetgo.gbatis2.struct.model.std.StdStr;
import org.testng.annotations.Test;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class DbEssenceTest {
  private void addField(DbEssence dbEssence, String name, Essence up) {
    dbEssence.fieldList.add(new DbField(true, name, up, null));
  }

  @Test
  public void keyFields() throws Exception {

    DbEssence dbEssenceUpUp = new DbEssence(null, "upUp", null);
    addField(dbEssenceUpUp, "keyUpUp", StdLong.INSTANCE);

    DbEssence dbEssenceUp1 = new DbEssence(null, "up1", null);
    addField(dbEssenceUp1, "keyUp1_1", StdLong.INSTANCE);
    addField(dbEssenceUp1, "keyUp1_2", StdLong.INSTANCE);

    DbEssence dbEssenceUp2 = new DbEssence(null, "up2", null);
    addField(dbEssenceUp2, "keyUp2_1", StdLong.INSTANCE);
    addField(dbEssenceUp2, "keyUp2_2", dbEssenceUpUp);

    DbEssence dbEssence = new DbEssence(null, null, null);
    addField(dbEssence, "key1", dbEssenceUp1);
    addField(dbEssence, "key2", dbEssenceUp2);
    addField(dbEssence, "key3", new StdStr(40));

    List<KeyField> keyFields = dbEssence.keyFields();
    assertThat(keyFields).hasSize(5);

    assertThat(keyFields.get(0).name()).isEqualTo("keyUp1_1");
    assertThat(keyFields.get(4).name()).isEqualTo("key3");
  }

}