package kz.greetgo.db.example.register_impl;

import kz.greetgo.db.InTransaction;
import kz.greetgo.db.Jdbc;
import kz.greetgo.db.example.register.WiringRegister;
import kz.greetgo.db.example.register_impl.jdbc.Exec;
import kz.greetgo.db.example.register_impl.jdbc.ExecParams;
import kz.greetgo.db.example.register_impl.jdbc.GetTotalValue;
import kz.greetgo.db.example.register_impl.jdbc.GetWiringValue;
import kz.greetgo.db.example.register_impl.jdbc.SetWiringValue;
import kz.greetgo.depinject.core.Bean;
import kz.greetgo.depinject.core.BeanGetter;

import java.util.List;

@Bean
@InTransaction
public class WiringRegisterImpl implements WiringRegister {

  public BeanGetter<Jdbc> jdbc;

  @Override
  public void createWiringTable() {
    jdbc.get().execute(new Exec("create table wiring (\n" +
      "  id varchar(32) primary key,\n" +
      "  value bigint not null default 0\n" +
      ")"));
  }

  @Override
  public void insertZeroWiringList(List<String> wiringIdList) {
    wiringIdList.forEach(id -> jdbc.get().execute(new ExecParams("insert into wiring (id) values (?)", id)));
  }

  @Override
  public void cleanWiring() {
    jdbc.get().execute(new Exec("delete from wiring"));
  }

  @Override
  public void move(String id1, String id2, long amount) {

    if (id1 == null || id2 == null) throw new NullPointerException();
    if (id1.equals(id2)) return;
    if (id1.compareTo(id2) < 0) {
      String tmp = id1;
      id1 = id2;
      id2 = tmp;
      amount = -amount;
    }

    Long value1 = jdbc.get().execute(new GetWiringValue(id1));
    Long value2 = jdbc.get().execute(new GetWiringValue(id2));

//    String s1 = "(" + value1 + ", " + value2 + ")";

    value1 -= amount;
    value2 += amount;

//    String s2 = "(" + value1 + ", " + value2 + ")";

    jdbc.get().execute(new SetWiringValue(id1, value1));
    jdbc.get().execute(new SetWiringValue(id2, value2));

//    System.out.println(s1 + " --> " + s2 + " on (" + id1 + ", " + id2 + ")");
  }

  @Override
  public long getTotalValue() {
    return jdbc.get().execute(new GetTotalValue());
  }
}
