package kz.greetgo.gbatis.futurecall;

import static org.fest.assertions.api.Assertions.assertThat;

import java.net.URL;
import java.util.Date;

import kz.greetgo.gbatis.model.Param;
import kz.greetgo.gbatis.model.Request;
import kz.greetgo.gbatis.model.SqlWithParams;
import kz.greetgo.gbatis.model.WithView;
import kz.greetgo.sqlmanager.gen.Conf;
import kz.greetgo.sqlmanager.parser.StruShaper;
import kz.greetgo.util.db.DbType;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class PreparedSqlTest {
  
  private Conf conf;
  private StruShaper sg;
  
  private void setupSG() throws Exception {
    URL url = getClass().getResource("PreparedSqlTest.stru.nf3");
    sg = new StruShaper();
    sg.printPStru = false;
    sg.parse(url);
  }
  
  private void setupConf() {
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
  }
  
  @BeforeMethod
  public void setup() throws Exception {
    setupConf();
    setupSG();
  }
  
  @Test
  public void prepare_01() throws Exception {
    Request r = new Request();
    
    r.withList.add(new WithView("v_client", "x_client", "surname", "name", "age"));
    
    r.sql = "select * from x_client where age = #{age} and age = #{age2} or age = #{age}";
    r.paramList.add(new Param(Integer.class, "age"));
    r.paramList.add(new Param(Integer.class, "age2"));
    
    SqlWithParams sql = PreparedSql.prepare(conf, sg.stru, r, new Object[] { 13, 709 }, new Date(),
        DbType.PostgreSQL, 100, 300);
    
    assertThat(sql).isNotNull();
    assertThat(sql.sql).isNotNull();
    
    System.out.println("sql.sql = " + sql.sql);
    for (int i = 0, C = sql.params.size(); i < C; i++) {
      System.out.println("Param" + (i + 1) + " = " + sql.params.get(i));
    }
  }
  
  @Test
  public void prepare_02() throws Exception {
    Request r = new Request();
    
    r.withList.add(new WithView("v_client", "x_client", "surname", "name", "age"));
    
    r.sql = "select * from x_client where age = #{age} and age = #{age2} or age = #{age}";
    
    @SuppressWarnings("unused")
    class Asd {
      public int age = 78;
      public int age2 = 1001;
    }
    
    SqlWithParams sql = PreparedSql.prepare(conf, sg.stru, r, new Object[] { new Asd() },
        new Date(), DbType.PostgreSQL, 100, 300);
    
    assertThat(sql).isNotNull();
    assertThat(sql.sql).isNotNull();
    
    System.out.println("sql.sql = " + sql.sql);
    for (int i = 0, C = sql.params.size(); i < C; i++) {
      System.out.println("Param" + (i + 1) + " = " + sql.params.get(i));
    }
  }
  
  @Test
  public void prepare_03() throws Exception {
    Request r = new Request();
    
    r.withList.add(new WithView("v_client", "x_client", "surname", "name", "age"));
    
    r.sql = "select * from ${table} where age = #{age} and age = #{age2} or age = #{age}";
    r.paramList.add(new Param(Integer.class, "age"));
    r.paramList.add(new Param(Integer.class, "age2"));
    r.paramList.add(new Param(String.class, "table"));
    
    SqlWithParams sql = PreparedSql.prepare(conf, sg.stru, r, new Object[] { 13, 709, "x_client" },
        new Date(), DbType.PostgreSQL, 100, 300);
    
    assertThat(sql).isNotNull();
    assertThat(sql.sql).isNotNull();
    
    System.out.println("sql.sql = " + sql.sql);
    for (int i = 0, C = sql.params.size(); i < C; i++) {
      System.out.println("Param" + (i + 1) + " = " + sql.params.get(i));
    }
    
    assertThat(sql.sql).contains("select * from x_client where");
  }
  
  @Test
  public void prepare_04() throws Exception {
    Request r = new Request();
    
    r.withList.add(new WithView("v_client", "x_client", "surname", "name", "age"));
    
    r.sql = "select * from ${table} where age = #{age} and age = #{age2} or age = #{age}";
    r.paramList.add(new Param(Integer.class, "age"));
    r.paramList.add(new Param(Integer.class, "age2"));
    r.paramList.add(new Param(String.class, "table"));
    
    SqlWithParams sql = PreparedSql.prepare(conf, sg.stru, r, new Object[] { 13, 709, "x_client" },
        new Date(), DbType.PostgreSQL, 100, 300);
    
    assertThat(sql).isNotNull();
    assertThat(sql.sql).isNotNull();
    
    System.out.println("sql.sql = " + sql.sql);
    for (int i = 0, C = sql.params.size(); i < C; i++) {
      System.out.println("Param" + (i + 1) + " = " + sql.params.get(i));
    }
    
    assertThat(sql.sql).contains("select * from x_client where");
  }
  
}
