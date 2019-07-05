package kz.greetgo.sqlmanager.gen.procedures;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

  private static String streamToStr0(InputStream in) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte buffer[] = new byte[2048];
    while (true) {
      int count = in.read(buffer);
      if (count < 0) {
        return out.toString("UTF-8");
      }
      out.write(buffer, 0, count);
    }
  }

  private Changer() {}
}
