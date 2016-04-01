package kz.greetgo.gbatis2.struct.model.std;

import kz.greetgo.gbatis2.struct.model.SimpleEssence;
import org.testng.annotations.Test;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class StdTextTest {
  @Test
  public void isSequential() throws Exception {
    assertThat(StdText.INSTANCE.isSequential()).isFalse();
  }

  @Test
  public void simpleEssenceList() throws Exception {
    List<SimpleEssence> listOne = StdText.INSTANCE.keySimpleEssenceList();
    List<SimpleEssence> listTwo = StdText.INSTANCE.keySimpleEssenceList();

    assertThat(listOne).isEqualTo(listTwo);
    assertThat(listOne).hasSize(1);
    assertThat(listOne.get(0)).isEqualTo(StdText.INSTANCE);
  }

  @Test
  public void toStringTest() throws Exception {
    assertThat(StdText.INSTANCE.toString()).isEqualTo("text");
  }

  @Test
  public void visit() throws Exception {
    TestVisitor visitor = new TestVisitor();
    StdText.INSTANCE.visit(visitor);
    assertThat(visitor.visits).containsExactly(StdText.INSTANCE);
  }
}
