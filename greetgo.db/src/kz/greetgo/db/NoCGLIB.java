package kz.greetgo.db;

public class NoCGLIB extends RuntimeException {
  public NoCGLIB(ClassNotFoundException e) {
    super("No library cglib. Please add: compile 'cglib:cglib:3.2.0' : 3.2.0 or more", e);
  }
}
