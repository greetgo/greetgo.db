package kz.greetgo.db;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class GreetgoTransactionManager implements TransactionManager {

  private ExceptionCatcher exceptionCatcher = new ExceptionCatcher() {
    @Override
    public void catchException(Throwable e) {
      e.printStackTrace();
    }
  };
  private boolean exceptionCatcherUsed = false;

  public void setExceptionCatcher(ExceptionCatcher exceptionCatcher) {
    if (exceptionCatcher == null) throw new IllegalArgumentException("exceptionCatcher == null");
    if (exceptionCatcherUsed) throw new RuntimeException("exceptionCatcher already used");
    this.exceptionCatcher = exceptionCatcher;
  }

  private final ThreadLocal<ThreadLocalTM> localTMStore = new ThreadLocal<>();

  @Override
  public Connection getConnection(DataSource dataSource) {
    final ThreadLocalTM localTM = localTMStore.get();
    if (localTM == null) try {
      return dataSource.getConnection();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return localTM.getConnection(dataSource);
  }

  @Override
  public void upLevel(CallMeta callMeta) {
    ThreadLocalTM threadTM = localTMStore.get();
    if (threadTM == null) {
      threadTM = new ThreadLocalTM(exceptionCatcher);
      exceptionCatcherUsed = true;
      localTMStore.set(threadTM);
    }

    threadTM.upLevel(callMeta);
  }

  @Override
  public void downLevel(Throwable throwable) {
    localTMStore.get().downLevel(throwable);
  }

  @Override
  public void closeConnection(DataSource dataSource, Connection connection) {
    final ThreadLocalTM localTM = localTMStore.get();
    if (localTM == null) try {
      connection.close();
      return;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    localTM.closeConnection(dataSource, connection);
  }


}
