package nf36_example_with_depinject.errors;

public class DropTableError extends RuntimeException {
  public DropTableError(String message, Throwable e) {
    super(message, e);
  }
}
