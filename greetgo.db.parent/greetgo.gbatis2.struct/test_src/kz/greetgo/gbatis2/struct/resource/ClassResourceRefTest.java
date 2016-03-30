package kz.greetgo.gbatis2.struct.resource;

import kz.greetgo.gbatis2.struct.resource.subpackage.ClassResourceRefExample;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.fest.assertions.api.Assertions.assertThat;

public class ClassResourceRefTest {

  private static String streamToStr(InputStream in) {
    try {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      byte buffer[] = new byte[100];
      while (true) {
        int count = in.read(buffer);
        if (count < 0) return out.toString("UTF-8");
        out.write(buffer, 0, count);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void create() throws Exception {
    {
      InputStream in = ClassResourceRefExample.class.getResourceAsStream("res1.txt");
      assertThat(in).isNotNull();
    }

    ResourceRef res1 = ClassResourceRef.create(ClassResourceRefExample.class, "res1.txt");

    ResourceRef apple = res1.change("apple.txt");

    assertThat(res1).isNotNull();
    assertThat(apple).isNotNull();

    assertThat(streamToStr(res1.getInputStream())).isEqualTo("res1 contents");
    assertThat(streamToStr(apple.getInputStream())).isEqualTo("apple contents");

    ResourceRef res2 = res1.change("more/res2.txt");

    assertThat(streamToStr(res2.getInputStream())).isEqualTo("res2 contents");

    ResourceRef res3 = res2.change("../res3.txt");

    assertThat(streamToStr(res3.getInputStream())).isEqualTo("res3 contents");

    ResourceRef res4 = res1.change("../res4.txt");

    assertThat(streamToStr(res4.getInputStream())).isEqualTo("res4 contents");

    String fullName = "/" + ClassResourceRefExample.class.getPackage().getName().replace('.', '/') + "/res3.txt";
    ResourceRef res3More = res3.change(fullName);
    assertThat(streamToStr(res3More.getInputStream())).isEqualTo("res3 contents");

  }

  @Test
  public void testEquals() throws Exception {

    ResourceRef res2 = ClassResourceRef.create(ClassResourceRefExample.class, "more/res2.txt");

    ResourceRef res4_one = res2.change("../../res4.txt");

    ResourceRef res4_two = ClassResourceRef.create(ClassResourceRefExample.class, "../res4.txt");

    System.out.println(res4_one);
    System.out.println(res4_two);

    assertThat(res4_one.equals(res4_two)).isTrue();

  }

  @Test
  public void normalizePath_001() throws Exception {

    String path = ClassResourceRef.normalizePath("asd/wow/../more/file.txt");

    assertThat(path).isEqualTo("asd/more/file.txt");

  }

  @Test
  public void normalizePath_002() throws Exception {

    String path = ClassResourceRef.normalizePath("asd//file.txt");

    assertThat(path).isEqualTo("asd/file.txt");

  }

  @Test
  public void normalizePath_003() throws Exception {

    String path = ClassResourceRef.normalizePath("asd/./file.txt");

    assertThat(path).isEqualTo("asd/file.txt");

  }

  @Test
  public void normalizePath_004() throws Exception {

    String path = ClassResourceRef.normalizePath("asd///wow//../more/./luna/../file.txt");

    assertThat(path).isEqualTo("asd/more/file.txt");

  }

  @Test
  public void normalizePath_005() throws Exception {

    String path = ClassResourceRef.normalizePath("more/../../file.txt");

    assertThat(path).isEqualTo("../file.txt");

  }

}
