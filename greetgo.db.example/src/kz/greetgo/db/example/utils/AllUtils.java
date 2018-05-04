package kz.greetgo.db.example.utils;

public class AllUtils {
  public static String replaceDbName(String url, String dbName) {
    int lastIndex = url.lastIndexOf('/');
    if (lastIndex < 0) throw new RuntimeException("Unknown URL format: " + url);
    return url.substring(0, lastIndex) + "/" + dbName;
  }
}
