package kz.greetgo.db.nf36.core;

import java.util.Date;
import java.util.function.Consumer;

public interface Nf36HistorySelector {
  Nf36HistorySelector field(String dbFieldName, String aliasName);

  void peekSql(Consumer<String> sqlConsumer);

  void putTo(Object destinationObject);

  void addId(String idName);

  void addIdAlias(String idName, String idAlias);

  void at(Date date);
}
