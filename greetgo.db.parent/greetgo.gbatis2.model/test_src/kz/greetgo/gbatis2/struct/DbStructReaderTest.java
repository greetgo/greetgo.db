package kz.greetgo.gbatis2.struct;

import kz.greetgo.gbatis2.struct.resource.ClassResourceRef;
import kz.greetgo.gbatis2.struct.resource.ResourceRef;
import kz.greetgo.gbatis2.struct.struct.TestDbStruct;
import org.testng.annotations.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class DbStructReaderTest {
  @Test
  public void read_notNull() {
    ResourceRef ref = ClassResourceRef.create(TestDbStruct.class, "test.dbStruct");

    DbStruct dbStruct = DbStructReader.read(ref);

    assertThat(dbStruct).isNotNull();
  }

  @Test
  public void read_simpleParsed() {
    ResourceRef ref = ClassResourceRef.create(TestDbStruct.class, "test.dbStruct");

    DbStruct dbStruct = DbStructReader.read(ref);

    assertThat(dbStruct).isNotNull();

    assertThat(dbStruct.typeMap.size()).isGreaterThanOrEqualTo(3);
    assertThat(dbStruct.typeMap.keySet()).contains("client");

    assertThat(dbStruct.typeMap.get("client").subpackage).isEqualTo("main.core");
    assertThat(dbStruct.typeMap.get("client").name).isEqualTo("client");
    assertThat(dbStruct.typeMap.get("client").type).isEqualTo("str50");
    assertThat(dbStruct.typeMap.get("client").comment).isEqualTo("сущность клиент");

    assertThat(dbStruct.typeMap.get("client").fieldList).hasSize(3);

    assertThat(dbStruct.typeMap.get("client").fieldList.get(0).name).isEqualTo("surname");
    assertThat(dbStruct.typeMap.get("client").fieldList.get(0).type).isEqualTo("str500");
    assertThat(dbStruct.typeMap.get("client").fieldList.get(0).comment).isEqualTo("фамилия клиента");
  }
}
