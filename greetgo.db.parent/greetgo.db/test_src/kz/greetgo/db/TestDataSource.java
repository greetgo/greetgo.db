package kz.greetgo.db;

import kz.greetgo.util.RND;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static java.util.Collections.synchronizedList;

public class TestDataSource implements DataSource {

  public static TestConnection extractTestConnection(Connection connection) {
    return (TestConnection) Proxy.getInvocationHandler(connection);
  }

  public class TestConnection implements InvocationHandler {

    public final String name = "TestConnection(" + RND.str(10) + ")";

    public int closeCallCount = 0;
    public int setAutoCommitCallCount = 0;
    public boolean autoCommit = true;
    public int transactionIsolation;
    public int setTransactionIsolationCallCount = 0;

    public int commitCallCount = 0, rollbackCallCount = 0;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

      final String methodName = method.getName();

      if (method.getParameterTypes().length == 0 && methodName.equals("close")) {
        closeCallCount++;
        return null;
      }

      if (method.getParameterTypes().length == 1 && methodName.equals("equals")) {
        return System.identityHashCode(proxy) == System.identityHashCode(args[0]);
      }

      if (method.getParameterTypes().length == 1 && methodName.equals("setAutoCommit")) {
        autoCommit = (boolean) args[0];
        setAutoCommitCallCount++;
        return null;
      }

      if (method.getParameterTypes().length == 1 && methodName.equals("setTransactionIsolation")) {
        transactionIsolation = (int) args[0];
        setTransactionIsolationCallCount++;
        return null;
      }

      if (method.getParameterTypes().length == 0 && methodName.equals("getSchema")) {
        return this.name;
      }

      if (method.getParameterTypes().length == 0 && methodName.equals("getAutoCommit")) {
        return autoCommit;
      }

      if (method.getParameterTypes().length == 0 && methodName.equals("getTransactionIsolation")) {
        return transactionIsolation;
      }

      if (method.getParameterTypes().length == 0 && methodName.equals("isClosed")) {
        return closeCallCount > 0;
      }

      if (method.getParameterTypes().length == 0 && methodName.equals("commit")) {
        commitCallCount++;
        return null;
      }

      if (method.getParameterTypes().length == 0 && methodName.equals("rollback")) {
        rollbackCallCount++;
        return null;
      }

      throw new RuntimeException("Cannot invoke method " + method);
    }
  }

  public final List<Connection> gotConnections = synchronizedList(new ArrayList<Connection>());

  public Connection lastGotConnection() {
    return gotConnections.get(gotConnections.size() - 1);
  }

  @Override
  public Connection getConnection() throws SQLException {
    return saveConnection((Connection) Proxy.newProxyInstance(
      getClass().getClassLoader(),
      new Class<?>[]{Connection.class},
      new TestConnection()
    ));
  }

  private Connection saveConnection(Connection connection) {
    gotConnections.add(connection);
    return connection;
  }

  @Override
  public Connection getConnection(String username, String password) throws SQLException {
    throw new RuntimeException();
  }

  @Override
  public PrintWriter getLogWriter() throws SQLException {
    throw new RuntimeException();
  }

  @Override
  public void setLogWriter(PrintWriter out) throws SQLException {
    throw new RuntimeException();
  }

  @Override
  public void setLoginTimeout(int seconds) throws SQLException {
    throw new RuntimeException();
  }

  @Override
  public int getLoginTimeout() throws SQLException {
    throw new RuntimeException();
  }

  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    throw new RuntimeException();
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    throw new RuntimeException();
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    throw new RuntimeException();
  }
}
