package kz.greetgo.gbatis2.struct;

import kz.greetgo.gbatis2.struct.exceptions.OptionIsNotDefined;
import org.testng.annotations.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class OptionsTest {
  @Test
  public void read() throws Exception {

    Options options = new Options();

    options.parseLine("identification", "asd001", null);
    assertThat(options.identification()).isEqualTo("asd001");

    options.parseLine("tablePrefixId", "asd002", null);
    assertThat(options.tablePrefixId()).isEqualTo("asd002");

    options.parseLine("keyTableCreatedAt", "asd003", null);
    assertThat(options.keyTableCreatedAt()).isEqualTo("asd003");

    options.parseLine("keyTableCreatedBy", "asd004", null);
    assertThat(options.keyTableCreatedBy()).isEqualTo("asd004");

    options.parseLine("tablePrefixMemory", "asd005", null);
    assertThat(options.tablePrefixMemory()).isEqualTo("asd005");

    options.parseLine("tablePrefixOperative", "asd006", null);
    assertThat(options.tablePrefixOperative()).isEqualTo("asd006");

    options.parseLine("concatenationSuffix", "asd007", null);
    assertThat(options.concatenationSuffix()).isEqualTo("asd007");

    options.parseLine("sequencePrefix", "asd008", null);
    assertThat(options.sequencePrefix()).isEqualTo("asd008");

    options.parseLine("tsFieldName", "asd009", null);
    assertThat(options.tsFieldName()).isEqualTo("asd009");

  }

  @Test(expectedExceptions = OptionIsNotDefined.class)
  public void throwOptionIsNotDefined() throws Exception {

    Options options = new Options();

    options.identification();
  }
}