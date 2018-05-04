package kz.greetgo.sqlmanager.model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Простой тип (long, int, float, ...)
 *
 * @author pompei
 */
public class SimpleType extends Type implements JavaType {

  public final boolean needSequence;
  public final String objectType;
  public final String javaType;

  private SimpleType(String name, boolean needSequence, String objectType, String javaType) {
    super(name, null);
    this.needSequence = needSequence;
    this.objectType = objectType;
    this.javaType = javaType;
  }

  public static final SimpleType tlong, tint, tfloat, tline, tword;
  public static final SimpleType tlongline, ttext, ttime, tbool;

  @SuppressFBWarnings("MS_MUTABLE_COLLECTION")
  public static final Map<String, SimpleType> SimpleTypes = new HashMap<>();

  static {
    tlong = add("long", true, "Long", "long");
    tint = add("int", true, "Integer", "int");

    tfloat = add("float", false, "Double", "double");

    tline = add("line", false, "String", "String");//varchar(300)
    tword = add("word", false, "String", "String");//varchar(100)
    tlongline = add("longline", false, "String", "String");//varchar(4000)
    ttext = add("text", false, "String", "String");

    ttime = add("time", false, "java.util.Date", "java.util.Date");
    tbool = add("bool", false, "Boolean", "boolean");
  }

  private static SimpleType add(String name, boolean needSequence, String objectType,
                                String javaType) {
    SimpleType x = new SimpleType(name, needSequence, objectType, javaType);
    SimpleTypes.put(x.name, x);
    return x;
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public void assignSimpleTypes(List<SimpleType> types) {
    types.add(this);
  }

  @Override
  public void assignJavaTypes(List<JavaType> types) {
    types.add(this);
  }

  @Override
  public String javaType() {
    return javaType;
  }

  public boolean isString() {
    return "String".equals(objectType);
  }

  @Override
  public String objectType() {
    return objectType;
  }
}
