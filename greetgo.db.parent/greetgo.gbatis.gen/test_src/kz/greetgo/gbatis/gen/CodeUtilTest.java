package kz.greetgo.gbatis.gen;

import org.testng.annotations.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.fest.assertions.api.Assertions.assertThat;

public class CodeUtilTest {

  @Test
  public void strToCode_null() throws Exception {
    String code = CodeUtil.strValueToCode(null);
    assertThat(code).isEqualTo("null");
  }

  @Test
  public void strToCode_str() throws Exception {
    String code = CodeUtil.strValueToCode("asd1\nasd2\rasd3\"''\"@~wow");
    assertThat(code).isEqualTo("\"asd1\\nasd2\\rasd3\\\"''\\\"@~wow\"");
  }

  @Test
  public void enumToCode_null() throws Exception {
    final String code = CodeUtil.enumValueToCode(TestEnum.class, null);
    assertThat(code).isEqualTo("null");
  }

  private enum TestEnum {VALUE_1}

  @Test
  public void enumToCode_enum() throws Exception {
    final String code = CodeUtil.enumValueToCode(TestEnum.class, TestEnum.VALUE_1);
    assertThat(code).isEqualTo("kz.greetgo.gbatis.gen.CodeUtilTest.TestEnum.VALUE_1");
  }

  private static class TestClass {
    public List<Map<String, Set<TestClass>>> list;
  }

  @Test
  public void classToCode_class() throws Exception {
    final String code = CodeUtil.classValueToCode(TestClass.class);
    assertThat(code).isEqualTo("kz.greetgo.gbatis.gen.CodeUtilTest.TestClass.class");
  }

  @Test
  public void classToCode_null() throws Exception {
    final String code = CodeUtil.classValueToCode(null);
    assertThat(code).isEqualTo("null");
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void typeToCode_null() throws Exception {
    TestClass tc = new TestClass();
    tc.list = new ArrayList<>();
    assertThat(tc.list).isNotNull();

    CodeUtil.typeToCode(null);
  }

  @Test
  public void typeToCode_class() throws Exception {
    final String code = CodeUtil.typeToCode(String.class);

    System.out.println(code);

    assertThat(code).isEqualTo("java.lang.String");
  }

  @Test
  public void typeToCode_type() throws Exception {
    final Type listType = TestClass.class.getField("list").getGenericType();
    final String code = CodeUtil.typeToCode(listType);

    System.out.println(code);

    assertThat(code).isEqualTo("java.util.List<java.util.Map<java.lang.String, " +
      "java.util.Set<kz.greetgo.gbatis.gen.CodeUtilTest.TestClass>>>");
  }


}