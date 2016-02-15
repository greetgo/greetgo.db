package kz.greetgo.sqlmanager.gen;

import java.net.URL;

import kz.greetgo.sqlmanager.model.Table;
import kz.greetgo.sqlmanager.parser.StruShaper;

import org.testng.annotations.Test;

public class ViewFormerRowNumberTest {
  @Test
  public void formTableSelect() throws Exception {
    URL url = getClass().getResource("example.nf3");
    StruShaper sg = new StruShaper();
    sg.printPStru = false;
    sg.parse(url);
    
    ViewFormerRowNumber vf = new ViewFormerRowNumber(new Conf());
    
    Table table = sg.stru.tables.get("pkbResultParamAnn");
    
    StringBuilder sb = new StringBuilder();
    vf.formTableSelect(sb, table, null, 2, 0);
    
    System.out.println(sb);
  }
  
  @Test
  public void formFieldSelect() throws Exception {
    URL url = getClass().getResource("example.nf3");
    StruShaper sg = new StruShaper();
    sg.printPStru = false;
    sg.parse(url);
    
    ViewFormerRowNumber vf = new ViewFormerRowNumber(new Conf());
    
    Table table = sg.stru.tables.get("pkbResultParamAnn");
    
    StringBuilder sb = new StringBuilder();
    vf.formFieldSelect(sb, table.fields.get(0), "TTS.TS", 2, 1);
    
    System.out.println(sb);
  }
}
