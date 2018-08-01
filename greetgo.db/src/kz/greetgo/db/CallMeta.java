package kz.greetgo.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

public final class CallMeta {
  private final CallMeta parent;
  private final IsolationLevel isolationLevel;
  private final Class<? extends Throwable>[] commitExceptions;

  public CallMeta(CallMeta parent, IsolationLevel isolationLevel, Class<? extends Throwable>[] commitExceptions) {
    this.parent = parent;
    this.isolationLevel = isolationLevel;
    this.commitExceptions = commitExceptions;
  }

  public void applyIsolationLevel(Connection connection)
    throws SQLException {
    if (isolationLevel == null) return;
    switch (isolationLevel) {
      case DEFAULT:
        return;

      case READ_COMMITTED:
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        return;

      case READ_UNCOMMITTED:
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        return;

      case REPEATABLE_READ:
        connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
        return;

      case SERIALIZABLE:
        connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        return;

      default:
        throw new IllegalArgumentException("Unknown value of isolationLevel = " + isolationLevel);
    }
  }

  public boolean isTransactional() {
    return getIsolationLevel() != null;
  }

  private static class IsolationLevelCache {
    final IsolationLevel isolationLevel;

    private IsolationLevelCache(IsolationLevel isolationLevel) {this.isolationLevel = isolationLevel;}
  }

  private final AtomicReference<IsolationLevelCache> isolationLevelCache = new AtomicReference<>(null);

  public IsolationLevel getIsolationLevel() {

    IsolationLevelCache cache = isolationLevelCache.get();
    if (cache != null) return cache.isolationLevel;

    IsolationLevel isolationLevel = calcIsolationLevel();
    isolationLevelCache.set(new IsolationLevelCache(isolationLevel));

    return isolationLevel;
  }

  private IsolationLevel calcIsolationLevel() {
    if (isolationLevel != null) return isolationLevel;
    if (parent == null) return null;
    return parent.getIsolationLevel();
  }

  public boolean needToCommit(Throwable throwable) {

    if (commitExceptions != null) for (Class<? extends Throwable> commitException : commitExceptions) {
      if (commitException.isInstance(throwable)) return true;
    }

    if (parent != null) return parent.needToCommit(throwable);

    return false;
  }
}
