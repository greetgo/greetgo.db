package kz.greetgo.db;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class ThreadLocalTransactionManager {

  private final ExceptionCatcherGetter exceptionCatcherGetter;

  ThreadLocalTransactionManager(ExceptionCatcherGetter exceptionCatcherGetter) {
    if (exceptionCatcherGetter == null) throw new NullPointerException("exceptionCatcherGetter == null");
    this.exceptionCatcherGetter = exceptionCatcherGetter;
  }

  static class CallMetaStack {
    private final List<CallMeta> stack = new ArrayList<>();

    public void add(CallMeta callMeta) {
      stack.add(callMeta);
    }

    public int size() {
      return stack.size();
    }

    public CallMeta pop() {
      return stack.remove(stack.size() - 1);
    }

    private CallMeta firstTransactional() {
      for (CallMeta callMeta : stack) {
        if (callMeta.isTransactional()) return callMeta;
      }
      return null;
    }

    public void prepareNewConnection(Connection c) throws SQLException {
      CallMeta callMeta = firstTransactional();
      if (callMeta == null) return;
      callMeta.applyIsolationLevel(c);
      c.setAutoCommit(false);
    }

    public boolean isTransactional() {
      return firstTransactional() != null;
    }
  }

  private final Map<DataSource, DataSourceDot> dataSourceDots = new HashMap<>();

  private DataSourceDot getDot(DataSource dataSource) {
    DataSourceDot dot = dataSourceDots.get(dataSource);
    if (dot == null) {
      dot = new DataSourceDot(dataSource);
      dataSourceDots.put(dataSource, dot);
    }
    return dot;
  }

  public Connection getConnection(DataSource dataSource) throws SQLException {
    return getDot(dataSource).getConnection();
  }

  public void closeConnection(DataSource dataSource, Connection connection) throws SQLException {
    getDot(dataSource).closeConnection(connection);
  }

  public void upLevel(CallMeta callMeta) {
    callMetaStack.add(callMeta);
  }

  public void downLevel(Throwable throwable) {
    if (callMetaStack.size() == 0) throw new RuntimeException("downLevel more then upLevels");
    final CallMeta lastCallMeta = callMetaStack.pop();

    for (DataSourceDot dot : dataSourceDots.values()) {
      dot.downLevel(lastCallMeta, throwable);
    }
  }

  private final CallMetaStack callMetaStack = new CallMetaStack();

  private class DataSourceDot {
    final DataSource dataSource;

    DataSourceDot(DataSource dataSource) {
      this.dataSource = dataSource;
    }

    final LinkedList<Connection> freeConnections = new LinkedList<>();
    final LinkedList<Connection> busyConnections = new LinkedList<>();

    Connection getConnection() throws SQLException {
      if (!callMetaStack.isTransactional()) {
        return dataSource.getConnection();
      }
      if (freeConnections.size() > 0) {
        Connection c = freeConnections.pop();
        busyConnections.add(c);
        return c;
      }
      {
        Connection c = dataSource.getConnection();
        callMetaStack.prepareNewConnection(c);
        busyConnections.add(c);
        return c;
      }
    }


    void closeConnection(Connection connection) throws SQLException {
      if (!callMetaStack.isTransactional()) {
        if (!connection.isClosed()) connection.close();
        return;
      }

      if (!busyConnections.contains(connection)) {
        if (!connection.isClosed()) connection.close();
        return;
      }

      busyConnections.remove(connection);
      if (!connection.isClosed()) freeConnections.add(connection);
    }

    public void downLevel(CallMeta lastCallMeta, Throwable throwable) {
      if (callMetaStack.isTransactional()) return;
      if (!lastCallMeta.isTransactional()) return;

      busyConnections.clear();

      boolean toCommit = throwable == null || lastCallMeta.needToCommit(throwable);

      for (Connection connection : freeConnections) {

        try {
          if (connection.isClosed()) continue;
        } catch (SQLException e) {
          exceptionCatcherGetter.get().catchException(e);
        }

        try {
          if (toCommit) {
            connection.commit();
          } else {
            connection.rollback();
          }
        } catch (SQLException e) {
          exceptionCatcherGetter.get().catchException(e);
        }

        try {
          if (!connection.getAutoCommit()) connection.setAutoCommit(true);
        } catch (SQLException e) {
          exceptionCatcherGetter.get().catchException(e);
        }

        try {
          connection.close();
        } catch (SQLException e) {
          exceptionCatcherGetter.get().catchException(e);
        }
      }

      freeConnections.clear();
    }

  }
}
