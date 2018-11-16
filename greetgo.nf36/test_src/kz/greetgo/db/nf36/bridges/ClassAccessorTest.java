package kz.greetgo.db.nf36.bridges;

import kz.greetgo.db.nf36.errors.CannotExtractFieldFromClass;
import kz.greetgo.db.nf36.errors.CannotSetFieldToClass;
import kz.greetgo.util.RND;
import org.testng.annotations.Test;

import static kz.greetgo.db.nf36.bridges.ClassAccessor.extractAcceptorFieldName;
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
    assertThat(extractAcceptorFieldName("getField", "is", "get")).isEqualTo("field");
  }

  @Test
  public void extractGetterFieldName_2() {
    assertThat(extractAcceptorFieldName("getFieldName", "is", "get")).isEqualTo("fieldName");
  }

  @Test
  public void extractGetterFieldName_3() {
    assertThat(extractAcceptorFieldName("getter", "is", "get")).isNull();
  }

  @Test
  public void extractGetterFieldName_4() {
    assertThat(extractAcceptorFieldName("hello", "is", "get")).isNull();
  }

  @Test
  public void extractGetterFieldName_5() {
    assertThat(extractAcceptorFieldName("isActualWhileMoving", "is", "get")).isEqualTo("actualWhileMoving");
  }

  @Test
  public void extractGetterFieldName_6() {
    assertThat(extractAcceptorFieldName("is", "is", "get")).isNull();
  }

  @Test
  public void extractGetterFieldName_7() {
    assertThat(extractAcceptorFieldName("get", "is", "get")).isNull();
  }

  @Test
  public void extractGetterFieldName_8() {
    assertThat(extractAcceptorFieldName("getA", "is", "get")).isEqualTo("a");
  }

  @Test
  public void extractGetterFieldName_9() {
    assertThat(extractAcceptorFieldName("isA", "is", "get")).isEqualTo("a");
  }

  public static class Test3 {
    public String superTopName;
  }

  @Test
  public void extractValue__camel_case_to_underscored() {
    Test3 test = new Test3();
    test.superTopName = RND.str(10);

    ClassAccessor classAccessor = new ClassAccessor(test.getClass());

    assertThat(classAccessor.extractValue("super_top_name", test)).isEqualTo(test.superTopName);
  }

  @Test
  public void isAbsent_FALSE__camel_case_to_underscored() {
    Test3 test = new Test3();

    ClassAccessor classAccessor = new ClassAccessor(test.getClass());

    assertThat(classAccessor.isAbsent("super_top_name")).isFalse();
  }

  public static class Test4 {
    public String superTopName;
    public String super_top_name;
  }

  @Test
  public void extractValue__camel_case_with_underscored() {
    Test4 test = new Test4();
    test.superTopName = RND.str(10);
    test.super_top_name = RND.str(10);

    ClassAccessor classAccessor = new ClassAccessor(test.getClass());

    assertThat(classAccessor.extractValue("super_top_name", test)).isEqualTo(test.super_top_name);
  }

  @Test
  public void isAbsent_FALSE__camel_case_with_underscored() {
    Test4 test = new Test4();

    ClassAccessor classAccessor = new ClassAccessor(test.getClass());

    assertThat(classAccessor.isAbsent("super_top_name")).isFalse();
  }

  public static class Test5 {
    String asd;

    @SuppressWarnings("unused")
    public String getSuperTopName() {
      return "{[(" + asd + ")]}";
    }
  }

  @Test
  public void extractValue_fromGetter_usingUnderscored() {
    Test5 test = new Test5();
    test.asd = RND.str(10);

    ClassAccessor classAccessor = new ClassAccessor(test.getClass());

    assertThat(classAccessor.extractValue("super_top_name", test)).isEqualTo("{[(" + test.asd + ")]}");
  }

  @Test
  public void isAbsent_fromGetter_usingUnderscored() {
    Test5 test = new Test5();

    ClassAccessor classAccessor = new ClassAccessor(test.getClass());

    assertThat(classAccessor.isAbsent("super_top_name")).isFalse();
  }

  public class SetTest1 {
    public String field1;
    public int field2;
  }

  @Test
  public void setValue() {
    SetTest1 test = new SetTest1();

    ClassAccessor classAccessor = new ClassAccessor(test.getClass());

    //
    //
    classAccessor.setValue(test, "field1", "Привет мир");
    classAccessor.setValue(test, "field2", 765);
    //
    //

    assertThat(test.field1).isEqualTo("Привет мир");
    assertThat(test.field2).isEqualTo(765);
  }

  @Test
  public void hasSetter() {
    SetTest1 test = new SetTest1();

    ClassAccessor classAccessor = new ClassAccessor(test.getClass());

    //
    //
    boolean field1 = classAccessor.hasSetter("field1");
    boolean fieldX = classAccessor.hasSetter("fieldX");
    //
    //

    assertThat(field1).isTrue();
    assertThat(fieldX).isFalse();
  }

  @Test(expectedExceptions = CannotSetFieldToClass.class)
  public void setValue_CannotSetFieldToClass() {
    SetTest1 test = new SetTest1();

    ClassAccessor classAccessor = new ClassAccessor(test.getClass());

    classAccessor.setValue(test, "hello_world", null);
  }

  public class SetTest2 {
    public String superFieldOne;
    public int good_field_two;
  }

  @Test
  public void setValue_CamelCase() {
    SetTest2 test = new SetTest2();

    ClassAccessor classAccessor = new ClassAccessor(test.getClass());

    //
    //
    classAccessor.setValue(test, "super_field_one", "Привет мир!");
    classAccessor.setValue(test, "good_field_two", 7615);
    //
    //

    assertThat(test.superFieldOne).isEqualTo("Привет мир!");
    assertThat(test.good_field_two).isEqualTo(7615);
  }

  @Test
  public void hasSetter_CamelCase() {
    SetTest2 test = new SetTest2();

    ClassAccessor classAccessor = new ClassAccessor(test.getClass());

    //
    //
    boolean superFieldOne = classAccessor.hasSetter("super_field_one");
    boolean goodFieldTwo = classAccessor.hasSetter("good_field_two");
    boolean leftName = classAccessor.hasSetter("leftName");
    //
    //

    assertThat(superFieldOne).isTrue();
    assertThat(goodFieldTwo).isTrue();
    assertThat(leftName).isFalse();
  }
}
