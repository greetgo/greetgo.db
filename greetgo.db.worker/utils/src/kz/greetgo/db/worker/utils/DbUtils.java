package kz.greetgo.db.worker.utils;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class DbUtils {
  public static String changeUrlDbName(String url, String dbName) {
    int idx = url.lastIndexOf('/');
    return url.substring(0, idx + 1) + dbName;
  }

  public static String extractDbName(String url) {
    int idx = url.lastIndexOf('/');
    return url.substring(idx + 1);
  }

  public static SQLException extractSqlException(Throwable e) {
    Set<Throwable> marked = new HashSet<>();

    Throwable current = e;

    while (true) {

      if (current == null) {
        return null;
      }

      if (marked.contains(current)) {
        return null;
      }
      marked.add(current);

      if (current instanceof SQLException) {
        return (SQLException) current;
      }

      current = current.getCause();
    }
  }
}
