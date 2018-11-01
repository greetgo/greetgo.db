package kz.greetgo.db.nf36.errors;

public class CannotExtractFieldFromClass extends RuntimeException {
  public CannotExtractFieldFromClass(String fieldName, Class<?> aClass) {
    super("Cannot extract field " + fieldName + " from " + aClass);
  }
}
