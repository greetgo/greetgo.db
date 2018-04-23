package kz.greetgo.db.nf36;

import kz.greetgo.db.nf36.core.Nf3ID;
import kz.greetgo.db.nf36.core.Nf3ReferenceTo;
import kz.greetgo.db.nf36.model.DbType;
import kz.greetgo.db.nf36.model.Nf3Field;
import kz.greetgo.db.nf36.utils.SqlTypeUtil;

import java.lang.reflect.Field;

class Nf3FieldImpl implements Nf3Field {
  private final Field source;
  private final Nf3ID nf3ID;
  private final int enumLength;

  public Nf3FieldImpl(Field source, int enumLength) {
    this.source = source;
    nf3ID = source.getAnnotation(Nf3ID.class);
    this.enumLength = enumLength;
  }

  @Override
  public boolean isId() {
    return nf3ID != null;
  }

  @Override
  public int idOrder() {
    return nf3ID == null ? 0 : nf3ID.order();
  }

  @Override
  public String javaName() {
    return source.getName();
  }

  @Override
  public String dbName() {
    return UtilsNf36.javaNameToDbName(javaName());
  }

  @Override
  public Class<?> javaType() {
    return source.getType();
  }

  @Override
  public DbType dbType() {
    return SqlTypeUtil.extractDbType(source, enumLength);
  }

  @Override
  public Class<?> referenceTo() {
    {
      Nf3ReferenceTo a = source.getAnnotation(Nf3ReferenceTo.class);
      if (a != null) return a.value();
    }
    {
      Nf3ID a = source.getAnnotation(Nf3ID.class);
      if (a != null && a.ref() != Object.class) return a.ref();
    }
    return null;
  }

  @Override
  public String nextPart() {
    {
      Nf3ReferenceTo a = source.getAnnotation(Nf3ReferenceTo.class);
      if (a != null && a.nextPart().length() > 0) return a.nextPart();
    }
    {
      Nf3ID a = source.getAnnotation(Nf3ID.class);
      if (a != null && a.nextPart().length() > 0) return a.nextPart();
    }
    return null;
  }

  @Override
  public boolean isReference() {
    return referenceTo() != null;
  }

  @Override
  public boolean hasNextPart() {
    return nextPart() != null;
  }

  @Override
  public boolean notNullAndNotPrimitive() {
    return !dbType().nullable() && !source.getType().isPrimitive();
  }
}
