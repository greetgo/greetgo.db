package kz.greetgo.gbatis.gen;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class CodeUtil {
  public static String spaces(int count) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < count; i++) {
      sb.append(' ');
    }
    return sb.toString();
  }

  public static String strValueToCode(String str) {
    if (str == null) return "null";

    StringBuilder sb = new StringBuilder();

    sb.append('"');

    for (int i = 0, n = str.length(); i < n; i++) {

      final char c = str.charAt(i);

      if (c == '\\') {
        sb.append("\\\\");
        continue;
      }
      if (c == '"') {
        sb.append("\\\"");
        continue;
      }
      if (c == '\n') {
        sb.append("\\n");
        continue;
      }
      if (c == '\r') {
        sb.append("\\r");
        continue;
      }

      sb.append(c);

    }

    sb.append('"');

    return sb.toString();
  }

  static <T extends Enum> String enumValueToCode(Class<T> anEnum, T value) {
    if (value == null) return "null";
    return classNameToCodeName(anEnum.getName()) + "." + value.name();
  }

  public static String classNameToCodeName(String className) {
    return className.replace('$', '.');
  }

  static String classValueToCode(Class<?> aClass) {
    if (aClass == null) return "null";
    return classNameToCodeName(aClass.getName()) + ".class";
  }

  public static String typeToCode(Type type) {
    if (type == null) throw new NullPointerException("type == null");

    StringBuilder sb = new StringBuilder();
    appendTypeCode(sb, type);

    return sb.toString();
  }

  private static void appendTypeCode(StringBuilder sb, Type type) {
    if (type instanceof Class) {
      sb.append(classNameToCodeName(((Class<?>) type).getName()));
      return;
    }
    if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;
      appendTypeCode(sb, parameterizedType.getRawType());
      sb.append('<');
      for (Type argumentType : parameterizedType.getActualTypeArguments()) {
        appendTypeCode(sb, argumentType);
        sb.append(", ");
      }
      sb.setLength(sb.length() - 2);
      sb.append('>');
      return;
    }
    throw new IllegalArgumentException("Unknown argument type");
  }
}
