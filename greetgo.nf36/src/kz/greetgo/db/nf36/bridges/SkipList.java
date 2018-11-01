package kz.greetgo.db.nf36.bridges;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class SkipList {
  private static class Skip {
    final String fieldName;
    final Predicate predicate;

    public Skip(String fieldName, Predicate<?> predicate) {
      Objects.requireNonNull(fieldName);
      Objects.requireNonNull(predicate);
      this.fieldName = fieldName;
      this.predicate = predicate;
    }
  }

  private final List<Skip> list = new ArrayList<>();

  public void addSkipIf(String fieldName, Predicate<?> predicate) {
    list.add(new Skip(fieldName, predicate));
  }

  public boolean needSkip(String fieldName, Object fieldValue) {

    for (Skip skip : list) {

      if (skip.fieldName.equals(fieldName)) {//see above Objects.requireNonNull(fieldName);

        //noinspection unchecked
        if (skip.predicate.test(fieldValue)) {

          return true;
        }
      }
    }

    return false;
  }

}
