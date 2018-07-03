package kz.greetgo.db.nf36.gen;

import kz.greetgo.db.nf36.core.Nf3DefaultNow;
import kz.greetgo.db.nf36.core.Nf3DefaultValue;
import kz.greetgo.db.nf36.errors.ConflictError;
import kz.greetgo.db.nf36.model.DbType;
import kz.greetgo.db.nf36.model.Sequence;
import kz.greetgo.db.nf36.model.SqlType;
import kz.greetgo.db.nf36.utils.UtilsNf36;

import java.lang.reflect.Field;

public abstract class SqlDialectAbstract implements SqlDialect {
  @Override
  public String createFieldDefinition(DbType dbType, String name, Field field, Object definer) throws Exception {
    String type = extractType(dbType);

    Nf3DefaultValue aDefStrValue = field.getAnnotation(Nf3DefaultValue.class);
    Nf3DefaultNow aDefNow = field.getAnnotation(Nf3DefaultNow.class);

    if (aDefNow != null && aDefStrValue != null) {
      throw new ConflictError(aDefNow, aDefStrValue);
    }

    if (dbType.sqlType() == SqlType.TIMESTAMP) {
      String def = aDefNow != null ? " default " + now() + " not null" : "";
      return name + " " + type + def;
    }

    if (field.getType() == boolean.class) {
      if (aDefStrValue != null) throw new RuntimeException("@" + Nf3DefaultValue.class.getSimpleName()
        + " is incompatible with boolean");
      if (aDefNow != null) throw new RuntimeException("@" + Nf3DefaultNow.class.getSimpleName()
        + " is incompatible with boolean");
      boolean defaultValue = (boolean) field.get(definer);
      String def = " default " + (defaultValue ? "1" : "0") + " not null";

      return name + " " + type + def;
    }

    String def = "";

    if (aDefStrValue != null) {
      def = " default '" + UtilsNf36.quoteForSql(aDefStrValue.value()) + "'";
    }

    return name + " " + type + def;
  }

  @Override
  public String createAuthorFieldDefinition(AuthorField authorField) {
    return authorField.name + " " + extractAuthorType(authorField.authorType, authorField.typeLength);
  }

  protected String extractAuthorType(AuthorType authorType, int typeLength) {

    switch (authorType) {

      case STR:
        return strType() + "(" + typeLength + ")";

      case INT:
        return intType(typeLength);

      default:
        throw new IllegalArgumentException("Unknown authorType = " + authorType);
    }

  }

  @Override
  public void checkObjectName(String objectName, ObjectNameType objectNameType) {}

  private String extractType(DbType t) {
    switch (t.sqlType()) {
      case TIMESTAMP:
        return "TIMESTAMP";
      case VARCHAR:
        return strType() + "(" + t.len() + ")";
      case INT:
        return intType(t.len());
      case BOOL:
        return smallintType();
      case BIGINT:
        return bigintType();
      case CLOB:
        return clobType();
      case DECIMAL:
        return "DECIMAL(" + t.len() + ", " + t.scale() + ")";
      default:
        throw new IllegalArgumentException("Cannot create type for " + t.sqlType());
    }
  }

  @Override
  public String fieldTimestampWithDefaultNow(String fieldName) {
    return fieldName + " timestamp default " + now() + " not null";
  }

  @Override
  public String createSequence(Sequence sequence) {
    return "create sequence " + sequence.name + " start with " + sequence.startFrom;
  }

  protected abstract String strType();

  protected abstract String bigintType();

  protected abstract String smallintType();

  protected abstract String intType(int length);

  protected abstract String clobType();

  protected abstract String now();
}
