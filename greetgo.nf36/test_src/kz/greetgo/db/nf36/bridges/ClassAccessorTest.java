package kz.greetgo.db.nf36.bridges;

import kz.greetgo.db.nf36.errors.CannotExtractFieldFromClass;
import kz.greetgo.util.RND;
import org.testng.annotations.Test;

import static kz.greetgo.db.nf36.bridges.ClassAccessor.extractGetterFieldName;
import static org.fest.assertions.api.Assertions.assertThat;

public class ClassAccessorTest {

  public class Test1 {
    public String field1;
    public int field2;
  }

  @Test
  public void extractValue_ok() {

    Test1 test = new Test1();
    test.field1 = RND.str(10);
    test.field2 = RND.plusInt(1_000_000_000);

    ClassAccessor classAccessor = new ClassAccessor(test.getClass());

    //
    //
    Object field1 = classAccessor.extractValue("field1", test);
    Object field2 = classAccessor.extractValue("field2", test);
    //
    //

    assertThat(field1).isEqualTo(test.field1);
    assertThat(field2).isEqualTo(test.field2);
  }

  @Test(expectedExceptions = CannotExtractFieldFromClass.class)
  public void extractValue_CannotExtractFieldFromClass() {
    Test1 test = new Test1();
    ClassAccessor classAccessor = new ClassAccessor(test.getClass());

    //
    //
    classAccessor.extractValue("Some_left_field_name", new Test1());
    //
    //
  }

  public class Test2 {
    public int field1;
    public boolean actual;

    public boolean calledGetField1 = false;

    @SuppressWarnings("unused")
    public int getField1() {
      calledGetField1 = true;
      return field1 + 11;
    }

    public boolean calledIsActual = false;

    @SuppressWarnings("unused")
    public boolean isActual() {
      calledIsActual = true;
      return !actual;
    }
  }

  @Test
  public void extractValue_usingGetter() {
    Test2 test = new Test2();
    test.field1 = 7000;

    ClassAccessor classAccessor = new ClassAccessor(test.getClass());

    assertThat(test.calledGetField1).isFalse();
    //
    //
    Object actualValue = classAccessor.extractValue("field1", test);
    //
    //
    assertThat(test.calledGetField1).isTrue();

    assertThat(actualValue).isEqualTo(7011);
  }

  @Test
  public void extractValue_usingBoolGetter() {
    Test2 test = new Test2();
    test.actual = RND.bool();

    ClassAccessor classAccessor = new ClassAccessor(test.getClass());

    assertThat(test.calledIsActual).isFalse();
    //
    //
    Object actualValue = classAccessor.extractValue("actual", test);
    //
    //
    assertThat(test.calledIsActual).isTrue();

    assertThat(actualValue).isEqualTo(!test.actual);
  }

  @Test
  public void isAbsent_true() {
    Test1 test = new Test1();

    ClassAccessor classAccessor = new ClassAccessor(test.getClass());

    //
    //
    assertThat(classAccessor.isAbsent("left")).isTrue();
    //
    //
  }

  @Test
  public void isAbsent_false() {
    Test1 test = new Test1();

    ClassAccessor classAccessor = new ClassAccessor(test.getClass());

    //
    //
    assertThat(classAccessor.isAbsent("field1")).isFalse();
    //
    //
  }

  @Test
  public void extractGetterFieldName_1() {
    assertThat(extractGetterFieldName("getField")).isEqualTo("field");
  }

  @Test
  public void extractGetterFieldName_2() {
    assertThat(extractGetterFieldName("getFieldName")).isEqualTo("fieldName");
  }

  @Test
  public void extractGetterFieldName_3() {
    assertThat(extractGetterFieldName("getter")).isNull();
  }

  @Test
  public void extractGetterFieldName_4() {
    assertThat(extractGetterFieldName("hello")).isNull();
  }

  @Test
  public void extractGetterFieldName_5() {
    assertThat(extractGetterFieldName("isActualWhileMoving")).isEqualTo("actualWhileMoving");
  }

  @Test
  public void extractGetterFieldName_6() {
    assertThat(extractGetterFieldName("is")).isNull();
  }

  @Test
  public void extractGetterFieldName_7() {
    assertThat(extractGetterFieldName("get")).isNull();
  }

  @Test
  public void extractGetterFieldName_8() {
    assertThat(extractGetterFieldName("getA")).isEqualTo("a");
  }

  @Test
  public void extractGetterFieldName_9() {
    assertThat(extractGetterFieldName("isA")).isEqualTo("a");
  }
}
