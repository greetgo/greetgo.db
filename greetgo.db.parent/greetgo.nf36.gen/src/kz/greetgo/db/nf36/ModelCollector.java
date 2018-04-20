package kz.greetgo.db.nf36;

import kz.greetgo.db.nf36.core.Nf3ID;
import kz.greetgo.db.nf36.model.DbType;
import kz.greetgo.db.nf36.model.Nf3Field;
import kz.greetgo.db.nf36.model.Nf3Table;
import kz.greetgo.db.nf36.utils.SqlUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModelCollector {

  String nf3prefix;
  String nf6prefix;

  private ModelCollector() {
  }

  public static ModelCollector newCollector() {
    return new ModelCollector();
  }

  public ModelCollector setNf3Prefix(String nf3prefix) {
    this.nf3prefix = nf3prefix;
    return this;
  }

  public ModelCollector setNf6Prefix(String nf6prefix) {
    this.nf6prefix = nf6prefix;
    return this;
  }

  final List<Object> registeredObjects = new ArrayList<>();

  public ModelCollector register(Object object) {
    registeredObjects.add(object);
    return this;
  }

  private boolean collected = false;

  public List<Nf3Table> collect() {
    if (collected) throw new RuntimeException("Already collected");
    collected = true;

    List<Nf3Table> ret = new ArrayList<>();

    for (Object object : registeredObjects) {
      ret.add(new Nf3Table() {
        @Override
        public Class<?> source() {
          return object.getClass();
        }

        @Override
        public List<Nf3Field> fields() {
          return Arrays.stream(object.getClass().getFields())
            .map(f -> convertField(f))
            .collect(Collectors.toList());
        }

        @Override
        public String nf3prefix() {
          return nf3prefix;
        }

        @Override
        public String nf6prefix() {
          return nf6prefix;
        }

        @Override
        public String tableName() {
          String ret = UtilsNf36.javaNameToDbName(object.getClass().getSimpleName());
          if (ret.startsWith("_")) return ret.substring(1);
          return ret;
        }
      });
    }

    return ret;
  }

  private Nf3Field convertField(Field f) {
    Nf3ID nf3ID = f.getAnnotation(Nf3ID.class);

    return new Nf3Field() {
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
        return f.getName();
      }

      @Override
      public String dbName() {
        return UtilsNf36.javaNameToDbName(javaName());
      }

      @Override
      public Class<?> javaType() {
        return f.getType();
      }

      @Override
      public DbType dbType() {
        return SqlUtil.convertType(javaType());
      }
    };
  }

}
