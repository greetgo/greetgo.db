package kz.greetgo.gbatis2.struct.model.std;

import kz.greetgo.gbatis2.struct.model.SimpleEssence;
import org.testng.annotations.Test;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class StdTimeTest {
  @Test
  public void isSequential() throws Exception {
    assertThat(StdTime.INSTANCE.isSequential()).isFalse();
  }

  @Test
  public void simpleEssenceList() throws Exception {
    List<SimpleEssence> listOne = StdTime.INSTANCE.keySimpleEssenceList();
    List<SimpleEssence> listTwo = StdTime.INSTANCE.keySimpleEssenceList();

    assertThat(listOne).isEqualTo(listTwo);
    assertThat(listOne).hasSize(1);
    assertThat(listOne.get(0)).isEqualTo(StdTime.INSTANCE);
  }

  @Test
  public void toStringTest() throws Exception {
    assertThat(StdTime.INSTANCE.toString()).isEqualTo("time");
  }

  @Test
  public void visit() throws Exception {
    TestVisitor visitor = new TestVisitor();
    StdTime.INSTANCE.visit(visitor);
    assertThat(visitor.visits).containsExactly(StdTime.INSTANCE);
  }
}
