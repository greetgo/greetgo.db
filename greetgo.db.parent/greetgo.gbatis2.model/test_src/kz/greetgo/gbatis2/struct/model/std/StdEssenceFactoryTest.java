package kz.greetgo.gbatis2.struct.model.std;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class StdEssenceFactoryTest {

  private StdEssenceFactory stdFactory;

  @BeforeMethod
  public void setup() {
    stdFactory = new StdEssenceFactory();
  }

  @Test
  public void testInt() {
    assertThat(stdFactory.parse("int")).isInstanceOf(StdInt.class);
  }

  @Test
  public void testLong() {
    assertThat(stdFactory.parse("long")).isInstanceOf(StdLong.class);
  }

  @Test
  public void testFloat() {
    assertThat(stdFactory.parse("float")).isInstanceOf(StdFloat.class);
  }

  @Test
  public void testStr() {
    StdEssence essence = stdFactory.parse("str139");
    assertThat(essence).isInstanceOf(StdStr.class);
    StdStr stdStr = (StdStr) essence;
    assertThat(stdStr.length).isEqualTo(139);
  }

  @Test
  public void testNull() {
    assertThat(stdFactory.parse("asd")).isNull();
  }
}