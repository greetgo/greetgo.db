package kz.greetgo.db.nf36.gen.example.util;

public class DbUrlUtils {
  public static String changeUrlDbName(String url, String dbName) {
    int idx = url.lastIndexOf('/');
    return url.substring(0, idx + 1) + dbName;
  }

  public static String extractDbName(String url) {
    int idx = url.lastIndexOf('/');
    return url.substring(idx + 1);
  }
}
