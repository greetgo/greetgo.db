package kz.greetgo.gbatis.modelreader;

import static org.fest.assertions.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.Date;

import kz.greetgo.gbatis.model.Param;
import kz.greetgo.gbatis.model.Request;
import kz.greetgo.gbatis.model.RequestType;
import kz.greetgo.sqlmanager.gen.Conf;
import kz.greetgo.sqlmanager.parser.StruShaper;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ModelReaderXmlTest {
  
  private Conf conf;
  
  private StruShaper sg;
  
  @BeforeMethod
  public void setup() throws Exception {
    conf = new Conf();
    conf.separator = ";;";
    conf.mPrefix = "m_";
    conf.seqPrefix = "s_";
    conf.vPrefix = "v_";
    conf.withPrefix = "x_";
    conf.ts = "ts";
    conf.cre = "createdAt";
    conf.bigQuote = "big_quote";
    conf._ins_ = "ins_";
    conf._p_ = "p_";
    conf._value_ = "__value__";
    conf.daoSuffix = "Dao";
    
    URL url = getClass().getResource("stru.nf3");
    sg = new StruShaper();
    sg.printPStru = false;
    sg.parse(url);
  }
  
  private static final Class<?> testIface = RequestTestIfaceXml.class;
  
  @Test
  public void readWithList() throws Exception {
    
    Method method = testIface.getMethod("methodReadWithList", Long.TYPE);
    
    //
    //
    Request request = RequestGenerator.methodToRequest(method, sg.stru, conf);
    //
    //
    
    assertThat(request).isNotNull();
    
    assertThat(request.withList.get(0).table).isEqualTo("m_phone");
    assertThat(request.withList.get(0).view).isEqualTo("x_phone");
    assertThat(request.withList.get(0).fields).containsExactly("type", "actual");
    
    assertThat(request.withList.get(1).table).isEqualTo("m_client");
    assertThat(request.withList.get(1).view).isEqualTo("hello");
    assertThat(request.withList.get(1).fields).containsExactly("surname", "name", "patronymic");
    
  }
  
  @Test(expectedExceptions = RequestGeneratorException.class,
      expectedExceptionsMessageRegExp = "No sql for .*")
  public void noSql() throws Exception {
    
    Method method = testIface.getMethod("left");
    
    //
    //
    RequestGenerator.methodToRequest(method, sg.stru, conf);
    //
    //
  }
  
  @Test
  public void readSql1() throws Exception {
    
    Method method = testIface.getMethod("methodReadSql1");
    
    //
    //
    Request request = RequestGenerator.methodToRequest(method, sg.stru, conf);
    //
    //
    
    assertThat(request).isNotNull();
    
    assertThat(request.sql).isEqualTo("select name, value from x_asd where asd = #{asd}");
    assertThat(request.type).isEqualTo(RequestType.Sele);
  }
  
  @Test
  public void readSql2() throws Exception {
    
    Method method = testIface.getMethod("methodReadSql2");
    
    //
    //
    Request request = RequestGenerator.methodToRequest(method, sg.stru, conf);
    //
    //
    
    assertThat(request).isNotNull();
    
    assertThat(request.sql).isEqualTo("insert asd dsfsadfsaf");
    assertThat(request.type).isEqualTo(RequestType.Call);
  }
  
  @Test
  public void params() throws Exception {
    
    Method method = findMethodWithName(testIface.getMethods(), "methodParams");
    
    //
    //
    Request request = RequestGenerator.methodToRequest(method, sg.stru, conf);
    //
    //
    
    assertThat(request).isNotNull();
    
    for (Param param : request.paramList) {
      System.out.println(param);
    }
    
    assertThat(request.paramList).hasSize(3);
    
    assertThat(request.paramList.get(0).name).isEqualTo("asd");
    assertThat(request.paramList.get(0).type + "").isEqualTo(Long.TYPE + "");
    
    assertThat(request.paramList.get(1).name).isEqualTo("dsa");
    assertThat(request.paramList.get(1).type + "").isEqualTo(Long.class + "");
    
    assertThat(request.paramList.get(2).name).isEqualTo("inDate");
    assertThat(request.paramList.get(2).type + "").isEqualTo(Date.class + "");
  }
  
  private Method findMethodWithName(Method[] methods, String methodName) {
    for (Method method : methods) {
      if (methodName.equals(method.getName())) return method;
    }
    throw new IllegalArgumentException("No method with name = " + methodName);
  }
}
