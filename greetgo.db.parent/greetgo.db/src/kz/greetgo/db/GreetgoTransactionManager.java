package kz.greetgo.db;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class GreetgoTransactionManager implements TransactionManager {

  private static final ExceptionCatcher DEFAULT_EXCEPTION_CATCHER = new ExceptionCatcher() {
    @Override
    public void catchException(Throwable e) {
      e.printStackTrace();
    }
  };

  private ExceptionCatcher exceptionCatcher = DEFAULT_EXCEPTION_CATCHER;

  private final ExceptionCatcherGetter exceptionCatcherGetter = new ExceptionCatcherGetter() {
    @Override
    public ExceptionCatcher get() {
      return exceptionCatcher;
    }
  };

  public void setExceptionCatcher(ExceptionCatcher exceptionCatcher) {
    this.exceptionCatcher = exceptionCatcher == null ? DEFAULT_EXCEPTION_CATCHER : exceptionCatcher;
  }

  private final ThreadLocal<ThreadLocalTransactionManager> threadLocalTM
    = new ThreadLocal<ThreadLocalTransactionManager>() {
    @Override
    protected ThreadLocalTransactionManager initialValue() {
      return new ThreadLocalTransactionManager(exceptionCatcherGetter);
    }
  };

  @Override
  public Connection getConnection(DataSource dataSource) {
    try {
      return threadLocalTM.get().getConnection(dataSource);
    } catch (SQLException e) {
      throw new SQLRuntimeException(e);
    }
  }

  @Override
  public void upLevel(CallMeta callMeta) {
    threadLocalTM.get().upLevel(callMeta);
  }

  @Override
  public void downLevel(Throwable throwable) {
    threadLocalTM.get().downLevel(throwable);
  }

  @Override
  public void closeConnection(DataSource dataSource, Connection connection) {
    try {
      threadLocalTM.get().closeConnection(dataSource, connection);
    } catch (SQLException e) {
      throw new SQLRuntimeException(e);
    }
  }
}
