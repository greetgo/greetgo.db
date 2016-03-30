package kz.greetgo.gbatis2.struct.exceptions;

public class GbatisException extends RuntimeException {
  public GbatisException(String message) {
    super(message);
  }

  public GbatisException(String message, Throwable cause) {
    super(message, cause);
  }
}
