package kz.greetgo.db.example.register_impl;

import kz.greetgo.db.example.register.WiringRegister;
import kz.greetgo.db.example.utils.TableAlreadyExists;
import kz.greetgo.db.example.utils.TestParent;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.util.RND;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class WiringRegisterImplTest extends TestParent {

  public BeanGetter<WiringRegister> wiringRegister;

  @Test
  public void checkTransactions() throws Exception {

    try {
      wiringRegister.get().createWiringTable();
    } catch (TableAlreadyExists ignore) {}

    List<String> ids = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      ids.add(RND.str(10));
    }

    wiringRegister.get().cleanWiring();
    wiringRegister.get().insertZeroWiringList(ids);

    {
      long totalValue = wiringRegister.get().getTotalValue();
      assertThat(totalValue).isZero();
    }

    List<Thread> threadList = new ArrayList<>();

    for (int i = 0; i < 10; i++) {
      threadList.add(new Thread(() -> {

        for (int j = 0; j < 10; j++) {

          String idFrom = ids.get(RND.plusInt(ids.size()));
          String idTo = ids.get(RND.plusInt(ids.size()));

          while (idFrom.equals(idTo)) {
            idFrom = ids.get(RND.plusInt(ids.size()));
            idTo = ids.get(RND.plusInt(ids.size()));
          }

          wiringRegister.get().move(idFrom, idTo, RND.plusLong(1_000_000));
        }

      }));
    }

    for (Thread thread : threadList) {
      thread.start();
//      thread.join();
    }
    for (Thread thread : threadList) {
//      thread.start();
      thread.join();
    }

    {
      long totalValue = wiringRegister.get().getTotalValue();
      assertThat(totalValue).isZero();
    }
  }
}