package kz.greetgo.db.nf36.gen;

import kz.greetgo.db.nf36.core.Nf3Description;
import kz.greetgo.db.nf36.core.Nf3ID;
import kz.greetgo.db.nf36.core.Nf3ReferenceTo;
import kz.greetgo.db.nf36.model.DbType;
import kz.greetgo.db.nf36.model.Nf3Field;
import kz.greetgo.db.nf36.model.Nf3Table;

import java.lang.reflect.Field;
import java.util.List;

class Nf3FieldImpl implements Nf3Field {
  private final Object definer;
  private final Field source;
  private final Nf3ID nf3ID;
  private final ModelCollector collector;

  public Nf3FieldImpl(Object definer, Field source, ModelCollector collector) {
    this.definer = definer;
    this.source = source;
    nf3ID = source.getAnnotation(Nf3ID.class);
    this.collector = collector;
  }

  @Override
  public Object definer() {
    return definer;
  }

  @Override
  public Field source() {
    return source;
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
    return SqlTypeUtil.extractDbType(source, collector);
  }

  @Override
  public Class<?> referenceToClass() {
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
    return referenceToClass() != null;
  }

  boolean isRootReference = false;

  @Override
  public boolean isRootReference() {
    return isRootReference;
  }

  List<Nf3Field> referenceFields = null;

  @Override
  public List<Nf3Field> referenceFields() {
    if (referenceFields == null) throw new RuntimeException("No referenceFields");
    return referenceFields;
  }

  Nf3Table referenceTo = null;

  @Override
  public Nf3Table referenceTo() {
    if (referenceTo == null) throw new RuntimeException("No referenceTo");
    return referenceTo;
  }

  Nf3Field rootField = this;

  @Override
  public Nf3Field rootField() {
    return rootField;
  }

  @Override
  public boolean hasNextPart() {
    return nextPart() != null;
  }

  @Override
  public boolean notNullAndNotPrimitive() {
    return !dbType().nullable() && !source.getType().isPrimitive();
  }

  @Override
  public String commentQuotedForSql() {
    Nf3Description d = source.getAnnotation(Nf3Description.class);
    if (d == null) {
      throw new RuntimeException("No description for field " + definer.getClass().getSimpleName()
        + "." + source.getName());
    }
    return UtilsNf36.quoteForSql(d.value());
  }
}
