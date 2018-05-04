package kz.greetgo.gbatis.gen.autoimpl_for_tests;

import kz.greetgo.gbatis.model.FutureCall;
import kz.greetgo.gbatis.t.*;

import java.util.List;

@Autoimpl
public interface TestDao6 {
  @Sele("select id from contract where client_id = #{clientId}")
  FutureCall<Long> loadContractIdList(@Prm("clientId") long clientId);

  @Sele("select id from contract where client_id = #{clientId} and serena = #{serena}")
  long loadContractIdList(@Prm("clientId") long clientId, @Prm("serena") String serena);

  @T1("m_account")
  @Sele("select id from contract")
  List<Long> wow();

  @T1("m_account")
  @Modi("update contract set asd=1")
  void updateIt();
}
