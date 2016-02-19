package kz.greetgo.gbatis.gen;

import kz.greetgo.gbatis.model.FutureCall;
import kz.greetgo.gbatis.t.Prm;
import kz.greetgo.gbatis.t.Sele;
import kz.greetgo.gbatis.t.T1;

import java.util.List;

public interface TestDao6 {
  @Sele("select id from contract where client_id = #{clientId}")
  FutureCall<Long> loadContractIdList(@Prm("clientId") long clientId);

  @Sele("select id from contract where client_id = #{clientId} and serena = #{serena}")
  long loadContractIdList(@Prm("clientId") long clientId, @Prm("serena") String serena);

  @T1("m_account")
  @Sele("select id from contract")
  List<Long> wow();
}
