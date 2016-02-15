package kz.greetgo.gbatis.modelreader;

import static org.fest.assertions.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.Date;

import kz.greetgo.gbatis.model.Param;
import kz.greetgo.gbatis.model.Request;
import kz.greetgo.gbatis.model.RequestType;
import kz.greetgo.gbatis.model.ResultType;
import kz.greetgo.sqlmanager.gen.Conf;
import kz.greetgo.sqlmanager.parser.StruShaper;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ModelReaderTest {
  
  private Conf conf;
  
  private StruShaper sg;
  
  @BeforeMethod
  public void setup() throws Exception {
    conf = new Conf();
    conf.separator = ";;";
    conf.tabPrefix = "m_";
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
  
  private static final Class<?> testIface = RequestTestIface.class;
  
  @Test
  public void readWithList() throws Exception {
    
    Method method = testIface.getMethod("methodReadWithList", Long.TYPE);
    
    //
    //
    Request request = RequestGenerator.methodToRequest(method, sg.stru, conf);
    //
    //
    
    assertThat(request).isNotNull();
    assertThat(request.withList.get(0).table).isEqualTo("m_client");
    assertThat(request.withList.get(0).view).isEqualTo("hello");
    assertThat(request.withList.get(0).fields).containsExactly("surname", "name", "patronymic");
    assertThat(request.withList.get(1).table).isEqualTo("m_phone");
    assertThat(request.withList.get(1).view).isEqualTo("x_phone");
    assertThat(request.withList.get(1).fields).containsExactly("type", "actual");
    
  }
  
  @Test(expectedExceptions = RequestGeneratorException.class,
      expectedExceptionsMessageRegExp = "No sql for .*RequestTestIface\\.left\\(\\)")
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
  
  @Test
  public void resultType_list() throws Exception {
    
    Method method = findMethodWithName(testIface.getMethods(), "resultType_list");
    
    //
    //
    Request request = RequestGenerator.methodToRequest(method, sg.stru, conf);
    //
    //
    
    assertThat(request).isNotNull();
    
    assertThat(request.result.type).isEqualTo(ResultType.LIST);
    assertThat(request.result.resultDataClass + "").isEqualTo(RequestTestIface.Asd.class + "");
    assertThat(request.callNow).isTrue();
    assertThat(request.result.mapKeyField).isNull();
    assertThat(request.result.mapKeyClass).isNull();
  }
  
  @Test
  public void resultType_simple() throws Exception {
    
    Method method = findMethodWithName(testIface.getMethods(), "resultType_simple");
    
    //
    //
    Request request = RequestGenerator.methodToRequest(method, sg.stru, conf);
    //
    //
    
    assertThat(request).isNotNull();
    
    assertThat(request.result.type).isEqualTo(ResultType.SIMPLE);
    assertThat(request.result.resultDataClass + "").isEqualTo(RequestTestIface.Asd.class + "");
    assertThat(request.callNow).isTrue();
    assertThat(request.result.mapKeyField).isNull();
    assertThat(request.result.mapKeyClass).isNull();
  }
  
  @Test(expectedExceptions = RequestGeneratorException.class,
      expectedExceptionsMessageRegExp = "Result List without type arguments in .+")
  public void resultType_emptyList() throws Exception {
    
    Method method = findMethodWithName(testIface.getMethods(), "resultType_emptyList");
    
    //
    //
    RequestGenerator.methodToRequest(method, sg.stru, conf);
  }
  
  @Test(expectedExceptions = RequestGeneratorException.class,
      expectedExceptionsMessageRegExp = "Result Map without type arguments in .+")
  public void resultType_emptyMap() throws Exception {
    
    Method method = findMethodWithName(testIface.getMethods(), "resultType_emptyMap");
    
    //
    //
    RequestGenerator.methodToRequest(method, sg.stru, conf);
  }
  
  @Test
  public void resultType_noMapKey() throws Exception {
    
    Method method = findMethodWithName(testIface.getMethods(), "resultType_noMapKey");
    
    //
    //
    Request request = RequestGenerator.methodToRequest(method, sg.stru, conf);
    //
    //
    
    assertThat(request).isNotNull();
    
    assertThat(request).isNotNull();
    
    assertThat(request.result.type).isEqualTo(ResultType.MAP);
    assertThat(request.result.resultDataClass + "").isEqualTo(RequestTestIface.Asd.class + "");
    assertThat(request.callNow).isTrue();
    assertThat(request.result.mapKeyClass + "").isEqualTo(String.class + "");
    assertThat(request.result.mapKeyField).isNull();
    
  }
  
  @Test
  public void resultType_map() throws Exception {
    
    Method method = findMethodWithName(testIface.getMethods(), "resultType_map");
    
    //
    //
    Request request = RequestGenerator.methodToRequest(method, sg.stru, conf);
    //
    //
    
    assertThat(request).isNotNull();
    
    assertThat(request.result.type).isEqualTo(ResultType.MAP);
    assertThat(request.result.resultDataClass + "").isEqualTo(RequestTestIface.Asd.class + "");
    assertThat(request.callNow).isTrue();
    assertThat(request.result.mapKeyClass + "").isEqualTo(String.class + "");
    assertThat(request.result.mapKeyField).isEqualTo("mapKeyFieldName");
  }
  
  @Test
  public void resultType_mapList() throws Exception {
    
    Method method = findMethodWithName(testIface.getMethods(), "resultType_mapList");
    
    //
    //
    Request request = RequestGenerator.methodToRequest(method, sg.stru, conf);
    //
    //
    
    assertThat(request).isNotNull();
    
    assertThat(request.result.type).isEqualTo(ResultType.MAP_LIST);
    assertThat(request.result.resultDataClass + "").isEqualTo(RequestTestIface.Asd.class + "");
    assertThat(request.callNow).isTrue();
    assertThat(request.result.mapKeyClass + "").isEqualTo(String.class + "");
    assertThat(request.result.mapKeyField).isEqualTo("mapKeyFieldName");
  }
  
  @Test
  public void resultType_mapSet() throws Exception {
    
    Method method = findMethodWithName(testIface.getMethods(), "resultType_mapSet");
    
    //
    //
    Request request = RequestGenerator.methodToRequest(method, sg.stru, conf);
    //
    //
    
    assertThat(request).isNotNull();
    
    assertThat(request.result.type).isEqualTo(ResultType.MAP_SET);
    assertThat(request.result.resultDataClass + "").isEqualTo(RequestTestIface.Asd.class + "");
    assertThat(request.callNow).isTrue();
    assertThat(request.result.mapKeyClass + "").isEqualTo(String.class + "");
    assertThat(request.result.mapKeyField).isEqualTo("mapKeyFieldName");
  }
  
  @Test(expectedExceptions = RequestGeneratorException.class,
      expectedExceptionsMessageRegExp = "Result FutureCall without type argument in .+")
  public void resultType_emptyFutureCall() throws Exception {
    
    Method method = findMethodWithName(testIface.getMethods(), "resultType_emptyFutureCall");
    
    //
    //
    RequestGenerator.methodToRequest(method, sg.stru, conf);
  }
  
  @Test
  public void resultType_futureCall_simple() throws Exception {
    
    Method method = findMethodWithName(testIface.getMethods(), "resultType_futureCall_simple");
    
    //
    //
    Request request = RequestGenerator.methodToRequest(method, sg.stru, conf);
    //
    //
    
    assertThat(request).isNotNull();
    
    assertThat(request.result.type).isEqualTo(ResultType.SIMPLE);
    assertThat(request.result.resultDataClass + "").isEqualTo(RequestTestIface.Asd.class + "");
    assertThat(request.callNow).isFalse();
    assertThat(request.result.mapKeyClass).isNull();
    assertThat(request.result.mapKeyField).isNull();
  }
  
  @Test(expectedExceptions = RequestGeneratorException.class,
      expectedExceptionsMessageRegExp = "Result List in FutureCall without type arguments in .+")
  public void resultType_futureCall_emptyList() throws Exception {
    
    Method method = findMethodWithName(testIface.getMethods(), "resultType_futureCall_emptyList");
    
    //
    //
    RequestGenerator.methodToRequest(method, sg.stru, conf);
  }
  
  @Test
  public void resultType_futureCall_list() throws Exception {
    
    Method method = findMethodWithName(testIface.getMethods(), "resultType_futureCall_list");
    
    //
    //
    Request request = RequestGenerator.methodToRequest(method, sg.stru, conf);
    //
    //
    
    assertThat(request).isNotNull();
    
    assertThat(request.result.type).isEqualTo(ResultType.LIST);
    assertThat(request.result.resultDataClass + "").isEqualTo(RequestTestIface.Asd.class + "");
    assertThat(request.callNow).isFalse();
    assertThat(request.result.mapKeyField).isNull();
    assertThat(request.result.mapKeyClass).isNull();
  }
  
  @Test(expectedExceptions = RequestGeneratorException.class,
      expectedExceptionsMessageRegExp = "Result FutureCall Map without type arguments in .+")
  public void resultType_futureCall_emptyMap() throws Exception {
    
    Method method = findMethodWithName(testIface.getMethods(), "resultType_futureCall_emptyMap");
    
    //
    //
    RequestGenerator.methodToRequest(method, sg.stru, conf);
  }
  
  @Test(expectedExceptions = RequestGeneratorException.class,
      expectedExceptionsMessageRegExp = "No MapKey in .+")
  public void resultType_futureCall_noMapKey() throws Exception {
    
    Method method = findMethodWithName(testIface.getMethods(), "resultType_futureCall_noMapKey");
    
    //
    //
    RequestGenerator.methodToRequest(method, sg.stru, conf);
  }
  
  @Test
  public void resultType_futureCall_map() throws Exception {
    
    Method method = findMethodWithName(testIface.getMethods(), "resultType_futureCall_map");
    
    //
    //
    Request request = RequestGenerator.methodToRequest(method, sg.stru, conf);
    //
    //
    
    assertThat(request).isNotNull();
    
    assertThat(request.result.type).isEqualTo(ResultType.MAP);
    assertThat(request.result.resultDataClass + "").isEqualTo(RequestTestIface.Asd.class + "");
    assertThat(request.callNow).isFalse();
    assertThat(request.result.mapKeyClass + "").isEqualTo(String.class + "");
    assertThat(request.result.mapKeyField).isEqualTo("mapKeyFieldName");
  }
  
  @Test
  public void resultType_futureCall_mapList() throws Exception {
    
    Method method = findMethodWithName(testIface.getMethods(), "resultType_futureCall_mapList");
    
    //
    //
    Request request = RequestGenerator.methodToRequest(method, sg.stru, conf);
    //
    //
    
    assertThat(request).isNotNull();
    
    assertThat(request.result.type).isEqualTo(ResultType.MAP_LIST);
    assertThat(request.result.resultDataClass + "").isEqualTo(RequestTestIface.Asd.class + "");
    assertThat(request.callNow).isFalse();
    assertThat(request.result.mapKeyClass + "").isEqualTo(String.class + "");
    assertThat(request.result.mapKeyField).isEqualTo("mapKeyFieldName");
  }
  
  @Test
  public void resultType_futureCall_mapSet() throws Exception {
    
    Method method = findMethodWithName(testIface.getMethods(), "resultType_futureCall_mapSet");
    
    //
    //
    Request request = RequestGenerator.methodToRequest(method, sg.stru, conf);
    //
    //
    
    assertThat(request).isNotNull();
    
    assertThat(request.result.type).isEqualTo(ResultType.MAP_SET);
    assertThat(request.result.resultDataClass + "").isEqualTo(RequestTestIface.Asd.class + "");
    assertThat(request.callNow).isFalse();
    assertThat(request.result.mapKeyClass + "").isEqualTo(String.class + "");
    assertThat(request.result.mapKeyField).isEqualTo("mapKeyFieldName");
  }
  
  @Test
  public void voidModi() throws Exception {
    
    Method method = findMethodWithName(testIface.getMethods(), "voidModi");
    
    //
    //
    Request request = RequestGenerator.methodToRequest(method, sg.stru, conf);
    //
    //
    
    assertThat(request).isNotNull();
    
    assertThat(request.type).isEqualTo(RequestType.Modi);
    assertThat(request.result.type).isEqualTo(ResultType.SIMPLE);
    assertThat(request.result.resultDataClass + "").isEqualTo(Void.TYPE + "");
    assertThat(request.callNow).isTrue();
    assertThat(request.result.mapKeyClass).isNull();
    assertThat(request.result.mapKeyField).isNull();
    assertThat(request.sql).isEqualTo("modi sql text");
  }
  
  @Test
  public void resultType_futureCall_set() throws Exception {
    
    Method method = findMethodWithName(testIface.getMethods(), "resultType_futureCall_set");
    
    //
    //
    Request request = RequestGenerator.methodToRequest(method, sg.stru, conf);
    //
    //
    
    assertThat(request).isNotNull();
    
    assertThat(request.result.type).isEqualTo(ResultType.SET);
    assertThat(request.result.resultDataClass + "").isEqualTo(RequestTestIface.Asd.class + "");
    assertThat(request.callNow).isFalse();
    assertThat(request.result.mapKeyField).isNull();
    assertThat(request.result.mapKeyClass).isNull();
  }
  
  @Test
  public void resultType_set() throws Exception {
    
    Method method = findMethodWithName(testIface.getMethods(), "resultType_set");
    
    //
    //
    Request request = RequestGenerator.methodToRequest(method, sg.stru, conf);
    //
    //
    
    assertThat(request).isNotNull();
    
    assertThat(request.result.type).isEqualTo(ResultType.SET);
    assertThat(request.result.resultDataClass + "").isEqualTo(RequestTestIface.Asd.class + "");
    assertThat(request.callNow).isTrue();
    assertThat(request.result.mapKeyField).isNull();
    assertThat(request.result.mapKeyClass).isNull();
  }
}
