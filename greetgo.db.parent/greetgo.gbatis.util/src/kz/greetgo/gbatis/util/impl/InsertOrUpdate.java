package kz.greetgo.gbatis.util.impl;

import kz.greetgo.db.ConnectionCallback;
import kz.greetgo.gbatis.util.iface.TableName;
import kz.greetgo.gbatis.util.model.ColInfo;
import kz.greetgo.util.db.DbType;
import kz.greetgo.util.db.DbTypeDetector;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class InsertOrUpdate {

  private final DbInfo dbInfo;
  private final ClassInfo classInfo;

  public InsertOrUpdate(DbInfo dbInfo, ClassInfo classInfo) {
    this.dbInfo = dbInfo;
    this.classInfo = classInfo;
  }

  public void object(Object object, Object... params) {
    String tableName = extractTableName(object);
    objectTable(object, tableName, params);
  }

  private String extractTableName(Object object) {
    TableName tableName = object.getClass().getAnnotation(TableName.class);
    if (tableName == null) throw new RuntimeException("Cannot extract table name from " + object.getClass());
    return tableName.value();
  }

  private final List<TableModification> modificationList = new ArrayList<>();

  public void objectTable(Object object, String tableName, Object... params) {

    Set<String> columns = new LinkedHashSet<>();
    List<String> keys = new ArrayList<>();
    for (ColInfo info : dbInfo.getColInfo(tableName)) {
      columns.add(info.name);
    }
    for (String keyName : dbInfo.getKeyNames(tableName)) {
      keys.add(keyName);
    }

    TableModification tableModification = new TableModification(tableName, keys);

    ClassAcceptor classAcceptor = classInfo.getClassAcceptor(object.getClass());
    for (String attributeName : classAcceptor.getReadAttributes()) {
      if (columns.contains(attributeName)) {
        Object value = classAcceptor.get(object, attributeName);
        tableModification.setValue(attributeName, value);
      }
    }

    for (int i = 0, c = params.length / 2; i < c; i++) {
      String attributeName = (String) params[2 * i + 0];

      if (!columns.contains(attributeName)) {
        throw new RuntimeException("No column " + attributeName + " in table " + tableName);
      }

      Object attributeValue = params[2 * i + 1];

      tableModification.setValue(attributeName, attributeValue);
    }

    tableModification.checkAllKeyValuesExists();

    modificationList.add(tableModification);
  }

  private ModificationListApplier createModificationListApplier(DbType dbType) {
    switch (dbType) {
      case PostgreSQL:
        return new ModificationListApplierPostgres(dbInfo.sqlViewer());
    }
    throw new IllegalArgumentException("No ModificationListApplier for db type = " + dbType);
  }

  public void go() {
    dbInfo.jdbc().execute(new ConnectionCallback<Void>() {
      @Override
      public Void doInConnection(Connection connection) throws Exception {

        DbType dbType = DbTypeDetector.detect(connection);
        ModificationListApplier modificationListApplier = createModificationListApplier(dbType);
        modificationListApplier.apply(connection, modificationList);

        return null;
      }
    });
  }
}
