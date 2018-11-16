package kz.greetgo.db.nf36.core;

import java.util.Date;
import java.util.function.Consumer;

public interface Nf36HistorySelector {
  Nf36HistorySelector setTimeFieldName(String timeFieldName);

  Nf36HistorySelector setInsertedAtFieldName(String insertedAtFieldName);

  Nf36HistorySelector setNf3TableName(String nf3TableName);

  Nf36HistorySelector field(String nf6TableName, String dbFieldName, String aliasName, String authorFieldName);

  Nf36HistorySelector onAbsent(Consumer<Object> destinationObjectConsumer);

  boolean putTo(Object destinationObject);

  Nf36HistorySelector addId(String idName);

  void addIdAlias(String idName, String idAlias);

  Nf36HistorySelector at(Date date);
}
