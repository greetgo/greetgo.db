package kz.greetgo.gbatis2.struct.model.std;

import kz.greetgo.gbatis2.struct.model.SimpleEssence;
import org.testng.annotations.Test;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class StdIntTest {
  @Test
  public void isSequential() throws Exception {
    assertThat(StdInt.INSTANCE.isSequential()).isTrue();
  }

  @Test
  public void simpleEssenceList() throws Exception {
    List<SimpleEssence> listOne = StdInt.INSTANCE.keySimpleEssenceList();
    List<SimpleEssence> listTwo = StdInt.INSTANCE.keySimpleEssenceList();

    assertThat(listOne).isEqualTo(listTwo);
    assertThat(listOne).hasSize(1);
    assertThat(listOne.get(0)).isEqualTo(StdInt.INSTANCE);
  }

  @Test
  public void toStringTest() throws Exception {
    assertThat(StdInt.INSTANCE.toString()).isEqualTo("int");
  }
  
  @Test
  public void visit() throws Exception {
    TestVisitor visitor = new TestVisitor();
    StdInt.INSTANCE.visit(visitor);
    assertThat(visitor.visits).containsExactly(StdInt.INSTANCE);
  }
  
}