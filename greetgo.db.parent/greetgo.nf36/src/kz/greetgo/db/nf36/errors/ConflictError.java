package kz.greetgo.db.nf36.errors;

public class ConflictError extends RuntimeException {
  public ConflictError(Object a1, Object a2) {
    super("@" + a1.getClass().getSimpleName()
      + " and @" + a2.getClass().getSimpleName()
      + " are in conflict with each other");
  }
}
