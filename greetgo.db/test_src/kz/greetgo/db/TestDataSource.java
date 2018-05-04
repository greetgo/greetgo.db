package kz.greetgo.db;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static java.util.Collections.synchronizedList;
import static org.fest.assertions.api.Assertions.assertThat;

public class TestDataSource implements DataSource {

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
    throw new UnsupportedOperationException();
  }

  @Override
  public PrintWriter getLogWriter() throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setLogWriter(PrintWriter out) throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setLoginTimeout(int seconds) throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getLoginTimeout() throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    throw new UnsupportedOperationException();
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    throw new UnsupportedOperationException();
  }

}
