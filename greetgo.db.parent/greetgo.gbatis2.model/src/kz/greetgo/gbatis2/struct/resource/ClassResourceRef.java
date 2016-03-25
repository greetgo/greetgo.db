package kz.greetgo.gbatis2.struct.resource;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassResourceRef implements ResourceRef {

  private final Class<?> aClass;
  private final String path;

  private ClassResourceRef(Class<?> aClass, String path) {
    this.aClass = aClass;
    this.path = path;
  }

  @Override
  public String toString() {
    return aClass.getSimpleName() + " : " + path;
  }

  public static ResourceRef create(Class<?> aClass, String path) {
    return new ClassResourceRef(aClass, normalizePath(path));
  }

  private static final Pattern KILL1 = Pattern.compile("//");
  private static final Pattern KILL2 = Pattern.compile("/\\./");
  private static final Pattern KILL3 = Pattern.compile("/([^/]+)/\\.\\./");
  private static final Pattern KILL4 = Pattern.compile("[^/]+/\\.\\./(.*)");

  static String normalizePath(String path) {

    while (true) {
      boolean changed = false;

      {
        Matcher matcher = KILL1.matcher(path);
        if (matcher.find()) {
          path = path.substring(0, matcher.start()) + '/' + path.substring(matcher.end());
          changed = true;
        }
      }

      {
        Matcher matcher = KILL2.matcher(path);
        if (matcher.find()) {
          path = path.substring(0, matcher.start()) + '/' + path.substring(matcher.end());
          changed = true;
        }
      }

      {
        Matcher matcher = KILL3.matcher(path);
        if (matcher.find()) {
          if (!matcher.group(1).equals("..")) {
            path = path.substring(0, matcher.start()) + '/' + path.substring(matcher.end());
            changed = true;
          }
        }
      }

      {
        Matcher matcher = KILL4.matcher(path);
        if (matcher.matches()) {
          path = matcher.group(1);
          changed = true;
        }
      }

      if (!changed) return path;
    }

  }

  @Override
  public InputStream getInputStream() {
    return aClass.getResourceAsStream(path);
  }

  @Override
  public ResourceRef change(String path) {
    if (path.startsWith("/")) return create(aClass, path);

    final int lastSlash = this.path.lastIndexOf('/');
    if (lastSlash < 0) return create(aClass, path);

    return create(aClass, this.path.substring(0, lastSlash + 1) + path);
  }

  @Override
  public String display() {
    return aClass.getSimpleName() + "|" + path;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ClassResourceRef that = (ClassResourceRef) o;

    return !(aClass != null ? !aClass.equals(that.aClass) : that.aClass != null) &&
      !(path != null ? !path.equals(that.path) : that.path != null);

  }

  @Override
  public int hashCode() {
    int result = aClass != null ? aClass.hashCode() : 0;
    result = 31 * result + (path != null ? path.hashCode() : 0);
    return result;
  }
}
