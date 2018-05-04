package kz.greetgo.gbatis.gen.gbatis;

import java.net.URL;

import kz.greetgo.sqlmanager.gen.Conf;
import kz.greetgo.sqlmanager.gen.UserIdFieldType;
import kz.greetgo.sqlmanager.parser.StruShaper;

public class ZNF3 {
  private static StruShaper struGen = null;
  
  private static final Conf conf;
  static {
    conf = new Conf();
    conf.genOperTables = true;
    conf.oracleInsertDupValues = true;
    conf.userIdFieldType = UserIdFieldType.Long;
  }
  
  public static StruShaper struGen() throws Exception {
    if (struGen == null) struGen = createStruGen();
    return struGen;
  }
  
  private static StruShaper createStruGen() throws Exception {
    URL url = ZNF3.class.getResource("structura.nf3");
    StruShaper sg = new StruShaper();
    sg.printPStru = false;
    sg.parse(url);
    return sg;
  }
  
  public static Conf getConf() {
    return conf;
  }
}
