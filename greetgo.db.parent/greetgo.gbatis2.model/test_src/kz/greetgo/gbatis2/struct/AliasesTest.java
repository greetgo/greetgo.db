package kz.greetgo.gbatis2.struct;

import kz.greetgo.gbatis2.struct.exceptions.AliasSetContainsCircling;
import org.testng.annotations.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.fail;

public class AliasesTest {
  private AliasDot alias(String alias, String target, String place) {
    return new AliasDot(alias, target, new TestStrPlace(place));
  }

  @Test
  public void real_throwAliasSetContainsCircling() {
    Aliases aliases = new Aliases();
    aliases.append(alias("alias1", "alias2", "place1"));
    aliases.append(alias("alias2", "alias3", "place2"));
    aliases.append(alias("alias3", "alias1", "place3"));

    AliasSetContainsCircling error = null;

    try {
      aliases.real("alias1");
      fail("Must be generated exception");
    } catch (AliasSetContainsCircling e) {
      error = e;
    }

    if (error == null) return;

    assertThat(error.aliasDots()).hasSize(3);

    assertThat(error.aliasDots().get(0).display()).isEqualTo("alias1 alias2 at place1");
    assertThat(error.aliasDots().get(1).display()).isEqualTo("alias2 alias3 at place2");
    assertThat(error.aliasDots().get(2).display()).isEqualTo("alias3 alias1 at place3");

    assertThat(error.getMessage()).isNotNull();
  }

  @Test
  public void real_replace() {
    Aliases aliases = new Aliases();
    aliases.append(alias("alias1", "alias2", "place1"));
    aliases.append(alias("alias2", "alias3", "place2"));

    assertThat(aliases.real("alias1")).isEqualTo("alias3");
  }
  
  @Test
  public void real_thought() {
    Aliases aliases = new Aliases();
    aliases.append(alias("alias1", "alias2", "place1"));
    aliases.append(alias("alias2", "alias3", "place2"));

    assertThat(aliases.real("asd")).isEqualTo("asd");
  }
}