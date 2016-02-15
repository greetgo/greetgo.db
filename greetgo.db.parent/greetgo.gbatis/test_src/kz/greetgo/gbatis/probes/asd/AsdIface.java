package kz.greetgo.gbatis.probes.asd;

import kz.greetgo.gbatis.model.FutureCall;
import kz.greetgo.gbatis.t.Prm;
import kz.greetgo.gbatis.t.Sele;
import kz.greetgo.gbatis.t.T1;
import kz.greetgo.gbatis.t.T2;

public interface AsdIface {
  @T1(value = "m_asd", fields = "asd,dsa,wow")
  @T2(value = "m_dsa", fields = "asd,dsa,wow")
  @Sele("select name, value from x_asd where asd = #{asd}")
  FutureCall<ClassNameValue> asdName(@Prm("asd") long asd);
}
