package kz.greetgo.db.nf36.gen;

public enum AuthorType {
  STR {
    @Override
    public void checkLength(int typeLength) {
      if (typeLength <= 0) {
        throw new RuntimeException("Illegal STR length = " + typeLength);
      }
    }
  }, INT {
    @Override
    public void checkLength(int typeLength) {
      if (typeLength != 4 && typeLength != 8) {
        throw new RuntimeException("Illegal int length: int length may be 4 or 8");
      }
    }
  };

  public abstract void checkLength(int typeLength);
}
