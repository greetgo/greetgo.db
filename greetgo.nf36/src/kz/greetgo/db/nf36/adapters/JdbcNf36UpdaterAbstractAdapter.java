package kz.greetgo.db.nf36.adapters;

import kz.greetgo.db.ConnectionCallback;
import kz.greetgo.db.Jdbc;
import kz.greetgo.db.nf36.core.Nf36Updater;
import kz.greetgo.db.nf36.core.SqlLogAcceptor;
import kz.greetgo.db.nf36.model.SqlLog;
import kz.greetgo.db.nf36.utils.SqlConvertUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.joining;

public abstract class JdbcNf36UpdaterAbstractAdapter implements Nf36Updater, ConnectionCallback<Void> {
  Jdbc jdbc;
  SqlLogAcceptor logAcceptor = null;
  protected String nf3TableName;
  protected List<String> idFieldNames;
  protected String nf3ModifiedBy;
  protected String nf6InsertedBy;
  protected Object author;

  @Override
  public void setNf3TableName(String tableName) {
    this.nf3TableName = tableName;
  }

  @Override
  public void setAuthorFieldNames(String nf3ModifiedBy, String nf6InsertedBy) {
    this.nf3ModifiedBy = nf3ModifiedBy;
    this.nf6InsertedBy = nf6InsertedBy;
  }

  @Override
  public Nf36Updater setAuthor(Object author) {
    this.author = author;
    return this;
  }

  @Override
  public void setIdFieldNames(String... idFieldNames) {
    this.idFieldNames = Arrays.asList(idFieldNames);
  }

  protected final List<String> updateToNowFieldList = new ArrayList<>();

  @Override
  public void updateFieldToNow(String fieldName) {
    updateToNowFieldList.add(fieldName);
  }

  protected IdValues extractIdValues(ResultSet rs) throws SQLException {
    List<Object> ret = new ArrayList<>();

    int index = 1;
    for (String ignore : idFieldNames) {
      ret.add(rs.getObject(index++));
    }

    return new IdValues(ret);
  }

  protected static class SetField {
    public final String nf6TableName;
    public final String fieldName;
    public final Object fieldValue;

    public SetField(String nf6TableName, String fieldName, Object fieldValue) {
      this.nf6TableName = nf6TableName;
      this.fieldName = fieldName;
      this.fieldValue = fieldValue;
    }
  }

  protected final List<SetField> setFieldList = new ArrayList<>();

  @Override
  public void setField(String nf6TableName, String fieldName, Object fieldValue) {
    setFieldList.add(new SetField(nf6TableName, fieldName, SqlConvertUtil.forSql(fieldValue)));
  }

  protected static class Where {
    public final String fieldName;
    public final Object fieldValue;

    public Where(String fieldName, Object fieldValue) {
      this.fieldName = fieldName;
      this.fieldValue = fieldValue;
    }
  }

  protected final List<Where> whereList = new ArrayList<>();

  @Override
  public void where(String fieldName, Object fieldValue) {
    whereList.add(new Where(fieldName, SqlConvertUtil.forSql(fieldValue)));
  }

  protected static class IdValues {
    public final List<Object> values;

    public IdValues(List<Object> values) {this.values = values;}
  }

  @Override
  public void commit() {
    if (idFieldNames == null || idFieldNames.isEmpty()) {
      throw new RuntimeException("No id names. Please call method setIdFieldNames");
    }

    jdbc.execute(this);
  }

  @Override
  public Void doInConnection(Connection con) throws Exception {

    boolean autoCommit = con.getAutoCommit();

    if (autoCommit) {
      con.setAutoCommit(false);
    }

    try {

      execute(con);

      if (autoCommit) con.commit();

    } catch (Exception e) {
      if (autoCommit) con.rollback();
      throw e;
    } finally {
      con.setAutoCommit(autoCommit);
    }

    return null;
  }

  private void execute(Connection con) throws Exception {
    List<IdValues> idValuesList = updateNf3(con);
    for (IdValues idValues : idValuesList) {
      insertIntoNf6(idValues, con);
    }
  }

  protected void insertIntoNf6(IdValues idValues, Connection con) throws Exception {
    for (SetField setField : setFieldList) {
      if (setField.nf6TableName != null) {
        insertIntoNf6Table(setField, idValues, con);
      }
    }
  }

  protected void insertIntoNf6Table(SetField setField, IdValues idValues, Connection con) throws Exception {
    List<Object> params = new ArrayList<>(idValues.values);

    String insByF = "", insByV = "";
    if (nf6InsertedBy != null) {
      insByF = ", " + nf6InsertedBy;
      insByV = ", ?";
      params.add(author);
    }

    params.add(setField.fieldValue);

    String sql = "insert into " + setField.nf6TableName + " (" + (

        String.join(", ", idFieldNames)

    ) + insByF + ", " + setField.fieldName + ") values (" + (

      idFieldNames.stream().map(fn -> "?").collect(joining(", "))

    ) + insByV + ", ?)";

    long startedAt = System.nanoTime();

    try (PreparedStatement ps = con.prepareStatement(sql)) {

      int index = 1;
      for (Object param : params) {
        ps.setObject(index++, param);
      }

      ps.executeUpdate();

      if (logAcceptor != null && logAcceptor.isTraceEnabled()) {
        SqlLog log = new SqlLog(sql, params, null, System.nanoTime() - startedAt);
        logAcceptor.accept(log);
      }

    } catch (Exception e) {

      if (logAcceptor != null && logAcceptor.isErrorEnabled()) {
        SqlLog log = new SqlLog(sql, params, e, System.nanoTime() - startedAt);
        logAcceptor.accept(log);
      }

      throw e;
    }

  }

  protected abstract List<IdValues> updateNf3(Connection con) throws Exception;
}
