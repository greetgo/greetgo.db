package kz.greetgo.gbatis2.struct.model;

import kz.greetgo.gbatis2.struct.model.std.StdInt;
import org.testng.annotations.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class KeyFieldTest {

  @Test
  public void toStringTest() throws Exception {
    assertThat(new KeyField(new DbField(true, "asd", StdInt.INSTANCE, null)).toString())
        .isEqualTo("key{asd int}");
  }

  @Test
  public void type() throws Exception {
    assertThat(
        new KeyField(new DbField(true, "asd", StdInt.INSTANCE, null)).type()
    ).isEqualTo(StdInt.INSTANCE);
  }

  @Test
  public void name() throws Exception {
    assertThat(
        new KeyField(new DbField(true, "asd", StdInt.INSTANCE, null)).name()
    ).isEqualTo("asd");
  }
}