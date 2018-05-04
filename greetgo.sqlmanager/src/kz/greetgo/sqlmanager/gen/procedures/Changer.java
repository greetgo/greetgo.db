package kz.greetgo.sqlmanager.gen.procedures;

import static kz.greetgo.util.ServerUtil.streamToStr0;

import java.io.InputStream;

import kz.greetgo.sqlmanager.gen.UserIdFieldType;

public class Changer {
  public static final String[] forOracle(UserIdFieldType uift) {
    return changers("oracle", uift);
  }
  
  public static final String[] forPostgres(UserIdFieldType uift) {
    return changers("postgres", uift);
  }
  
  private static String[] changers(String db, UserIdFieldType uift) {
    return loadAndSplit("changer_" + db + "_" + uift.name().toLowerCase() + ".sql");
  }
  
  private static String[] loadAndSplit(String name) {
    try (InputStream in = Changer.class.getResourceAsStream(name)) {
      if (in == null) throw new IllegalArgumentException("No resource " + name);
      return streamToStr0(in).split(";;");
    } catch (Exception e) {
      if (e instanceof RuntimeException) throw (RuntimeException)e;
      throw new RuntimeException(e);
    }
  }
  
  private Changer() {}
}
