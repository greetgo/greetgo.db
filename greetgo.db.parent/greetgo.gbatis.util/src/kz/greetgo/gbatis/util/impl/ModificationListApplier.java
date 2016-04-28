package kz.greetgo.gbatis.util.impl;

import java.sql.Connection;
import java.util.List;

public interface ModificationListApplier {
  void apply(Connection connection, List<TableModification> tableModificationList) throws Exception;
}
