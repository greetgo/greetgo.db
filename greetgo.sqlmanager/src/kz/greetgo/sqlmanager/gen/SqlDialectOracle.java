package kz.greetgo.sqlmanager.gen;

import java.util.HashMap;
import java.util.Map;

import kz.greetgo.sqlmanager.model.SimpleType;

/**
 * Диалект для Оракла. См.: {@link SqlDialect}
 * 
 * @author pompei
 */
public class SqlDialectOracle implements SqlDialect {
  
  private final Map<SimpleType, String> simpleTypeMap = new HashMap<>();
  {
    simpleTypeMap.put(SimpleType.tbool, "int");
    simpleTypeMap.put(SimpleType.tfloat, "double precision");
    simpleTypeMap.put(SimpleType.tint, "int");
    simpleTypeMap.put(SimpleType.tline, "varchar2(300)");
    simpleTypeMap.put(SimpleType.tlong, "number");
    simpleTypeMap.put(SimpleType.tlongline, "varchar2(4000)");
    simpleTypeMap.put(SimpleType.ttext, "clob");
    simpleTypeMap.put(SimpleType.ttime, "timestamp(9)");
    simpleTypeMap.put(SimpleType.tword, "varchar2(100)");
  }
  private final Map<SimpleType, String> simpleTypeProcMap = new HashMap<>();
  {
    simpleTypeProcMap.put(SimpleType.tbool, "int");
    simpleTypeProcMap.put(SimpleType.tfloat, "double precision");
    simpleTypeProcMap.put(SimpleType.tint, "int");
    simpleTypeProcMap.put(SimpleType.tline, "varchar2");
    simpleTypeProcMap.put(SimpleType.tlong, "number");
    simpleTypeProcMap.put(SimpleType.tlongline, "varchar2");
    simpleTypeProcMap.put(SimpleType.ttext, "clob");
    simpleTypeProcMap.put(SimpleType.ttime, "timestamp");
    simpleTypeProcMap.put(SimpleType.tword, "varchar2");
  }
  
  @Override
  public String sqlType(SimpleType simpleType) {
    String ret = simpleTypeMap.get(simpleType);
    if (ret == null) throw new RuntimeException("No SQL type for " + simpleType);
    return ret;
  }
  
  @Override
  public String procType(SimpleType simpleType) {
    String ret = simpleTypeProcMap.get(simpleType);
    if (ret == null) throw new RuntimeException("No proc SQL type for " + simpleType);
    return ret;
  }
  
  @Override
  public String timestamp() {
    return "timestamp(9)";
  }
  
  @Override
  public String current_timestamp() {
    return "systimestamp";
  }
  
  @Override
  public String userIdFieldType(UserIdFieldType userIdFieldType, int userIdLength) {
    switch (userIdFieldType) {
    case Long:
      return "number";
      
    case Str:
      return "varchar2(" + userIdLength + ")";
      
    default:
      throw new IllegalArgumentException("userIdFieldType = " + userIdFieldType);
    }
  }
}
