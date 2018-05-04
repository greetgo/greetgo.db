package kz.greetgo.gbatis.util.impl;

import kz.greetgo.db.Jdbc;
import kz.greetgo.gbatis.futurecall.SqlViewer;
import kz.greetgo.gbatis.util.callbacks.meta.ColInfoCallback;
import kz.greetgo.gbatis.util.callbacks.meta.KeyNamesCallback;
import kz.greetgo.gbatis.util.model.ColInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class DbInfo {

  public abstract Jdbc jdbc();

  public abstract SqlViewer sqlViewer();

  private final Map<String, List<ColInfo>> colInfoCache = new ConcurrentHashMap<>();

  public List<ColInfo> getColInfo(String tableName) {
    {
      List<ColInfo> ret = colInfoCache.get(tableName);
      if (ret != null) return ret;
    }
    {
      List<ColInfo> ret = jdbc().execute(new ColInfoCallback(sqlViewer(), tableName));
      colInfoCache.put(tableName, ret);
      return ret;
    }
  }

  private final Map<String, List<String>> keyNameCache = new ConcurrentHashMap<>();

  public List<String> getKeyNames(String tableName) {
    {
      List<String> ret = keyNameCache.get(tableName);
      if (ret != null) return ret;
    }
    {
      List<String> ret = jdbc().execute(new KeyNamesCallback(sqlViewer(), tableName));
      keyNameCache.put(tableName, ret);
      return ret;
    }
  }
}
