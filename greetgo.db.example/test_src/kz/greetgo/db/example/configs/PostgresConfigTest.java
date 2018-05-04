package kz.greetgo.db.example.configs;

import org.testng.annotations.Test;

public class PostgresConfigTest {
  @Test
  public void url() throws Exception {
    String url = PostgresConfig.url();
    System.out.println(url);
  }
}