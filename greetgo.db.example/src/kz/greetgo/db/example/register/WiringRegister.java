package kz.greetgo.db.example.register;

import java.util.List;

public interface WiringRegister {
  void createWiringTable();

  void insertZeroWiringList(List<String> wiringIdList);

  void move(String wiringIdFrom, String wiringIdTo, long amount);

  void cleanWiring();

  long getTotalValue();
}
