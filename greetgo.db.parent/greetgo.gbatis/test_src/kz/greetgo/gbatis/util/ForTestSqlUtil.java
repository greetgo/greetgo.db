package kz.greetgo.gbatis.util;

public enum ForTestSqlUtil {
  
  ONE {
    @Override
    public int hi() {
      return 12;
    }
  },
  TWO {
    @Override
    public int hi() {
      return 13;
    }
  };
  
  public abstract int hi();
}
