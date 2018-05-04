package kz.greetgo.db.nf36.gen;

public class AuthorField {
  public final String name;
  public final AuthorType authorType;
  public final int typeLength;

  public AuthorField(String name, AuthorType authorType, int typeLength) {
    authorType.checkLength(typeLength);
    this.name = name;
    this.authorType = authorType;
    this.typeLength = typeLength;
  }
}
