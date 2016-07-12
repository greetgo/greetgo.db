package kz.greetgo.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

class ThreadLocalTM {
  
  enum CloseType {
    SIMPLE_CLOSE, COMMIT_ON_CLOSE, ROLLBACK_ON_CLOSE
  }
  
  private final ExceptionCatcher exceptionCatcher;
  
  ThreadLocalTM(ExceptionCatcher exceptionCatcher) {
    this.exceptionCatcher = exceptionCatcher;
  }
  
  private static int dataSourceKey(DataSource dataSource) {
    return System.identityHashCode(dataSource);
  }
  
  private final List<CallMeta> callMetaStack = new ArrayList<>();
  
  private final Map<Integer, DataSourceDot> dataSourceDots = new HashMap<>();
  
  private DataSourceDot getDot(DataSource dataSource) {
    final int key = dataSourceKey(dataSource);
    DataSourceDot ret = dataSourceDots.get(key);
    if (ret == null) {
      ret = new DataSourceDot(dataSource);
      dataSourceDots.put(key, ret);
    }
    return ret;
  }
  
  public Connection getConnection(DataSource dataSource) {
    if (callMetaStack.size() == 0) try {
      return dataSource.getConnection();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return getDot(dataSource).getConnection();
  }
  
  public void upLevel(CallMeta callMeta) {
    callMetaStack.add(callMeta);
  }
  
  public void downLevel(Throwable throwable) {
    final CallMeta lastCallMeta = callMetaStack.remove(callMetaStack.size() - 1);
    
    CloseType closeType = CloseType.SIMPLE_CLOSE;
    
    if (noAnyTransactional() && lastCallMeta.isTransactional()) {
      if (throwable == null) {
        closeType = CloseType.COMMIT_ON_CLOSE;
      } else {
        closeType = lastCallMeta.needToCommit(throwable) ? CloseType.COMMIT_ON_CLOSE
            :CloseType.ROLLBACK_ON_CLOSE;
      }
    }
    
    closeAllConnections(closeType);
  }
  
  private void closeAllConnections(CloseType closeType) {
    for (DataSourceDot dot : dataSourceDots.values()) {
      dot.closeAllConnections(closeType);
    }
  }
  
  private CallMeta getLastTransactional() {
    final List<CallMeta> callMetaStack = this.callMetaStack;
    for (int i = callMetaStack.size(); i-- > 0;) {
      final CallMeta callMeta = callMetaStack.get(i);
      if (callMeta.isTransactional()) return callMeta;
    }
    
    return null;
  }
  
  private boolean noAnyTransactional() {
    return getLastTransactional() == null;
  }
  
  public void closeConnection(DataSource dataSource, Connection connection) {
    if (noAnyTransactional()) {
      doConnectionClose(connection, null, null);
      return;
    }
    
    getDot(dataSource).closeConnection(connection);
  }
  
  private class DataSourceDot {
    final DataSource dataSource;
    final List<Connection> connections = new ArrayList<>();
    int busyCount = 0;
    
    DataSourceDot(DataSource dataSource) {
      this.dataSource = dataSource;
    }
    
    Connection getConnection() {
      if (noAnyTransactional()) {
        try {
          return dataSource.getConnection();
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
      }
      
      if (busyCount < connections.size()) return connections.get(busyCount++);
      final Connection connection = createConnection();
      connections.add(connection);
      busyCount = connections.size();
      return connection;
    }
    
    private Connection createConnection() {
      try {
        final Connection connection = dataSource.getConnection();
        final CallMeta lastCallMeta = getLastTransactional();
        if (lastCallMeta == null) throw new RuntimeException("lastCallMeta == null");
        
        connection.setAutoCommit(false);
        applyIsolationLevel(connection, lastCallMeta.getIsolationLevel());
        
        return connection;
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }
    
    void closeAllConnections(CloseType closeType) {
      List<Throwable> throwList = new ArrayList<>();
      
      if (noAnyTransactional()) {
        for (Connection connection : connections) {
          doConnectionClose(connection, closeType, throwList);
        }
        connections.clear();
      }
      
      busyCount = 0;
      
      if (throwList.size() > 0) {
        final Throwable t = throwList.get(0);
        if (t instanceof RuntimeException) throw (RuntimeException)t;
        throw new RuntimeException(t);
      }
    }
    
    void closeConnection(Connection connection) {
      if (noAnyTransactional()) {
        doConnectionClose(connection, null, null);
        return;
      }
      
      busyCount = freeConnection(connections, connection, busyCount);
      if (busyCount < 0) {
        busyCount = 0;
        doConnectionClose(connection, null, null);
      }
      
    }
    
  }
  
  private void doConnectionClose(Connection connection, CloseType closeType,
      List<Throwable> throwList) {
    try {
      
      if (connection.isClosed()) return;
      
      if (closeType != null) {
        if (closeType == CloseType.COMMIT_ON_CLOSE) {
          connection.commit();
        }
        if (closeType == CloseType.ROLLBACK_ON_CLOSE) {
          connection.rollback();
        }
      }
    } catch (Throwable e) {
      if (throwList == null) {
        exceptionCatcher.catchException(e);
      } else {
        throwList.add(e);
      }
    }
    
    try {
      if (connection.isClosed()) return;
      
      if (!connection.getAutoCommit()) connection.setAutoCommit(true);
      connection.close();
    } catch (SQLException e) {
      exceptionCatcher.catchException(e);
    }
  }
  
  static int freeConnection(List<Connection> connections, Connection connection, int busyCount) {
    int found = -1;
    for (int i = 0, n = connections.size(); i < n; i++) {
      if (connection == connections.get(i)) {
        found = i;
        break;
      }
    }
    
    if (found < 0) return -1;
    
    if (found == busyCount - 1) return found;
    
    if (found >= busyCount) return busyCount;
    
    for (int i = found, n = busyCount - 1; i < n; i++) {
      connections.set(i, connections.get(i + 1));
    }
    
    busyCount--;
    
    connections.set(busyCount, connection);
    
    return busyCount;
  }
  
  private static void applyIsolationLevel(Connection connection, IsolationLevel isolationLevel)
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
}
