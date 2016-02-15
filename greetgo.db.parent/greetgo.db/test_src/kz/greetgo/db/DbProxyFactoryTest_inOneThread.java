package kz.greetgo.db;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static kz.greetgo.db.TestDataSource.extractTestConnection;
import static org.fest.assertions.api.Assertions.assertThat;

public class DbProxyFactoryTest_inOneThread {

  private TestingController original, proxied;

  @BeforeMethod
  public void setup() {
    TestExceptionCatcher exceptionCatcher = new TestExceptionCatcher();
    GreetgoTransactionManager transactionManager = new GreetgoTransactionManager();
    transactionManager.setExceptionCatcher(exceptionCatcher);
    DbProxyFactory dbProxyFactory = new DbProxyFactory(transactionManager);

    original = new TestingController();
    original.transactionManager = transactionManager;
    proxied = (TestingController) dbProxyFactory.createProxyFor(original);
  }

  public static class TestingController {

    TransactionManager transactionManager;

    private TestDataSource ds = new TestDataSource();

    TestDataSource.TestConnection noIsolationLevel_tc1 = null;
    TestDataSource.TestConnection noIsolationLevel_tc2 = null;

    public void noIsolationLevel() throws SQLException {

      final Connection connection1 = transactionManager.getConnection(ds);
      assertThat(connection1.getAutoCommit()).isTrue();
      connection1.prepareStatement("select 1 from dual");
      transactionManager.closeConnection(ds, connection1);

      final Connection connection2 = transactionManager.getConnection(ds);
      assertThat(connection2.getAutoCommit()).isTrue();
      connection2.prepareStatement("select 2 from dual");
      transactionManager.closeConnection(ds, connection2);

      assertNotSame(connection1, connection2);

      noIsolationLevel_tc1 = extractTestConnection(connection1);
      noIsolationLevel_tc2 = extractTestConnection(connection2);
    }

    TestDataSource.TestConnection isolationLevel_default_tc = null;

    @InTransaction
    public void isolationLevel_default() throws Exception {
      final Connection connection1 = transactionManager.getConnection(ds);
      assertThat(connection1.getAutoCommit()).isFalse();
      connection1.prepareStatement("select 1 from dual");
      transactionManager.closeConnection(ds, connection1);

      final Connection connection2 = transactionManager.getConnection(ds);
      assertThat(connection2.getAutoCommit()).isFalse();
      connection2.prepareStatement("select 2 from dual");
      transactionManager.closeConnection(ds, connection2);

      assertSame(connection1, connection2);

      isolationLevel_default_tc = extractTestConnection(connection1);
    }

    TestDataSource.TestConnection isolationLevel_SERIALIZABLE_tc = null;

    @InTransaction(IsolationLevel.SERIALIZABLE)
    public void isolationLevel_SERIALIZABLE() throws SQLException {
      final Connection connection = transactionManager.getConnection(ds);
      assertThat(connection.getAutoCommit()).isFalse();
      connection.prepareStatement("select 1 from dual");
      transactionManager.closeConnection(ds, connection);

      isolationLevel_SERIALIZABLE_tc = extractTestConnection(connection);
    }

    TestDataSource.TestConnection isolationLevel_REPEATABLE_READ_tc = null;

    @InTransaction(IsolationLevel.REPEATABLE_READ)
    public void isolationLevel_REPEATABLE_READ() throws SQLException {
      final Connection connection = transactionManager.getConnection(ds);
      assertThat(connection.getAutoCommit()).isFalse();
      connection.prepareStatement("select 1 from dual");
      transactionManager.closeConnection(ds, connection);

      isolationLevel_REPEATABLE_READ_tc = extractTestConnection(connection);
    }

    TestDataSource.TestConnection isolationLevel_READ_UNCOMMITTED_tc = null;

    @InTransaction(IsolationLevel.READ_UNCOMMITTED)
    public void isolationLevel_READ_UNCOMMITTED() throws SQLException {
      final Connection connection = transactionManager.getConnection(ds);
      assertThat(connection.getAutoCommit()).isFalse();
      connection.prepareStatement("select 1 from dual");
      transactionManager.closeConnection(ds, connection);

      isolationLevel_READ_UNCOMMITTED_tc = extractTestConnection(connection);
    }

    TestDataSource.TestConnection isolationLevel_READ_COMMITTED_tc = null;

    @InTransaction(IsolationLevel.READ_COMMITTED)
    public void isolationLevel_READ_COMMITTED() throws SQLException {
      final Connection connection = transactionManager.getConnection(ds);
      assertThat(connection.getAutoCommit()).isFalse();
      connection.prepareStatement("select 1 from dual");
      transactionManager.closeConnection(ds, connection);

      isolationLevel_READ_COMMITTED_tc = extractTestConnection(connection);
    }


    TestDataSource.TestConnection throwsTestExceptionAndRollback_tc = null;

    @InTransaction
    public void throwsTestExceptionAndRollback() throws TestException, SQLException {

      final Connection connection = transactionManager.getConnection(ds);
      connection.prepareStatement("select 1 from dual");
      transactionManager.closeConnection(ds, connection);

      throwsTestExceptionAndRollback_tc = extractTestConnection(connection);

      throw new TestException();
    }

