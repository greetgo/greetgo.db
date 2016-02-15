package kz.greetgo.sqlmanager.gen;

import java.net.URL;

import kz.greetgo.sqlmanager.parser.StruShaper;

import org.testng.annotations.Test;

public class ParseTest {
  @Test
  public void parse() throws Exception {
    URL url = getClass().getResource("example.nf3");
    StruShaper sg = new StruShaper();
    sg.printPStru = false;
    sg.parse(url);
  }
}
