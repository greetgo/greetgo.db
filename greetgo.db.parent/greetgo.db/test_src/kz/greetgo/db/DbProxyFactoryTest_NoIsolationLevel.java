package kz.greetgo.db;

import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static kz.greetgo.db.TestConnection.extractTestConnection;
import static org.fest.assertions.api.Assertions.assertThat;

public class DbProxyFactoryTest_NoIsolationLevel {

  interface Iface {
    void method() throws SQLException;
  }

  class Impl implements Iface {
    private final TestDataSource dataSource;
    private final GreetgoTransactionManager transactionManager;

    public Impl(TestDataSource dataSource, GreetgoTransactionManager transactionManager) {
      this.dataSource = dataSource;
      this.transactionManager = transactionManager;
    }

    TestConnection testConnection1, testConnection2;

    TestConnection checkConnection() throws SQLException {
      Connection connection = transactionManager.getConnection(dataSource);

      assertThat(connection.getAutoCommit()).isTrue();
      TestConnection testConnection = extractTestConnection(connection);
      if (testConnection == null) throw new NullPointerException();
      String transactionIsolation = testConnection.transactionIsolationStr();
      assertThat(transactionIsolation).isEqualTo("NO_ISOLATION_LEVEL");
      assertThat(testConnection.events).isEmpty();

      transactionManager.closeConnection(dataSource, connection);

      assertThat(testConnection.events).hasSize(1);
      assertThat(testConnection.events.get(0)).isEqualTo("CLOSE");

      return testConnection;
    }

    @Override
    public void method() throws SQLException {
      testConnection1 = checkConnection();
      testConnection2 = checkConnection();
    }
  }

  @Test
  public void test() throws Exception {
    GreetgoTransactionManager transactionManager = new GreetgoTransactionManager();
    TestDataSource dataSource = new TestDataSource();
    DbProxyFactory dbProxyFactory = new DbProxyFactory(transactionManager);

    Impl original = new Impl(dataSource, transactionManager);
    Iface proxy = (Iface) dbProxyFactory.createProxyFor(original, Iface.class);

    proxy.method();

    assertThat(original.testConnection1.events).hasSize(1);
    assertThat(original.testConnection1.events.get(0)).isEqualTo("CLOSE");

    assertThat(original.testConnection2.events).hasSize(1);
    assertThat(original.testConnection2.events.get(0)).isEqualTo("CLOSE");

    assertThat(original.testConnection1.name).isNotEqualTo(original.testConnection2.name);
  }
}
