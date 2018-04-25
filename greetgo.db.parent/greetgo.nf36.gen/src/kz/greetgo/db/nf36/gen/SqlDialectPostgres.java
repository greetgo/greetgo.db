package kz.greetgo.db.nf36.gen;

import kz.greetgo.db.nf36.core.Nf3DefaultNow;
import kz.greetgo.db.nf36.core.Nf3DefaultStrValue;
import kz.greetgo.db.nf36.errors.ConflictError;
import kz.greetgo.db.nf36.model.DbType;
import kz.greetgo.db.nf36.model.SqlType;

import java.lang.reflect.Field;

public class SqlDialectPostgres implements SqlDialect {
  @Override
  public String createFieldDefinition(DbType dbType, String name, Field field, Object definer) throws Exception {
    String type = extractType(dbType.sqlType(), dbType.len());

    Nf3DefaultStrValue aDefStrValue = field.getAnnotation(Nf3DefaultStrValue.class);
    Nf3DefaultNow aDefNow = field.getAnnotation(Nf3DefaultNow.class);

    if (aDefNow != null && aDefStrValue != null) {
      throw new ConflictError(aDefNow, aDefStrValue);
    }

    if (dbType.sqlType() == SqlType.TIMESTAMP) {
      String def = aDefNow != null ? " not null default clock_timestamp()" : "";
      return name + " " + type + def;
    }

    if (field.getType() == boolean.class) {
      if (aDefStrValue != null) throw new RuntimeException("@" + Nf3DefaultStrValue.class.getSimpleName()
        + " is incompatible with boolean");
      if (aDefNow != null) throw new RuntimeException("@" + Nf3DefaultNow.class.getSimpleName()
        + " is incompatible with boolean");
      boolean defaultValue = (boolean) field.get(definer);
      String def = " not null default " + (defaultValue ? "1" : "0");

      return name + " " + type + def;
    }

    String def = "";

    if (aDefStrValue != null) {
      def = " default '" + UtilsNf36.quoteForSql(aDefStrValue.value()) + "'";
    }

    return name + " " + type + def;
  }

  @Override
  public void checkObjectName(String objectName, ObjectNameType objectNameType) {}

  private String extractType(SqlType sqlType, int len) {
    switch (sqlType) {
      case TIMESTAMP:
        return "TIMESTAMP";
      case VARCHAR:
        return "VARCHAR(" + len + ")";
      case INT:
        return "INT" + len;
      case BOOL:
        return "SMALLINT";
      case BIGINT:
        return "BIGINT";
      case CLOB:
        return "TEXT";
      default:
        throw new IllegalArgumentException("Cannot create type for " + sqlType);
    }
  }

  @Override
  public String fieldTimestampWithDefaultNow(String fieldName) {
    return fieldName + " timestamp not null default clock_timestamp()";
  }
}
