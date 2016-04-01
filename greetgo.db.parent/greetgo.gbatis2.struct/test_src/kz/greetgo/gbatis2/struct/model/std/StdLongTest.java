package kz.greetgo.gbatis2.struct.model.std;

import kz.greetgo.gbatis2.struct.model.SimpleEssence;
import org.testng.annotations.Test;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class StdLongTest {
  @Test
  public void isSequential() throws Exception {
    assertThat(StdLong.INSTANCE.isSequential()).isTrue();
  }

  @Test
  public void simpleEssenceList() throws Exception {
    List<SimpleEssence> listOne = StdLong.INSTANCE.keySimpleEssenceList();
    List<SimpleEssence> listTwo = StdLong.INSTANCE.keySimpleEssenceList();

    assertThat(listOne).isEqualTo(listTwo);
    assertThat(listOne).hasSize(1);
    assertThat(listOne.get(0)).isEqualTo(StdLong.INSTANCE);
  }

  @Test
  public void toStringTest() throws Exception {
    assertThat(StdLong.INSTANCE.toString()).isEqualTo("long");
  }
  
  @Test
  public void visit() throws Exception {
    TestVisitor visitor = new TestVisitor();
    StdLong.INSTANCE.visit(visitor);
    assertThat(visitor.visits).containsExactly(StdLong.INSTANCE);
  }
}