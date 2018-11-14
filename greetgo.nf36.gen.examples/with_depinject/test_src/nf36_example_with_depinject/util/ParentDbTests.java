package nf36_example_with_depinject.util;

import kz.greetgo.db.ConnectionCallback;
import kz.greetgo.db.Jdbc;
import kz.greetgo.db.worker.db.DbParameters;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.depinject.testng.AbstractDepinjectTestNg;
import nf36_example_with_depinject.errors.SqlError;
import nf36_example_with_depinject.jdbc.ByOne;
import nf36_example_with_depinject.jdbc.ByOneCount;
import nf36_example_with_depinject.jdbc.ByOneLast;
import nf36_example_with_depinject.jdbc.ByTwo;
import nf36_example_with_depinject.jdbc.ByTwoCount;
import nf36_example_with_depinject.jdbc.ByTwoLast;
import nf36_example_with_depinject.jdbc.Now;

import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static kz.greetgo.db.nf36.utils.SqlConvertUtil.fromSql;

@SuppressWarnings("SameParameterValue")
public abstract class ParentDbTests extends AbstractDepinjectTestNg {

  public BeanGetter<DbParameters> dbParameters;

  protected String t(String tableName) {
    if (tableName.startsWith("m_")) {
      return dbParameters.get().nf6prefix() + tableName.substring(2);
    }
    return tableName;
  }

  public BeanGetter<Jdbc> jdbc;

  protected void exec(String sql) {

    jdbc.get().execute((ConnectionCallback<Void>) con -> {
      try (PreparedStatement st = con.prepareStatement(sql)) {
        System.out.println("EXEC SQL " + sql);
        st.executeUpdate();
      }
      return null;
    });

  }

  protected void exec(String sql, SqlError.Type... ignoringSqlErrorTypes) {
    try {
      exec(sql);
    } catch (SqlError e) {
      for (SqlError.Type type : ignoringSqlErrorTypes) {
        if (e.type == type) {
          return;//ignoring this error
        }
      }
      throw e;
    }
  }

  protected <T> T load(String idName, Object idValue,
                       String tableName, String field) {
    return jdbc.get().execute(new ByOne<>(idName, idValue, tableName, field));
  }

  protected Date loadDate(String idName, Object idValue,
                          String tableName, String field) {
    return fromSql(load(idName, idValue, tableName, field), Date.class);
  }

  protected String loadStr(String idName, Object idValue,
                           String tableName, String field) {
    return load(idName, idValue, tableName, field);
  }

  protected String loadStr(String id1Name, Object id1Value,
                           String id2Name, Object id2Value,
                           String tableName, String field) {
    return load(id1Name, id1Value, id2Name, id2Value, tableName, field);
  }

  protected int loadInt(String idName, Object idValue,
                        String tableName, String field) {
    return fromSql(load(idName, idValue, tableName, field), int.class);
  }

  protected int loadInt(String id1Name, Object id1Value,
                        String id2Name, Object id2Value,
                        String tableName, String field) {
    return fromSql(load(id1Name, id1Value, id2Name, id2Value, tableName, field), int.class);
  }

  protected long loadLong(String idName, Object idValue,
                          String tableName, String field) {
    return fromSql(load(idName, idValue, tableName, field), long.class);
  }

  protected long loadLong(String id1Name, Object id1Value,
                          String id2Name, Object id2Value,
                          String tableName, String field) {
    return fromSql(load(id1Name, id1Value, id2Name, id2Value, tableName, field), long.class);
  }

  protected String loadTS(String idName, Object idValue,
                          String tableName, String field) {
    return formatTS(load(idName, idValue, tableName, field));
  }

  private String formatTS(Object value) {
    if (value == null) {
      return null;
    }
    {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      return sdf.format(fromSql(value));
    }
  }

  protected String loadTS(String id1Name, Object id1Value,
                          String id2Name, Object id2Value,
                          String tableName, String field) {
    return formatTS(load(id1Name, id1Value, id2Name, id2Value, tableName, field));
  }

  protected int loadCount(String idName, Object idValue, String tableName) {
    return jdbc.get().execute(new ByOneCount(idName, idValue, tableName));
  }

  protected <T> T loadLast(String idName, Object idValue,
                           String tableName, String field) {
    return jdbc.get().execute(new ByOneLast<>(idName, idValue, tableName, field));
  }

  protected String loadLastStr(String idName, Object idValue,
                               String tableName, String field) {
    return loadLast(idName, idValue, tableName, field);
  }

  protected String loadLastStr(String id1Name, Object id1Value,
                               String id2Name, Object id2Value,
                               String tableName, String field) {
    return loadLast(id1Name, id1Value, id2Name, id2Value, tableName, field);
  }

  protected String loadLastTS(String idName, Object idValue,
                              String tableName, String field) {
    return formatTS(loadLast(idName, idValue, tableName, field));
  }

  protected String loadLastTS(String id1Name, Object id1Value,
                              String id2Name, Object id2Value,
                              String tableName, String field) {
    return formatTS(loadLast(id1Name, id1Value, id2Name, id2Value, tableName, field));
  }

  protected int loadLastInt(String idName, Object idValue,
                            String tableName, String field) {
    return fromSql(loadLast(idName, idValue, tableName, field), int.class);
  }

  protected int loadLastInt(String id1Name, Object id1Value,
                            String id2Name, Object id2Value,
                            String tableName, String field) {
    return fromSql(loadLast(id1Name, id1Value, id2Name, id2Value, tableName, field), int.class);
  }

  protected long loadLastLong(String idName, Object idValue,
                              String tableName, String field) {
    return fromSql(loadLast(idName, idValue, tableName, field), long.class);
  }

  protected long loadLastLong(String id1Name, Object id1Value,
                              String id2Name, Object id2Value,
                              String tableName, String field) {
    return fromSql(loadLast(id1Name, id1Value, id2Name, id2Value, tableName, field), long.class);
  }

  protected <T> T load(String idName1, Object idValue1,
                       String idName2, Object idValue2,
                       String tableName, String field) {
    return jdbc.get().execute(new ByTwo<>(idName1, idValue1, idName2, idValue2, tableName, field));
  }

  protected int loadCount(String idName1, Object idValue1,
                          String idName2, Object idValue2,
                          String tableName) {
    return jdbc.get().execute(new ByTwoCount(idName1, idValue1, idName2, idValue2, tableName));
  }

  protected <T> T loadLast(String idName1, Object idValue1,
                           String idName2, Object idValue2,
                           String tableName, String field) {
    return jdbc.get().execute(new ByTwoLast<>(idName1, idValue1, idName2, idValue2, tableName, field));
  }

  protected static Date ts(String ts) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try {
      return sdf.parse(ts);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  protected static String str(Date date) {
    if (date == null) {
      return null;
    }
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return sdf.format(date);
  }

  protected Date now() {
    return jdbc.get().execute(new Now());
  }

  protected void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
