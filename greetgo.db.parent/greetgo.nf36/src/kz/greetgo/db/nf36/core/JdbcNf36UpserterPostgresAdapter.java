package kz.greetgo.db.nf36.core;

import kz.greetgo.db.ConnectionCallback;
import kz.greetgo.db.Jdbc;
import kz.greetgo.db.nf36.utils.SqlConvertUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

public class JdbcNf36UpserterPostgresAdapter implements Nf36Upserter, ConnectionCallback<Void> {

  private Jdbc jdbc;
  private String tableName;
  private String nf3prefix;
  private String nf6prefix;

  public JdbcNf36UpserterPostgresAdapter(Jdbc jdbc) {
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
  public Void doInConnection(Connection con) throws Exception {

    String sql = "insert into " + nf3prefix + tableName + "("
      + idValueMap.keySet().stream().sorted().collect(Collectors.joining(", "))
      + ", "
      + fieldValueMap.keySet().stream().sorted().collect(Collectors.joining(", "))
      + ") values ("
      + idValueMap.keySet().stream().map(k -> "?").collect(Collectors.joining(", "))
      + ", "
      + fieldValueMap.keySet().stream().map(k -> "?").collect(Collectors.joining(", "))
      + ") on conflict ("
      + idValueMap.keySet().stream().sorted().collect(Collectors.joining(", "))
      + ") do update set "
      + fieldValueMap.keySet().stream().map(k -> k + " = ?").collect(Collectors.joining(", "))
      + "";

    List<Object> params = Stream.concat(
      idValueMap.entrySet().stream().sorted(comparing(Map.Entry::getKey)).map(Map.Entry::getValue),
      Stream.concat(
        fieldValueMap.entrySet().stream().sorted(comparing(Map.Entry::getKey)).map(Map.Entry::getValue),
        fieldValueMap.entrySet().stream().sorted(comparing(Map.Entry::getKey)).map(Map.Entry::getValue)
      )
    ).collect(Collectors.toList());

    System.out.println("sql: " + sql);

    try (PreparedStatement ps = con.prepareStatement(sql)) {

      int index = 1;
      for (Object param : params) {
        ps.setObject(index++, param);
      }

      ps.executeUpdate();
    }

    return null;
  }
}