    TestDataSource.TestConnection throwsTestExceptionAndCommit_tc = null;

    @InTransaction
    @CommitOn(TestException.class)
    public void throwsTestExceptionAndCommit() throws TestException, SQLException {

      final Connection connection = transactionManager.getConnection(ds);
      connection.prepareStatement("select 1 from dual");
      transactionManager.closeConnection(ds, connection);

      throwsTestExceptionAndCommit_tc = extractTestConnection(connection);

      throw new TestException();
    }

  }

  static class TestException extends Exception {
  }

  private static void assertNotSame(Connection connection1, Connection connection2) throws SQLException {
    assertThat(connection1.getSchema()).isNotEqualTo(connection2.getSchema());
  }

  private static void assertSame(Connection connection1, Connection connection2) throws SQLException {
    assertThat(connection1.getSchema()).isEqualTo(connection2.getSchema());
  }

  @Test
  public void noIsolationLevel() throws Exception {

    proxied.noIsolationLevel();

    assertThat(original.noIsolationLevel_tc1.events).containsExactly(
      "CALL prepareStatement(select 1 from dual)",
      "CLOSE"
    );

    assertThat(original.noIsolationLevel_tc2.events).containsExactly(
      "CALL prepareStatement(select 2 from dual)",
      "CLOSE"
    );

  }

  @Test
  public void isolationLevel_default() throws Exception {

    proxied.isolationLevel_default();

    assertThat(original.isolationLevel_default_tc.events).containsExactly(
      "SET AutoCommit TO false",
      "CALL prepareStatement(select 1 from dual)",
      "CALL prepareStatement(select 2 from dual)",
      "COMMIT",
      "SET AutoCommit TO true",
      "CLOSE"
    );
  }

  @Test
  public void isolationLevel_SERIALIZABLE() throws Exception {

    proxied.isolationLevel_SERIALIZABLE();

    assertThat(original.isolationLevel_SERIALIZABLE_tc.events).containsExactly(
      "SET AutoCommit TO false",
      "SET TransactionIsolation TO SERIALIZABLE",
      "CALL prepareStatement(select 1 from dual)",
      "COMMIT",
      "SET AutoCommit TO true",
      "CLOSE"
    );
  }

  @Test
  public void isolationLevel_REPEATABLE_READ() throws Exception {

    proxied.isolationLevel_REPEATABLE_READ();

    assertThat(original.isolationLevel_REPEATABLE_READ_tc.events).containsExactly(
      "SET AutoCommit TO false",
      "SET TransactionIsolation TO REPEATABLE_READ",
      "CALL prepareStatement(select 1 from dual)",
      "COMMIT",
      "SET AutoCommit TO true",
      "CLOSE"
    );
  }

  @Test
  public void isolationLevel_READ_UNCOMMITTED() throws Exception {

    proxied.isolationLevel_READ_UNCOMMITTED();

    assertThat(original.isolationLevel_READ_UNCOMMITTED_tc.events).containsExactly(
      "SET AutoCommit TO false",
      "SET TransactionIsolation TO READ_UNCOMMITTED",
      "CALL prepareStatement(select 1 from dual)",
      "COMMIT",
      "SET AutoCommit TO true",
      "CLOSE"
    );
  }

  @Test
  public void isolationLevel_READ_COMMITTED() throws Exception {

    proxied.isolationLevel_READ_COMMITTED();

    assertThat(original.isolationLevel_READ_COMMITTED_tc.events).containsExactly(
      "SET AutoCommit TO false",
      "SET TransactionIsolation TO READ_COMMITTED",
      "CALL prepareStatement(select 1 from dual)",
      "COMMIT",
      "SET AutoCommit TO true",
      "CLOSE"
    );
  }

  @Test
  public void throwsTestExceptionAndRollback() throws Exception {

    TestException ex = null;

    try {
      proxied.throwsTestExceptionAndRollback();
    } catch (TestException e) {
      ex = e;
    }

    assertThat(ex).isNotNull();

    final TestDataSource.TestConnection tc = original.throwsTestExceptionAndRollback_tc;
    assertThat(tc.events).containsExactly(
      "SET AutoCommit TO false",
      "CALL prepareStatement(select 1 from dual)",
      "ROLLBACK",
      "SET AutoCommit TO true",
      "CLOSE"
    );

  }

  @Test
  public void throwsTestExceptionAndCommit() throws Exception {

    TestException ex = null;

    try {
      proxied.throwsTestExceptionAndCommit();
    } catch (TestException e) {
      ex = e;
    }

    assertThat(ex).isNotNull();

    final TestDataSource.TestConnection tc = original.throwsTestExceptionAndCommit_tc;
    assertThat(tc.events).containsExactly(
      "SET AutoCommit TO false",
      "CALL prepareStatement(select 1 from dual)",
      "COMMIT",
      "SET AutoCommit TO true",
      "CLOSE"
    );

  }

}
