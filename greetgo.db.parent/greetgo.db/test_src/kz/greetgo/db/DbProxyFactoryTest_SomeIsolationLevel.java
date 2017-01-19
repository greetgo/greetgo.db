package kz.greetgo.db;

import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static kz.greetgo.db.TestConnection.extractTestConnection;
import static org.fest.assertions.api.Assertions.assertThat;

public class DbProxyFactoryTest_SomeIsolationLevel {

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

      assertThat(connection.getAutoCommit()).isFalse();
      TestConnection testConnection = extractTestConnection(connection);
      if (testConnection == null) throw new NullPointerException();
      String transactionIsolation = testConnection.transactionIsolationStr();
      assertThat(transactionIsolation).isEqualTo("READ_COMMITTED");
      assertThat(testConnection.events).hasSize(2);
      assertThat(testConnection.events.get(0)).isEqualTo("SET TransactionIsolation TO READ_COMMITTED");
      assertThat(testConnection.events.get(1)).isEqualTo("SET AutoCommit TO false");

      transactionManager.closeConnection(dataSource, connection);

      assertThat(testConnection.events).hasSize(2);
      assertThat(testConnection.events.get(0)).isEqualTo("SET TransactionIsolation TO READ_COMMITTED");
      assertThat(testConnection.events.get(1)).isEqualTo("SET AutoCommit TO false");

      return testConnection;
    }

    @Override
    @InTransaction(IsolationLevel.READ_COMMITTED)
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
    Iface proxy = dbProxyFactory.createProxyFor(original, Iface.class);

    proxy.method();

    assertThat(original.testConnection1.events).hasSize(5);
    assertThat(original.testConnection1.events.get(0)).isEqualTo("SET TransactionIsolation TO READ_COMMITTED");
    assertThat(original.testConnection1.events.get(1)).isEqualTo("SET AutoCommit TO false");
    assertThat(original.testConnection1.events.get(2)).isEqualTo("COMMIT");
    assertThat(original.testConnection1.events.get(3)).isEqualTo("SET AutoCommit TO true");
    assertThat(original.testConnection1.events.get(4)).isEqualTo("CLOSE");

    assertThat(original.testConnection1.name).isEqualTo(original.testConnection2.name);
  }
}
