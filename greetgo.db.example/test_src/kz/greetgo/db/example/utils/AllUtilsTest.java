package kz.greetgo.db.example.utils;

import org.testng.annotations.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class AllUtilsTest {
  @Test
  public void replaceDbName() throws Exception {
    String actual = AllUtils.replaceDbName("xxx/asd/dsa", "wow");
    assertThat(actual).isEqualTo("xxx/asd/wow");
  }
}