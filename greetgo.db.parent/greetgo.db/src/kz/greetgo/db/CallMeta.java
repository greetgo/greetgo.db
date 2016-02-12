package kz.greetgo.db;

public final class CallMeta {
  private final CallMeta parent;
  private final IsolationLevel isolationLevel;
  private final Class<? extends Throwable>[] commitExceptions;

  public CallMeta(CallMeta parent, IsolationLevel isolationLevel, Class<? extends Throwable>[] commitExceptions) {
    this.parent = parent;
    this.isolationLevel = isolationLevel;
    this.commitExceptions = commitExceptions;
  }

  public boolean isTransactional() {
    return getIsolationLevel() != null;
  }

  private IsolationLevel cachedIsolationLevel;
  private boolean isIsolationLevelCached = false;

  public IsolationLevel getIsolationLevel() {
    if (isIsolationLevelCached) return cachedIsolationLevel;
    isIsolationLevelCached = true;
    if (isolationLevel != null) return cachedIsolationLevel = isolationLevel;
    if (parent == null) return cachedIsolationLevel = null;
    return cachedIsolationLevel = parent.getIsolationLevel();
  }

  public boolean needToCommit(Throwable throwable) {

    if (commitExceptions != null) for (Class<? extends Throwable> commitException : commitExceptions) {
      if (commitException.isInstance(throwable)) return true;
    }

    //noinspection SimplifiableIfStatement
    if (parent != null) return parent.needToCommit(throwable);

    return false;
  }
}
