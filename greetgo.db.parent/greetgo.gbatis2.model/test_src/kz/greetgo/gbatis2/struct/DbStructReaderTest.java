package kz.greetgo.gbatis2.struct;

import kz.greetgo.gbatis2.struct.exceptions.OptionAlreadyDefined;
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

  @Test
  public void read_two_equal_options() {
    ResourceRef ref = ClassResourceRef.create(TestDbStruct.class, "two_equal_options.dbStruct");
    DbStructReader.read(ref);
  }

  @Test(expectedExceptions = OptionAlreadyDefined.class)
  public void read_OptionAlreadyDefined() {
    ResourceRef ref = ClassResourceRef.create(TestDbStruct.class, "two_options_with_same_keys.dbStruct");
    DbStructReader.read(ref);
  }

  @Test
  public void read_options() {
    ResourceRef ref = ClassResourceRef.create(TestDbStruct.class, "options.left.dbStruct");
    DbStruct dbStruct = DbStructReader.read(ref);

    assertThat(dbStruct.options.identification()).isEqualTo("id43214543");
    assertThat(dbStruct.options.tablePrefixId()).isEqualTo("m_65454534");
    assertThat(dbStruct.options.tablePrefixMemory()).isEqualTo("m_49842393829");
    assertThat(dbStruct.options.tablePrefixOperative()).isEqualTo("o_67263721");
    assertThat(dbStruct.options.concatenationSuffix()).isEqualTo("_4345435");
  }
}
