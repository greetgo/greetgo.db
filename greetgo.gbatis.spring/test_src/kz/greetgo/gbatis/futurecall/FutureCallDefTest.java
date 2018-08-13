package kz.greetgo.gbatis.futurecall;

import static org.fest.assertions.api.Assertions.assertThat;

import java.net.URL;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import kz.greetgo.gbatis.model.Param;
import kz.greetgo.gbatis.model.Request;
import kz.greetgo.gbatis.model.RequestType;
import kz.greetgo.gbatis.model.ResultType;
import kz.greetgo.gbatis.util.AbstractWithDbTest;

import org.fest.assertions.data.MapEntry;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class FutureCallDefTest extends AbstractWithDbTest {
  @Override
  protected URL getStruUrl() {
    return getClass().getResource("stru.nf3");
  }
  
  @BeforeMethod
  public void setup() throws Exception {
    prepareConf();
    prepareSG();
    setupJdbcTemplate();
  }
  
  @AfterMethod
  public void teardown() throws Exception {
//    dataSource.close();
    dataSource = null;
    jdbc = null;
  }
  
  @SuppressWarnings("unused")
  public static class Client {
    @SuppressWarnings("unused")
    public long id;
    @SuppressWarnings("unused")
    public String surname, name, patronymic;
    
    @Override
    public String toString() {
      return "Client [id=" + id + ", surname=" + surname + ", name=" + name + ", patronymic=" + patronymic + "]";
    }
    
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + (int)(id ^ (id >>> 32));
      return result;
    }
    
    @Override
    public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null) return false;
      if (getClass() != obj.getClass()) return false;
      Client other = (Client)obj;
      //noinspection RedundantIfStatement
      if (id != other.id) return false;
      return true;
    }
  }
  
  @Test
  public void last_select() throws Exception {

    {
      List<String> sqlList = new ArrayList<>();

      sqlList.add("insert into m_client (client) values (11)");
      sqlList.add("insert into m_client_surname (client, surname) values (11, 'Колпаков')");
      sqlList.add("insert into m_client_name (client, name) values (11, 'Евгений')");
      sqlList.add("insert into m_client_patronymic (client, patronymic) values (11, 'Анатольевич')");
      sqlList.add("insert into m_client_age (client, age) values (11, 30)");

      sqlList.add("insert into m_client (client) values (21)");
      sqlList.add("insert into m_client_surname (client, surname) values (21, 'Берсенев')");
      sqlList.add("insert into m_client_name (client, name) values (21, 'Олег')");
      sqlList.add("insert into m_client_patronymic (client, patronymic) values (21, 'Алексеевич')");
      sqlList.add("insert into m_client_age (client, age) values (21, 30)");

      sqlList.add("insert into m_client (client) values (13)");
      sqlList.add("insert into m_client_surname (client, surname) values (13, 'Иванов')");
      sqlList.add("insert into m_client_name (client, name) values (13, 'Иван')");
      sqlList.add("insert into m_client_patronymic (client, patronymic) values (13, 'Иванович')");
      sqlList.add("insert into m_client_age (client, age) values (13, 14)");

      executeSqls(sqlList);
    }

    Request req = new Request();
    req.callNow = true;
    req.paramList.add(new Param(Integer.TYPE, "age"));
    req.result.resultDataClass = Client.class;
    req.result.type = ResultType.LIST;
    req.type = RequestType.Sele;
    req.sql = "select client as id, surname, name, patronymic"
        + " from v_client where age = #{age} order by client";

    FutureCallDef<List<Client>> fc = new FutureCallDef<>(conf, sg.stru, jdbc, req,
        new Object[] { 30 });

    List<Client> list = fc.last();

    for (Client cl : list) {
      System.out.println(cl);
    }

    assertThat(list).hasSize(2);
    assertThat(list.get(0).id).isEqualTo(11);
    assertThat(list.get(0).surname).isEqualTo("Колпаков");
    assertThat(list.get(0).name).isEqualTo("Евгений");
    assertThat(list.get(0).patronymic).isEqualTo("Анатольевич");

    assertThat(list.get(1).id).isEqualTo(21);
    assertThat(list.get(1).surname).isEqualTo("Берсенев");
    assertThat(list.get(1).name).isEqualTo("Олег");
    assertThat(list.get(1).patronymic).isEqualTo("Алексеевич");

  }

  @Test
  public void last_select_map_withMapKeyField() throws Exception {
    {
      List<String> sqlList = new ArrayList<>();

      sqlList.add("insert into m_client (client) values (11)");
      sqlList.add("insert into m_client_surname (client, surname) values (11, 'Колпаков')");
      sqlList.add("insert into m_client_name (client, name) values (11, 'Евгений')");
      sqlList.add("insert into m_client_patronymic (client, patronymic) values (11, 'Анатольевич')");
      sqlList.add("insert into m_client_age (client, age) values (11, 30)");

      sqlList.add("insert into m_client (client) values (21)");
      sqlList.add("insert into m_client_surname (client, surname) values (21, 'Берсенев')");
      sqlList.add("insert into m_client_name (client, name) values (21, 'Олег')");
      sqlList.add("insert into m_client_patronymic (client, patronymic) values (21, 'Алексеевич')");
      sqlList.add("insert into m_client_age (client, age) values (21, 30)");

      sqlList.add("insert into m_client (client) values (13)");
      sqlList.add("insert into m_client_surname (client, surname) values (13, 'Иванов')");
      sqlList.add("insert into m_client_name (client, name) values (13, 'Иван')");
      sqlList.add("insert into m_client_patronymic (client, patronymic) values (13, 'Иванович')");
      sqlList.add("insert into m_client_age (client, age) values (13, 14)");

      executeSqls(sqlList);
    }

    Request req = new Request();
    req.callNow = true;
    req.result.resultDataClass = Client.class;
    req.result.mapKeyField = "id";
    req.result.type = ResultType.MAP;
    req.type = RequestType.Sele;
    req.sql = "select surname, client as id, name, patronymic from v_client";

    FutureCallDef<Map<Long, Client>> fc = new FutureCallDef<>(conf, sg.stru, jdbc, req,
        new Object[] {});

    Map<Long, Client> map = fc.last();

    for (Entry<Long, Client> e : map.entrySet()) {
      System.out.println(e);
    }

    assertThat(map).hasSize(3);
    assertThat(map).containsKey(11L);
    assertThat(map).containsKey(21L);
    assertThat(map).containsKey(13L);

    assertThat(map.get(11L).id).isEqualTo(11);
    assertThat(map.get(11L).surname).isEqualTo("Колпаков");
    assertThat(map.get(11L).name).isEqualTo("Евгений");
    assertThat(map.get(11L).patronymic).isEqualTo("Анатольевич");

    assertThat(map.get(21L).id).isEqualTo(21);
    assertThat(map.get(21L).surname).isEqualTo("Берсенев");
    assertThat(map.get(21L).name).isEqualTo("Олег");
    assertThat(map.get(21L).patronymic).isEqualTo("Алексеевич");
  }

  @Test
  public void last_select_map_noMapKeyField() throws Exception {
    {
      List<String> sqlList = new ArrayList<>();

      sqlList.add("insert into m_client (client) values (11)");
      sqlList.add("insert into m_client_surname (client, surname) values (11, 'Колпаков')");
      sqlList.add("insert into m_client_name (client, name) values (11, 'Евгений')");
      sqlList.add("insert into m_client_patronymic (client, patronymic) values (11, 'Анатольевич')");
      sqlList.add("insert into m_client_age (client, age) values (11, 30)");

      sqlList.add("insert into m_client (client) values (21)");
      sqlList.add("insert into m_client_surname (client, surname) values (21, 'Берсенев')");
      sqlList.add("insert into m_client_name (client, name) values (21, 'Олег')");
      sqlList.add("insert into m_client_patronymic (client, patronymic) values (21, 'Алексеевич')");
      sqlList.add("insert into m_client_age (client, age) values (21, 30)");

      sqlList.add("insert into m_client (client) values (13)");
      sqlList.add("insert into m_client_surname (client, surname) values (13, 'Иванов')");
      sqlList.add("insert into m_client_name (client, name) values (13, 'Иван')");
      sqlList.add("insert into m_client_patronymic (client, patronymic) values (13, 'Иванович')");
      sqlList.add("insert into m_client_age (client, age) values (13, 14)");

      executeSqls(sqlList);
    }

    Request req = new Request();
    req.callNow = true;
    req.result.resultDataClass = Client.class;
    req.result.mapKeyField = null;//no map key field
    req.result.type = ResultType.MAP;
    req.type = RequestType.Sele;
    req.sql = "select client as id, name, patronymic, surname from v_client";

    FutureCallDef<Map<Long, Client>> fc = new FutureCallDef<>(conf, sg.stru, jdbc, req,
        new Object[] { 30 });

    Map<Long, Client> map = fc.last();

    for (Entry<Long, Client> e : map.entrySet()) {
      System.out.println(e);
    }

    assertThat(map).hasSize(3);
    assertThat(map).containsKey(11L);
    assertThat(map).containsKey(21L);
    assertThat(map).containsKey(13L);

    assertThat(map.get(11L).id).isEqualTo(11);
    assertThat(map.get(11L).surname).isEqualTo("Колпаков");
    assertThat(map.get(11L).name).isEqualTo("Евгений");
    assertThat(map.get(11L).patronymic).isEqualTo("Анатольевич");

    assertThat(map.get(21L).id).isEqualTo(21);
    assertThat(map.get(21L).surname).isEqualTo("Берсенев");
    assertThat(map.get(21L).name).isEqualTo("Олег");
    assertThat(map.get(21L).patronymic).isEqualTo("Алексеевич");
  }

  @Test
  public void last_select_map_noMapKeyField_simpleValueType() {

    Request req = new Request();
    req.callNow = true;
    req.result.resultDataClass = Long.class;
    req.result.mapKeyField = null;//no map key field
    req.result.type = ResultType.MAP;
    req.type = RequestType.Sele;

    //noinspection StringBufferReplaceableByString
    StringBuilder sb = new StringBuilder();
    sb.append(" select 'asd', 1");
    sb.append(" union all");
    sb.append(" select 'dsa', 2");
    sb.append(" union all");
    sb.append(" select 'wow', 3");
    req.sql = sb.toString();

    FutureCallDef<Map<String, Long>> fc = new FutureCallDef<>(conf, sg.stru, jdbc, req,
        new Object[] {});

    Map<String, Long> map = fc.last();

    for (Entry<String, Long> e : map.entrySet()) {
      System.out.println(e);
    }

    assertThat(map).hasSize(3);
    assertThat(map).contains(MapEntry.entry("asd", 1L));
    assertThat(map).contains(MapEntry.entry("dsa", 2L));
    assertThat(map).contains(MapEntry.entry("wow", 3L));

  }

  @Test
  public void last_select_mapList_noMapKeyField_simpleValueType() {

    Request req = new Request();
    req.callNow = true;
    req.result.resultDataClass = Integer.class;
    req.result.mapKeyField = null;//no map key field
    req.result.type = ResultType.MAP_LIST;
    req.type = RequestType.Sele;

    //noinspection StringBufferReplaceableByString
    StringBuilder sb = new StringBuilder();
    sb.append("           select 'asd', 1");
    sb.append(" union all select 'asd', 2");
    sb.append(" union all select 'asd', 3");
    sb.append(" union all select 'dsa', 4");
    sb.append(" union all select 'dsa', 5");
    sb.append(" union all select 'wow', 6");
    sb.append(" union all select 'wow', 7");
    sb.append(" union all select 'wow', 8");
    sb.append(" union all select 'wow', 9");
    req.sql = sb.toString();

    FutureCallDef<Map<String, List<Integer>>> fc = new FutureCallDef<>(conf, sg.stru, jdbc, req,
        new Object[] {});

    Map<String, List<Integer>> map = fc.last();

    for (Entry<String, List<Integer>> e : map.entrySet()) {
      System.out.println(e);
    }

    assertThat(map).hasSize(3);
    assertThat(map).contains(MapEntry.entry("asd", lst(1, 2, 3)));
    assertThat(map).contains(MapEntry.entry("dsa", lst(4, 5)));
    assertThat(map).contains(MapEntry.entry("wow", lst(6, 7, 8, 9)));

  }

  @Test
  public void last_select_mapSet_noMapKeyField_simpleValueType() {

    Request req = new Request();
    req.callNow = true;
    req.result.resultDataClass = Integer.class;
    req.result.mapKeyField = null;//no map key field
    req.result.type = ResultType.MAP_SET;
    req.type = RequestType.Sele;

    //noinspection StringBufferReplaceableByString
    StringBuilder sb = new StringBuilder();
    sb.append("           select 'asd', 1");
    sb.append(" union all select 'asd', 2");
    sb.append(" union all select 'asd', 3");

    sb.append(" union all select 'dsa', 4");
    sb.append(" union all select 'dsa', 5");

    sb.append(" union all select 'wow', 6");
    sb.append(" union all select 'wow', 7");
    sb.append(" union all select 'wow', 8");
    sb.append(" union all select 'wow', 9");
    req.sql = sb.toString();

    FutureCallDef<Map<String, Set<Integer>>> fc = new FutureCallDef<>(conf, sg.stru, jdbc, req,
        new Object[] {});

    Map<String, Set<Integer>> map = fc.last();

    for (Entry<String, Set<Integer>> e : map.entrySet()) {
      System.out.println(e);
    }

    assertThat(map).hasSize(3);
    assertThat(map).contains(MapEntry.entry("asd", set(1, 2, 3)));
    assertThat(map).contains(MapEntry.entry("dsa", set(4, 5)));
    assertThat(map).contains(MapEntry.entry("wow", set(6, 7, 8, 9)));

  }

  @SuppressWarnings("unused")
  public static class Tmp01 {
    public String asd;
    public Integer age;
    public double val;
    
    @Override
    public String toString() {
      return "Tmp001 [asd=" + asd + ", age=" + age + ", val=" + val + "]";
    }
    
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((age == null) ? 0 :age.hashCode());
      long temp;
      temp = Double.doubleToLongBits(val);
      result = prime * result + (int)(temp ^ (temp >>> 32));
      return result;
    }
    
    @Override
    public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null) return false;
      if (getClass() != obj.getClass()) return false;
      Tmp01 other = (Tmp01)obj;
      if (age == null) {
        if (other.age != null) return false;
      } else if (!age.equals(other.age)) return false;
      //noinspection RedundantIfStatement
      if (Double.doubleToLongBits(val) != Double.doubleToLongBits(other.val)) return false;
      return true;
    }
    
  }
  
  @Test
  public void last_select_mapList_hasMapKeyField() {

    Request req = new Request();
    req.callNow = true;
    req.result.resultDataClass = Tmp01.class;
    req.result.mapKeyField = "asd";
    req.result.type = ResultType.MAP_LIST;
    req.type = RequestType.Sele;

    //noinspection StringBufferReplaceableByString
    StringBuilder sb = new StringBuilder();

    sb.append("           select 1 as age, 'asd' as asd, 2.3 as val ");
    sb.append(" union all select 2 as age, 'asd' as asd, 3.3 as val ");
    sb.append(" union all select 3 as age, 'asd' as asd, 4.3 as val ");

    sb.append(" union all select 4 as age, 'dsa' as asd, 5.3 as val ");
    sb.append(" union all select 5 as age, 'dsa' as asd, 6.3 as val ");

    sb.append(" union all select 6 as age, 'wow' as asd, 7.3 as val ");

    req.sql = sb.toString();

    FutureCallDef<Map<String, List<Tmp01>>> fc = new FutureCallDef<>(conf, sg.stru, jdbc, req,
        new Object[] {});

    Map<String, List<Tmp01>> map = fc.last();

    for (Entry<String, List<Tmp01>> e : map.entrySet()) {
      System.out.println(e);
    }

    assertThat(map).hasSize(3);
    assertThat(map.get("asd")).isEqualTo(lst(tmp01(1, 2.3), tmp01(2, 3.3), tmp01(3, 4.3)));
    assertThat(map.get("dsa")).isEqualTo(lst(tmp01(4, 5.3), tmp01(5, 6.3)));
    assertThat(map.get("wow")).isEqualTo(lst(tmp01(6, 7.3)));

  }

  @Test
  public void last_select_mapSet_hasMapKeyField() {

    Request req = new Request();
    req.callNow = true;
    req.result.resultDataClass = Tmp01.class;
    req.result.mapKeyField = "asd";
    req.result.type = ResultType.MAP_SET;
    req.type = RequestType.Sele;

    //noinspection StringBufferReplaceableByString
    StringBuilder sb = new StringBuilder();

    sb.append("           select 1 as age, 'asd' as asd, 2.3 as val ");
    sb.append(" union all select 2 as age, 'asd' as asd, 3.3 as val ");
    sb.append(" union all select 3 as age, 'asd' as asd, 4.3 as val ");

    sb.append(" union all select 4 as age, 'dsa' as asd, 5.3 as val ");
    sb.append(" union all select 5 as age, 'dsa' as asd, 6.3 as val ");

    sb.append(" union all select 6 as age, 'wow' as asd, 7.3 as val ");

    req.sql = sb.toString();

    FutureCallDef<Map<String, Set<Tmp01>>> fc = new FutureCallDef<>(conf, sg.stru, jdbc, req,
        new Object[] {});

    Map<String, Set<Tmp01>> map = fc.last();

    for (Entry<String, Set<Tmp01>> e : map.entrySet()) {
      System.out.println(e);
    }

    assertThat(map).hasSize(3);
    assertThat(map.get("asd")).isEqualTo(set(tmp01(1, 2.3), tmp01(2, 3.3), tmp01(3, 4.3)));
    assertThat(map.get("dsa")).isEqualTo(set(tmp01(4, 5.3), tmp01(5, 6.3)));
    assertThat(map.get("wow")).isEqualTo(set(tmp01(6, 7.3)));

  }

  private static Tmp01 tmp01(Integer age, double val) {
    Tmp01 ret = new Tmp01();
    ret.age = age;
    ret.val = val;
    return ret;
  }
  
  @SafeVarargs
  private static <T> List<T> lst(@SuppressWarnings("unchecked") T... tt) {
    List<T> ret = new ArrayList<>();
    Collections.addAll(ret, tt);
    return ret;
  }
  
  @SafeVarargs
  private static <T> Set<T> set(@SuppressWarnings("unchecked") T... tt) {
    Set<T> ret = new HashSet<>();
    Collections.addAll(ret, tt);
    return ret;
  }
  
  @Test
  public void last_modify() throws Exception {
    {
      List<String> sqlList = new ArrayList<>();

      sqlList.add("insert into m_client (client) values (11)");
      sqlList.add("insert into m_client_age (client, age) values (11, 30)");

      sqlList.add("insert into m_client (client) values (21)");
      sqlList.add("insert into m_client_age (client, age) values (21, 30)");

      sqlList.add("insert into m_client (client) values (13)");
      sqlList.add("insert into m_client_age (client, age) values (13, 14)");

      executeSqls(sqlList);
    }
    Request req = new Request();
    req.callNow = true;
    req.paramList.add(new Param(Integer.TYPE, "age"));
    req.paramList.add(new Param(Date.class, "atTime"));
    req.result.resultDataClass = Integer.TYPE;
    req.result.type = ResultType.SIMPLE;
    req.type = RequestType.Modi;
    req.sql = "update m_client_age set ts = #{atTime} where age = #{age}";

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    Object[] args = new Object[] { 30, sdf.parse("2014-01-01 11:00:00") };
    FutureCallDef<Integer> fc = new FutureCallDef<>(conf, sg.stru, jdbc, req, args);

    Integer lastRet = fc.last();

    assertThat(lastRet).isEqualTo(2);

  }

  @Test
  public void last_int() throws Exception {
    {
      List<String> sqlList = new ArrayList<>();

      sqlList.add("insert into m_client (client) values (11)");
      sqlList.add("insert into m_client_surname (client, surname) values (11, 'Колпаков')");
      sqlList.add("insert into m_client_name (client, name) values (11, 'Евгений')");
      sqlList.add("insert into m_client_patronymic (client, patronymic) values (11, 'Анатольевич')");
      sqlList.add("insert into m_client_age (client, age) values (11, 30)");

      sqlList.add("insert into m_client (client) values (21)");
      sqlList.add("insert into m_client_surname (client, surname) values (21, 'Берсенев')");
      sqlList.add("insert into m_client_name (client, name) values (21, 'Олег')");
      sqlList.add("insert into m_client_patronymic (client, patronymic) values (21, 'Алексеевич')");
      sqlList.add("insert into m_client_age (client, age) values (21, 30)");

      sqlList.add("insert into m_client (client) values (13)");
      sqlList.add("insert into m_client_surname (client, surname) values (13, 'Иванов')");
      sqlList.add("insert into m_client_name (client, name) values (13, 'Иван')");
      sqlList.add("insert into m_client_patronymic (client, patronymic) values (13, 'Иванович')");
      sqlList.add("insert into m_client_age (client, age) values (13, 14)");

      executeSqls(sqlList);
    }

    Request req = new Request();
    req.callNow = true;
    req.paramList.add(new Param(Integer.TYPE, "age"));
    req.result.resultDataClass = Long.class;
    req.result.type = ResultType.SIMPLE;
    req.type = RequestType.Sele;
    req.sql = "select client from v_client where age = #{age}";

    FutureCallDef<Long> fc = new FutureCallDef<>(conf, sg.stru, jdbc, req, new Object[] { 14 });

    Long id = fc.last();

    assertThat(id).isEqualTo(13);

  }

  @Test
  public void last_select_set() throws Exception {
    {
      List<String> sqlList = new ArrayList<>();

      sqlList.add("insert into m_client (client) values (11)");
      sqlList.add("insert into m_client_surname (client, surname) values (11, 'Колпаков')");
      sqlList.add("insert into m_client_name (client, name) values (11, 'Евгений')");
      sqlList.add("insert into m_client_patronymic (client, patronymic) values (11, 'Анатольевич')");
      sqlList.add("insert into m_client_age (client, age) values (11, 30)");

      sqlList.add("insert into m_client (client) values (21)");
      sqlList.add("insert into m_client_surname (client, surname) values (21, 'Берсенев')");
      sqlList.add("insert into m_client_name (client, name) values (21, 'Олег')");
      sqlList.add("insert into m_client_patronymic (client, patronymic) values (21, 'Алексеевич')");
      sqlList.add("insert into m_client_age (client, age) values (21, 30)");

      sqlList.add("insert into m_client (client) values (13)");
      sqlList.add("insert into m_client_surname (client, surname) values (13, 'Иванов')");
      sqlList.add("insert into m_client_name (client, name) values (13, 'Иван')");
      sqlList.add("insert into m_client_patronymic (client, patronymic) values (13, 'Иванович')");
      sqlList.add("insert into m_client_age (client, age) values (13, 14)");

      executeSqls(sqlList);
    }

    Request req = new Request();
    req.callNow = true;
    req.paramList.add(new Param(Integer.TYPE, "age"));
    req.result.resultDataClass = Client.class;
    req.result.type = ResultType.SET;
    req.type = RequestType.Sele;
    req.sql = "select client as id, surname, name, patronymic"
        + " from v_client where age = #{age} order by client";

    FutureCallDef<Set<Client>> fc = new FutureCallDef<>(conf, sg.stru, jdbc, req,
        new Object[] { 30 });

    Set<Client> list = fc.last();

    for (Client cl : list) {
      System.out.println(cl);
    }

    assertThat(list).hasSize(2);

  }

  @Test
  public void last_select_enum() {

    Request req = new Request();
    req.callNow = true;
    req.paramList.add(new Param(Integer.TYPE, "age"));
    req.result.resultDataClass = EnumAsd.class;
    req.result.type = ResultType.SIMPLE;
    req.type = RequestType.Sele;
    req.sql = "select 'ASD'";

    FutureCallDef<EnumAsd> fc = new FutureCallDef<>(conf, sg.stru, jdbc, req, new Object[] { 30 });

    EnumAsd asd = fc.last();

    assertThat(asd).isEqualTo(EnumAsd.ASD);

  }

  @Test
  public void last_select_enum_list() {

    Request req = new Request();
    req.callNow = true;
    req.paramList.add(new Param(Integer.TYPE, "age"));
    req.result.resultDataClass = EnumAsd.class;
    req.result.type = ResultType.LIST;
    req.type = RequestType.Sele;
    req.sql = "select 'ASD'";

    FutureCallDef<List<EnumAsd>> fc = new FutureCallDef<>(conf, sg.stru, jdbc, req,
        new Object[] { 30 });

    List<EnumAsd> asd = fc.last();

    assertThat(asd).hasSize(1);
    assertThat(asd.get(0)).isEqualTo(EnumAsd.ASD);

  }

  @Test
  public void last_select_str() {

    Request req = new Request();
    req.callNow = true;
    req.result.resultDataClass = String.class;
    req.result.type = ResultType.SIMPLE;
    req.type = RequestType.Sele;
    req.sql = "select 'Hello world'";

    FutureCallDef<String> fc = new FutureCallDef<>(conf, sg.stru, jdbc, req, new Object[] {});

    String asd = fc.last();

    assertThat(asd).isEqualTo("Hello world");

  }

  @Test
  public void last_select_str_list() {

    Request req = new Request();
    req.callNow = true;
    req.result.resultDataClass = String.class;
    req.result.type = ResultType.LIST;
    req.type = RequestType.Sele;
    req.sql = "select 'Hello world'";

    FutureCallDef<List<String>> fc = new FutureCallDef<>(conf, sg.stru, jdbc, req, new Object[] {});

    List<String> asd = fc.last();

    assertThat(asd).hasSize(1);
    assertThat(asd.get(0)).isEqualTo("Hello world");

  }

}
