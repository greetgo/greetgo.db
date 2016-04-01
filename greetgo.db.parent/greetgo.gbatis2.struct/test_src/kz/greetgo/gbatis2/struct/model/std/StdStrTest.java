package kz.greetgo.gbatis2.struct.model.std;

import kz.greetgo.gbatis2.struct.model.SimpleEssence;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.fest.assertions.api.Assertions.assertThat;

public class StdStrTest {
  @Test
  public void isSequential() throws Exception {
    assertThat(new StdStr(10).isSequential()).isFalse();
  }

  @Test
  public void simpleEssenceList() throws Exception {
    StdStr stdStr = new StdStr(10);
    List<SimpleEssence> listOne = stdStr.keySimpleEssenceList();
    List<SimpleEssence> listTwo = stdStr.keySimpleEssenceList();

    assertThat(listOne).isEqualTo(listTwo);
    assertThat(listOne).hasSize(1);
    assertThat(listOne.get(0)).isEqualTo(stdStr);
  }

  @Test
  public void toStringTest() throws Exception {
    assertThat(new StdStr(113).toString()).isEqualTo("str113");
  }

  @Test
  public void visit() throws Exception {
    TestVisitor visitor = new TestVisitor();

    StdStr stdStr = new StdStr(113);
    stdStr.visit(visitor);

    assertThat(visitor.visits).containsExactly(stdStr);
  }

  @Test
  public void equalsTest() throws Exception {
    StdStr s1 = new StdStr(113);
    StdStr s2 = new StdStr(113);
    StdStr s3 = new StdStr(113 + 1);

    assertThat(s1.equals(s2)).isTrue();
    assertThat(s1.equals(s3)).isFalse();
  }

  @Test
  public void hashCodeTest() throws Exception {
    final Map<StdStr, Long> map = new HashMap<>();

    map.put(new StdStr(113), 100L);
    map.put(new StdStr(213), 200L);
    
    assertThat(map.get(new StdStr(113))).isNotNull();
    assertThat(map.get(new StdStr(213))).isNotNull();
    assertThat(map.get(new StdStr(313))).isNull();
  }
}
