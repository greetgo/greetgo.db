package kz.greetgo.sqlmanager.parser;

public class NoComment extends RuntimeException {
  
  public final String comment;
  
  public NoComment(String comment, CannotOpenUrlStream e) {
    super(comment, e);
    this.comment = comment;
  }
}
