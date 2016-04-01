package kz.greetgo.gbatis2.struct.model;

import kz.greetgo.gbatis2.struct.model.std.TestVisitor;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.fest.assertions.api.Assertions.assertThat;

public class EnumEssenceTest {

  @Test
  public void isSequential() throws Exception {
    assertThat(new EnumEssence("asd", null).isSequential()).isFalse();
  }

  @Test
  public void toStringTest() throws Exception {
    assertThat(new EnumEssence("asd", null).toString()).isEqualTo("ENUM asd");
  }

  @Test
  public void visit() throws Exception {

    TestVisitor visitor = new TestVisitor();

    EnumEssence asd = new EnumEssence("asd", null);

    asd.visit(visitor);

    assertThat(visitor.visits).containsExactly(asd);

  }

  @Test
  public void simpleEssenceList() throws Exception {
    EnumEssence element = new EnumEssence("asd", null);
    List<SimpleEssence> listOne = element.keySimpleEssenceList();
    List<SimpleEssence> listTwo = element.keySimpleEssenceList();

    assertThat(listOne).isEqualTo(listTwo);
    assertThat(listOne).hasSize(1);
    assertThat(listOne.get(0)).isEqualTo(element);
  }


  @Test
  public void equalsTest() throws Exception {
    EnumEssence s1 = new EnumEssence("asd", null);
    EnumEssence s2 = new EnumEssence("asd", null);
    EnumEssence s3 = new EnumEssence("asd1", null);

    assertThat(s1.equals(s2)).isTrue();
    assertThat(s1.equals(s3)).isFalse();
  }

  @Test
  public void hashCodeTest() throws Exception {
    final Map<EnumEssence, Long> map = new HashMap<>();

    map.put(new EnumEssence("asd1", null), 100L);
    map.put(new EnumEssence("asd2", null), 200L);

    assertThat(map.get(new EnumEssence("asd1", null))).isNotNull();
    assertThat(map.get(new EnumEssence("asd2", null))).isNotNull();
    assertThat(map.get(new EnumEssence("asd3", null))).isNull();
  }

  enum TestEnum {}

  @Test
  public void enumClass() throws Exception {

    EnumEssence ee = new EnumEssence(TestEnum.class.getName(), null);

    assertThat((Object) ee.enumClass()).isEqualTo(TestEnum.class);
    assertThat((Object) ee.enumClass()).isEqualTo(TestEnum.class);

  }
}