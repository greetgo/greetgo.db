package kz.greetgo.sqlmanager.parser2;

import java.net.URL;

import kz.greetgo.sqlmanager.model.Field;
import kz.greetgo.sqlmanager.model.Table;
import kz.greetgo.sqlmanager.model.command.Command;
import kz.greetgo.sqlmanager.parser.StruShaper;

import org.testng.annotations.Test;

public class StruShaperTest {
  @Test
  public void test() throws Exception {
    URL url = getClass().getResource("example.nf3");
    StruShaper sg = new StruShaper();
    sg.printPStru = false;
    sg.parse(url);
    
    for (Table t : sg.stru.tables.values()) {
      System.out.println(t.name);
      for (Command command : t.commands) {
        System.out.println("command " + command);
      }
      for (Field field : t.keys) {
        print("*", field);
      }
      for (Field field : t.fields) {
        print(" ", field);
      }
      System.out.println();
    }
    
  }
  
  private void print(String pre, Field field) {
    System.out.println("  " + pre + field.name + ' ' + field.type);
  }
}
