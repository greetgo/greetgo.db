package kz.greetgo.db;

import java.util.ArrayList;
import java.util.List;

public class TestExceptionCatcher implements ExceptionCatcher {

  public final List<Throwable> list = new ArrayList<>();

  @Override
  public void catchException(Throwable e) {
    list.add(e);
  }
}
