package kz.greetgo.gbatis2.struct.model.std;

import kz.greetgo.gbatis2.struct.model.SimpleEssence;
import org.testng.annotations.Test;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class StdFloatTest {
  @Test
  public void isSequential() throws Exception {
    assertThat(StdFloat.INSTANCE.isSequential()).isFalse();
  }

  @Test
  public void simpleEssenceList() throws Exception {
    List<SimpleEssence> list1 = StdFloat.INSTANCE.keySimpleEssenceList();
    List<SimpleEssence> list2 = StdFloat.INSTANCE.keySimpleEssenceList();

    assertThat(list1).isEqualTo(list2);
    assertThat(list1).hasSize(1);
    assertThat(list1.get(0)).isEqualTo(StdFloat.INSTANCE);
  }

  @Test
  public void toStringTest() throws Exception {
    assertThat(StdFloat.INSTANCE.toString()).isEqualTo("float");
  }

  @Test
  public void visit() throws Exception {
    TestVisitor visitor = new TestVisitor();
    StdFloat.INSTANCE.visit(visitor);
    assertThat(visitor.visits).containsExactly(StdFloat.INSTANCE);
  }
}