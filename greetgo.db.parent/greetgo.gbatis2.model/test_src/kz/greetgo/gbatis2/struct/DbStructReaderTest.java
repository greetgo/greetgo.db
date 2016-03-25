package kz.greetgo.gbatis2.struct;

import kz.greetgo.gbatis2.struct.exceptions.*;
import kz.greetgo.gbatis2.struct.resource.ClassResourceRef;
import kz.greetgo.gbatis2.struct.resource.ResourceRef;
import kz.greetgo.gbatis2.struct.struct.TestDbStruct;
import org.testng.annotations.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class DbStructReaderTest {
  @Test
  public void read_notNull() throws Exception {
    ResourceRef ref = ClassResourceRef.create(TestDbStruct.class, "test.dbStruct");

    DbStruct dbStruct = DbStructReader.read(ref);

    assertThat(dbStruct).isNotNull();
  }

  @Test
  public void read_simpleParsed() throws Exception {
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
  public void read_two_equal_options() throws Exception {
    ResourceRef ref = ClassResourceRef.create(TestDbStruct.class, "two_equal_options.dbStruct");
    DbStructReader.read(ref);
  }

  @Test(expectedExceptions = OptionAlreadyDefined.class)
  public void read_OptionAlreadyDefined() throws Exception {
    ResourceRef ref = ClassResourceRef.create(TestDbStruct.class, "two_options_with_same_keys.dbStruct");
    DbStructReader.read(ref);
  }

  @Test
  public void read_options() throws Exception {
    ResourceRef ref = ClassResourceRef.create(TestDbStruct.class, "options.left.dbStruct");
    DbStruct dbStruct = DbStructReader.read(ref);

    assertThat(dbStruct.options.identification()).isEqualTo("id43214543");
    assertThat(dbStruct.options.tablePrefixId()).isEqualTo("m_65454534");
    assertThat(dbStruct.options.tablePrefixMemory()).isEqualTo("m_49842393829");
    assertThat(dbStruct.options.tablePrefixOperative()).isEqualTo("o_67263721");
    assertThat(dbStruct.options.concatenationSuffix()).isEqualTo("_4345435");
  }

  @Test
  public void read_two_equal_aliases() throws Exception {
    ResourceRef ref = ClassResourceRef.create(TestDbStruct.class, "two_equal_aliases.dbStruct");
    DbStructReader.read(ref);
  }

  @Test(expectedExceptions = AliasAlreadyDefined.class)
  public void read_AliasAlreadyDefined() throws Exception {
    ResourceRef ref = ClassResourceRef.create(TestDbStruct.class, "two_aliases_with_same_keys.dbStruct");
    DbStructReader.read(ref);
  }

  @Test
  public void read_two_equal_enums() throws Exception {
    ResourceRef ref = ClassResourceRef.create(TestDbStruct.class, "two_equal_enums.dbStruct");
    DbStructReader.read(ref);
  }

  @Test(expectedExceptions = EnumAlreadyDefined.class)
  public void read_EnumAlreadyDefined() throws Exception {
    ResourceRef ref = ClassResourceRef.create(TestDbStruct.class, "two_enums_with_same_keys.dbStruct");
    DbStructReader.read(ref);
  }

  @Test(expectedExceptions = FieldAlreadyExists.class)
  public void read_FieldAlreadyExists() throws Exception {
    ResourceRef ref = ClassResourceRef.create(TestDbStruct.class, "field.already.exists.dbStruct");
    DbStructReader.read(ref);
  }

  @Test(expectedExceptions = EnumClassNotFound.class)
  public void read_EnumClassNotFound() throws Exception {
    ResourceRef ref = ClassResourceRef.create(TestDbStruct.class, "enum.class.not.found.dbStruct");
    DbStructReader.read(ref);
  }

  @Test(expectedExceptions = EssenceAlreadyDefined.class)
  public void read_EssenceAlreadyDefined() throws Exception {
    ResourceRef ref = ClassResourceRef.create(TestDbStruct.class, "essence.already.defined.dbStruct");
    DbStructReader.read(ref);
  }

  @Test(expectedExceptions = UnknownLine.class)
  public void read_UnknownLine() throws Exception {
    ResourceRef ref = ClassResourceRef.create(TestDbStruct.class, "unknown.line.dbStruct");
    DbStructReader.read(ref);
  }

  @Test(expectedExceptions = OptionIsNotDefined.class)
  public void read_OptionIsNotDefined() throws Exception {
    ResourceRef ref = ClassResourceRef.create(TestDbStruct.class, "empty.dbStruct");
    DbStruct dbStruct = DbStructReader.read(ref);
    dbStruct.options.identification();
  }

}
