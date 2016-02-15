package kz.greetgo.gbatis.modelreader;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kz.greetgo.gbatis.model.FutureCall;
import kz.greetgo.gbatis.probes.asd.ClassNameValue;
import kz.greetgo.gbatis.t.Call;
import kz.greetgo.gbatis.t.MapKey;
import kz.greetgo.gbatis.t.Modi;
import kz.greetgo.gbatis.t.Prm;
import kz.greetgo.gbatis.t.Sele;
import kz.greetgo.gbatis.t.T1;
import kz.greetgo.gbatis.t.T2;

public interface RequestTestIface {
  @T2(value = "m_phone")
  @T1(value = "m_client", fields = "surname, name, patronymic", name = "hello")
  @Sele("select name, value from x_asd where asd = #{asd}")
  FutureCall<ClassNameValue> methodReadWithList(@Prm("asd") long asd);
  
  @Sele("select name, value from x_asd where asd = #{asd}")
  FutureCall<ClassNameValue> methodReadSql1();
  
  void left();
  
  @Call("insert asd dsfsadfsaf")
  void methodReadSql2();
  
  void manyParams(@Prm("longParam") long longParam);
  
  @Sele("select name, value from x_asd where asd = #{asd}")
  FutureCall<ClassNameValue> methodParams(@Prm("asd") long asd, @Prm("dsa") Long dsa,
      @Prm("inDate") Date inDate);
  
  class Asd {}
  
  @Sele("sele")
  List<Asd> resultType_list();
  
  @Sele("sele")
  Asd resultType_simple();
  
  @Sele("sele")
  @SuppressWarnings("rawtypes")
  List resultType_emptyList();
  
  @Sele("sele")
  @SuppressWarnings("rawtypes")
  Map resultType_emptyMap();
  
  @Sele("sele")
  Map<String, Asd> resultType_noMapKey();
  
  @Sele("sele")
  @MapKey("mapKeyFieldName")
  Map<String, Asd> resultType_map();
  
  @Sele("sele")
  @MapKey("mapKeyFieldName")
  Map<String, List<Asd>> resultType_mapList();
  
  @Sele("sele")
  @MapKey("mapKeyFieldName")
  Map<String, Set<Asd>> resultType_mapSet();
  
  @Sele("sele")
  @SuppressWarnings("rawtypes")
  FutureCall resultType_emptyFutureCall();
  
  @Sele("sele")
  FutureCall<Asd> resultType_futureCall_simple();
  
  @Sele("sele")
  @SuppressWarnings("rawtypes")
  FutureCall<List> resultType_futureCall_emptyList();
  
  @Sele("sele")
  FutureCall<List<Asd>> resultType_futureCall_list();
  
  @Sele("sele")
  @SuppressWarnings("rawtypes")
  FutureCall<Map> resultType_futureCall_emptyMap();
  
  @Sele("sele")
  FutureCall<Map<String, Asd>> resultType_futureCall_noMapKey();
  
  @Sele("sele")
  @MapKey("mapKeyFieldName")
  FutureCall<Map<String, Asd>> resultType_futureCall_map();
  
  @Sele("sele")
  @MapKey("mapKeyFieldName")
  FutureCall<Map<String, List<Asd>>> resultType_futureCall_mapList();
  
  @Sele("sele")
  @MapKey("mapKeyFieldName")
  FutureCall<Map<String, Set<Asd>>> resultType_futureCall_mapSet();
  
  @Modi("modi sql text")
  void voidModi();
  
  @Sele("sele")
  FutureCall<Set<Asd>> resultType_futureCall_set();
  
  @Sele("sele")
  Set<Asd> resultType_set();
  
}
