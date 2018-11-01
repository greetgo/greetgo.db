package kz.greetgo.db.nf36.bridges;

import kz.greetgo.db.nf36.errors.CannotExtractFieldFromClass;
import kz.greetgo.util.RND;
import org.testng.annotations.Test;

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

    @SuppressWarnings("unused")
    public int getField1() {
      return field1 + 11;
    }
  }

  @Test
  public void extractValue_usingGetter() {
    Test2 test = new Test2();
    test.field1 = 7000;

    ClassAccessor classAccessor = new ClassAccessor(test.getClass());

    //
    //
    Object actualValue = classAccessor.extractValue("field1", test);
    //
    //

    assertThat(actualValue).isEqualTo(7011);
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
}