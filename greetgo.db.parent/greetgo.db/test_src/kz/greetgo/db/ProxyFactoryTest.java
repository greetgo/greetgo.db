package kz.greetgo.db;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static kz.greetgo.db.TestDataSource.extractTestConnection;
import static org.fest.assertions.api.Assertions.assertThat;

public class ProxyFactoryTest {

  private ProxyFactory proxyFactory;
  private TransactionManager transactionManager;

  @BeforeMethod
  public void setup() {
    transactionManager = new GreetgoTransactionManager();
    proxyFactory = new ProxyFactory(transactionManager);
  }

  class IsolationLevelTester implements HasMethodsInTransaction {

    private TestDataSource ds = new TestDataSource();

    Connection noIsolationLevel_connection1 = null;
    Connection noIsolationLevel_connection2 = null;

    public void noIsolationLevel() {
      {
        final Connection connection = transactionManager.getConnection(ds);
        transactionManager.closeConnection(ds, connection);

        noIsolationLevel_connection1 = connection;
      }
      {
        final Connection connection = transactionManager.getConnection(ds);
        transactionManager.closeConnection(ds, connection);

        noIsolationLevel_connection2 = connection;
      }
    }

    Connection isolationLevel_default_connection1 = null;
    Connection isolationLevel_default_connection2 = null;

    @InTransaction
    public void isolationLevel_default() {
      {
        final Connection connection = transactionManager.getConnection(ds);
        transactionManager.closeConnection(ds, connection);

        isolationLevel_default_connection1 = connection;
      }
      {
        final Connection connection = transactionManager.getConnection(ds);
        transactionManager.closeConnection(ds, connection);

        isolationLevel_default_connection2 = connection;
      }
    }

    Connection isolationLevel_SERIALIZABLE_connection;

    @InTransaction(IsolationLevel.SERIALIZABLE)
    public void isolationLevel_SERIALIZABLE() {
      final Connection connection = transactionManager.getConnection(ds);
      transactionManager.closeConnection(ds, connection);

      isolationLevel_SERIALIZABLE_connection = connection;
    }

  }

  private void assertNotSame(Connection connection1, Connection connection2) throws SQLException {
    assertThat(connection1.getSchema()).isNotEqualTo(connection2.getSchema());
  }

  private void assertSame(Connection connection1, Connection connection2) throws SQLException {
    assertThat(connection1.getSchema()).isEqualTo(connection2.getSchema());
  }

  @Test
  public void createProxyFor_noIsolationLevel() throws Exception {
    IsolationLevelTester original = new IsolationLevelTester();

    //
    //
    final IsolationLevelTester proxied = (IsolationLevelTester) proxyFactory.createProxyFor(original);
    //
    //

    proxied.noIsolationLevel();

    assertThat(original.noIsolationLevel_connection1).isNotNull();
    assertThat(original.noIsolationLevel_connection2).isNotNull();

    assertNotSame(original.noIsolationLevel_connection1, original.noIsolationLevel_connection2);

    final TestDataSource.TestConnection tc1 = extractTestConnection(original.noIsolationLevel_connection1);
    assertThat(tc1.autoCommit).isFalse();
    assertThat(tc1.setTransactionIsolationCallCount).isZero();

    final TestDataSource.TestConnection tc2 = extractTestConnection(original.noIsolationLevel_connection2);
    assertThat(tc2.autoCommit).isFalse();
    assertThat(tc2.setTransactionIsolationCallCount).isZero();

  }

  @Test
  public void createProxyFor_isolationLevel_default() throws Exception {
    IsolationLevelTester original = new IsolationLevelTester();

    final IsolationLevelTester proxied = (IsolationLevelTester) proxyFactory.createProxyFor(original);

    proxied.isolationLevel_default();

    assertThat(original.isolationLevel_default_connection1).isNotNull();
    assertThat(original.isolationLevel_default_connection2).isNotNull();

    assertSame(original.isolationLevel_default_connection1, original.isolationLevel_default_connection2);

    final TestDataSource.TestConnection tc1 = extractTestConnection(original.isolationLevel_default_connection1);
    assertThat(tc1.autoCommit).isTrue();
    assertThat(tc1.setTransactionIsolationCallCount).isZero();

  }

  @Test
  public void createProxyFor_isolationLevel_SERIALIZABLE() throws Exception {
    IsolationLevelTester original = new IsolationLevelTester();

    final IsolationLevelTester proxied = (IsolationLevelTester) proxyFactory.createProxyFor(original);

    proxied.isolationLevel_SERIALIZABLE();

    assertThat(original.isolationLevel_SERIALIZABLE_connection).isNotNull();

    final TestDataSource.TestConnection tc = extractTestConnection(original.isolationLevel_SERIALIZABLE_connection);
    assertThat(tc.autoCommit).isTrue();
    assertThat(tc.setTransactionIsolationCallCount).isEqualTo(1);
    assertThat(tc.transactionIsolation).isEqualTo(Connection.TRANSACTION_SERIALIZABLE);

  }

}
