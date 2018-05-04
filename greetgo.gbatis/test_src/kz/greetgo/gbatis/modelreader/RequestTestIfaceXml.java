package kz.greetgo.gbatis.modelreader;

import java.util.Date;

import kz.greetgo.gbatis.model.FutureCall;
import kz.greetgo.gbatis.probes.asd.ClassNameValue;
import kz.greetgo.gbatis.t.DefaultXml;
import kz.greetgo.gbatis.t.FromXml;
import kz.greetgo.gbatis.t.Prm;
import kz.greetgo.gbatis.t.Sele;
import kz.greetgo.gbatis.t.T1;

@DefaultXml("RequestTestIfaceXml-test.xml")
public interface RequestTestIfaceXml {
  @T1("m_phone")
  @FromXml("methodReadWithList-001")
  FutureCall<ClassNameValue> methodReadWithList(@Prm("asd") long asd);
  
  @FromXml(value = "methodReadSql1-XX", xml = "RequestTestIfaceXml-test2.xml")
  FutureCall<ClassNameValue> methodReadSql1();
  
  void left();
  
  @FromXml("methodReadSql2")
  void methodReadSql2();
  
  void manyParams(@Prm("longParam") long longParam);
  
  @Sele("select name, value from x_asd where asd = #{asd}")
  FutureCall<ClassNameValue> methodParams(@Prm("asd") long asd, @Prm("dsa") Long dsa,
      @Prm("inDate") Date inDate);
  
}
