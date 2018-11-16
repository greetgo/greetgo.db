package kz.greetgo.db.nf36.errors;

public class CannotSetFieldToClass extends RuntimeException {
  public CannotSetFieldToClass(String fieldName, Class<?> aClass) {
    super("Cannot set field " + fieldName + " to " + aClass);
  }
}
