package kz.greetgo.db.nf36.gen.example.util;

import kz.greetgo.db.ConnectionCallback;
import kz.greetgo.db.Jdbc;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import kz.greetgo.db.nf36.utils.SqlConvertUtil;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class JdbcNf36UpserterAdapter implements Nf36Upserter, ConnectionCallback<Void> {

  private Jdbc jdbc;
  private String tableName;
  private String nf3prefix;
  private String nf6prefix;

  public JdbcNf36UpserterAdapter(Jdbc jdbc) {
    this.jdbc = jdbc;
  }

  @Override
  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  private final Map<String, Object> idValueMap = new HashMap<>();
  private final Map<String, Object> fieldValueMap = new HashMap<>();

  @Override
  public void putId(String idName, Object idValue) {
    idValueMap.put(idName, SqlConvertUtil.forSql(idValue));
  }

  @Override
  public void putField(String fieldName, Object fieldValue) {
    fieldValueMap.put(fieldName, SqlConvertUtil.forSql(fieldValue));
  }

  @Override
  public void setNf3Prefix(String nf3prefix) {
    this.nf3prefix = nf3prefix;
  }

  @Override
  public void setNf6Prefix(String nf6prefix) {
    this.nf6prefix = nf6prefix;
  }

  @Override
  public void go() {
    jdbc.execute(this);
  }

  @Override
  public Void doInConnection(Connection connection) throws Exception {



    return null;
  }
}
