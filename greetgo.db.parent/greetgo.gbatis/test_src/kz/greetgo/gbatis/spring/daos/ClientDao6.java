package kz.greetgo.gbatis.spring.daos;

import java.util.Date;
import java.util.List;

import kz.greetgo.gbatis.model.FutureCall;
import kz.greetgo.gbatis.t.Autoimpl;
import kz.greetgo.gbatis.t.Call;
import kz.greetgo.gbatis.t.Prm;
import kz.greetgo.gbatis.t.Sele;
import kz.greetgo.gbatis.t.T1;

@Autoimpl
public interface ClientDao6 {
  @T1("m_client")
  @Sele("select client as id, surname, name, patronymic, age"
      + " from x_client where age <= #{age} order by client")
  FutureCall<List<Client>> youngClients(@Prm("age") int age);
  
  @T1(value = "m_client", fields = { "${field}, age" })
  @Sele("select client as id, ${field} from x_client where age <= #{age} order by client")
  FutureCall<List<String>> youngClientsField(@Prm("age") int age, @Prm("field") String field);
  
  @T1("m_client")
  @Sele("select client as id,surname,name,patronymic,age from x_client")
  FutureCall<List<Client>> allClients();
  
  @Sele("select moment()")
  Date now();
  
  @Sele("select nextval('s_client')")
  long nextClient();
  
  @Call("{call p_client (#{id})}")
  void insClient(@Prm("id") long id);
  
  @Call("{call p_client_surname (#{id}, #{surname})}")
  void insClientSurname(@Prm("id") long id, @Prm("surname") String surname);
  
  @Call("{call p_client_name (#{id}, #{name})}")
  void insClientName(@Prm("id") long id, @Prm("name") String name);
  
  @Call("{call p_client_name (#{id}, #{patronymic})}")
  void insClientPatronymic(@Prm("id") long id, @Prm("patronymic") String patronymic);
  
  @Call("{call p_client_age (#{id}, #{age})}")
  void insClientAge(@Prm("id") long id, @Prm("age") int age);
  
  @Sele("select moment() - interval '3 days' - interval '21 seconds'")
  Date nowMinus3();
  
  @Sele("with asd as (select 1 as x) select 1 from asd where x = 2")
  FutureCall<Boolean> getFalse();
}
