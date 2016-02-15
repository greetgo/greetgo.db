package kz.greetgo.db;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static kz.greetgo.db.TestDataSource.extractTestConnection;
import static org.fest.assertions.api.Assertions.assertThat;

public class DbProxyFactoryTest_inSomeThreads {

  private OwnerController originalOwner, proxiedOwner;
  private ChildController originalChild;
  private TestDataSource commonDb;

  @BeforeMethod
  public void setup() {
    TestExceptionCatcher exceptionCatcher = new TestExceptionCatcher();
    GreetgoTransactionManager transactionManager = new GreetgoTransactionManager();
    transactionManager.setExceptionCatcher(exceptionCatcher);
    DbProxyFactory dbProxyFactory = new DbProxyFactory(transactionManager);

    commonDb = new TestDataSource();

    originalChild = new ChildController();
    originalChild.transactionManager = transactionManager;
    originalChild.commonDb = commonDb;

    ChildController proxiedChild = (ChildController) dbProxyFactory.createProxyFor(originalChild);

    originalOwner = new OwnerController();
    originalOwner.transactionManager = transactionManager;
    originalOwner.child = proxiedChild;
    originalOwner.commonDb = commonDb;

    proxiedOwner = (OwnerController) dbProxyFactory.createProxyFor(originalOwner);
  }

  public static class ChildController {
    TransactionManager transactionManager;
    TestDataSource commonDb;

    final Map<String, TestDataSource.TestConnection> allInTransaction_tc = new ConcurrentHashMap<>();

    @InTransaction
    public void allInTransaction() throws SQLException {

      final String threadName = Thread.currentThread().getName();

      final Connection connection = transactionManager.getConnection(commonDb);
      assertThat(connection.getAutoCommit()).isFalse();
      connection.prepareStatement("select 'child all' as m, '" + threadName + "' as t from dual");
      transactionManager.closeConnection(commonDb, connection);

      allInTransaction_tc.put(threadName, extractTestConnection(connection));

    }

    final Map<String, TestDataSource.TestConnection> childInTransaction_tc = new ConcurrentHashMap<>();

    @InTransaction
    public void childInTransaction() throws SQLException {
      final String threadName = Thread.currentThread().getName();

      final Connection connection = transactionManager.getConnection(commonDb);
      assertThat(connection.getAutoCommit()).isFalse();
      connection.prepareStatement("select 'child child' as m, '" + threadName + "' as t from dual");
      transactionManager.closeConnection(commonDb, connection);

      childInTransaction_tc.put(threadName, extractTestConnection(connection));
    }

    final Map<String, TestDataSource.TestConnection> ownerInTransaction_tc = new ConcurrentHashMap<>();

    public void ownerInTransaction() throws SQLException {

      final String threadName = Thread.currentThread().getName();

      final Connection connection = transactionManager.getConnection(commonDb);
      assertThat(connection.getAutoCommit()).isFalse();
      connection.prepareStatement("select 'child owner' as m, '" + threadName + "' as t from dual");
      transactionManager.closeConnection(commonDb, connection);

      ownerInTransaction_tc.put(threadName, extractTestConnection(connection));
    }

    final Map<String, TestDataSource.TestConnection> withoutTransaction_tc = new ConcurrentHashMap<>();

    public void withoutTransaction() throws SQLException {

      final String threadName = Thread.currentThread().getName();

      final Connection connection = transactionManager.getConnection(commonDb);
      assertThat(connection.getAutoCommit()).isTrue();
      connection.prepareStatement("select 'child without' as m, '" + threadName + "' as t from dual");
      transactionManager.closeConnection(commonDb, connection);

      withoutTransaction_tc.put(threadName, extractTestConnection(connection));
    }
  }

  public static class OwnerController {
    TransactionManager transactionManager;
    ChildController child;
    TestDataSource commonDb;

    final Map<String, TestDataSource.TestConnection> allInTransaction_tc = new ConcurrentHashMap<>();

    @InTransaction
    public void allInTransaction() throws SQLException {

      final String threadName = Thread.currentThread().getName();

      final Connection connection = transactionManager.getConnection(commonDb);
      assertThat(connection.getAutoCommit()).isFalse();
      connection.prepareStatement("select 'owner all' as m, '" + threadName + "' as t from dual");
      transactionManager.closeConnection(commonDb, connection);

      allInTransaction_tc.put(threadName, extractTestConnection(connection));

      child.allInTransaction();
    }

    final Map<String, TestDataSource.TestConnection> childInTransaction_tc = new ConcurrentHashMap<>();

    public void childInTransaction() throws SQLException {

      final String threadName = Thread.currentThread().getName();

      final Connection connection = transactionManager.getConnection(commonDb);
      assertThat(connection.getAutoCommit()).isTrue();
      connection.prepareStatement("select 'owner child' as m, '" + threadName + "' as t from dual");
      transactionManager.closeConnection(commonDb, connection);

      childInTransaction_tc.put(threadName, extractTestConnection(connection));

      child.childInTransaction();
    }

    final Map<String, TestDataSource.TestConnection> ownerInTransaction_tc = new ConcurrentHashMap<>();

    @InTransaction
    public void ownerInTransaction() throws SQLException {

      final String threadName = Thread.currentThread().getName();

      final Connection connection = transactionManager.getConnection(commonDb);
      assertThat(connection.getAutoCommit()).isFalse();
      connection.prepareStatement("select 'owner owner' as m, '" + threadName + "' as t from dual");
      transactionManager.closeConnection(commonDb, connection);

      ownerInTransaction_tc.put(threadName, extractTestConnection(connection));

      child.ownerInTransaction();
    }

    final Map<String, TestDataSource.TestConnection> withoutTransaction_tc = new ConcurrentHashMap<>();

    public void withoutTransaction() throws SQLException {

      final String threadName = Thread.currentThread().getName();

      final Connection connection = transactionManager.getConnection(commonDb);
      assertThat(connection.getAutoCommit()).isTrue();
      connection.prepareStatement("select 'owner without' as m, '" + threadName + "' as t from dual");
      transactionManager.closeConnection(commonDb, connection);

      withoutTransaction_tc.put(threadName, extractTestConnection(connection));

      child.withoutTransaction();
    }
  }

  static class TestThread extends Thread {
    public TestThread(String name) {
      super(name);
    }
  }

  @Test
  public void allInTransaction() throws Exception {

    TestThread threads[] = new TestThread[2];

    for (int i = 0; i < threads.length; i++) {
      threads[i] = new TestThread("allInTransaction " + i) {
        @Override
        public void run() {
          try {
            proxiedOwner.allInTransaction();
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        }
      };
    }

    for (TestThread thread : threads) {
      thread.start();
    }
    for (TestThread thread : threads) {
      thread.join();
    }

    assertThat(commonDb.gotConnections).hasSize(2);

    assertThat(
      originalChild.allInTransaction_tc.get(threads[0].getName())
    ).isSameAs(
      originalOwner.allInTransaction_tc.get(threads[0].getName())
    );

    assertThat(
      originalChild.allInTransaction_tc.get(threads[1].getName())
    ).isSameAs(
      originalOwner.allInTransaction_tc.get(threads[1].getName())
    );

    assertThat(originalChild.allInTransaction_tc.get(threads[0].getName()).events).containsExactly(
      "SET AutoCommit TO false",
      "CALL prepareStatement(select 'owner all' as m, 'allInTransaction 0' as t from dual)",
      "CALL prepareStatement(select 'child all' as m, 'allInTransaction 0' as t from dual)",
      "COMMIT",
      "SET AutoCommit TO true",
      "CLOSE"
    );
    assertThat(originalChild.allInTransaction_tc.get(threads[1].getName()).events).containsExactly(
      "SET AutoCommit TO false",
      "CALL prepareStatement(select 'owner all' as m, 'allInTransaction 1' as t from dual)",
      "CALL prepareStatement(select 'child all' as m, 'allInTransaction 1' as t from dual)",
      "COMMIT",
      "SET AutoCommit TO true",
      "CLOSE"
    );

  }

  @Test
  public void ownerInTransaction() throws Exception {

    TestThread threads[] = new TestThread[2];

    for (int i = 0; i < threads.length; i++) {
      threads[i] = new TestThread("ownerInTransaction " + i) {
        @Override
        public void run() {
          try {
            proxiedOwner.ownerInTransaction();
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        }
      };
    }

    for (TestThread thread : threads) {
      thread.start();
    }
    for (TestThread thread : threads) {
      thread.join();
    }

    assertThat(commonDb.gotConnections).hasSize(2);

    assertThat(
      originalChild.ownerInTransaction_tc.get(threads[0].getName())
    ).isSameAs(
      originalOwner.ownerInTransaction_tc.get(threads[0].getName())
    );

    assertThat(
      originalChild.ownerInTransaction_tc.get(threads[1].getName())
    ).isSameAs(
      originalOwner.ownerInTransaction_tc.get(threads[1].getName())
    );

    assertThat(originalChild.ownerInTransaction_tc.get(threads[0].getName()).events).containsExactly(
      "SET AutoCommit TO false",
      "CALL prepareStatement(select 'owner owner' as m, 'ownerInTransaction 0' as t from dual)",
      "CALL prepareStatement(select 'child owner' as m, 'ownerInTransaction 0' as t from dual)",
      "COMMIT",
      "SET AutoCommit TO true",
      "CLOSE"
    );
    assertThat(originalChild.ownerInTransaction_tc.get(threads[1].getName()).events).containsExactly(
      "SET AutoCommit TO false",
      "CALL prepareStatement(select 'owner owner' as m, 'ownerInTransaction 1' as t from dual)",
      "CALL prepareStatement(select 'child owner' as m, 'ownerInTransaction 1' as t from dual)",
      "COMMIT",
      "SET AutoCommit TO true",
      "CLOSE"
    );
  }

  @Test
  public void childInTransaction() throws Exception {

    TestThread threads[] = new TestThread[2];

    for (int i = 0; i < threads.length; i++) {
      threads[i] = new TestThread("childInTransaction " + i) {
        @Override
        public void run() {
          try {
            proxiedOwner.childInTransaction();
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        }
      };
    }

    for (TestThread thread : threads) {
      thread.start();
    }
    for (TestThread thread : threads) {
      thread.join();
    }

    assertThat(commonDb.gotConnections).hasSize(4);

    assertThat(
      originalChild.childInTransaction_tc.get(threads[0].getName())
    ).isNotSameAs(
      originalOwner.childInTransaction_tc.get(threads[0].getName())
    );

    assertThat(
      originalChild.childInTransaction_tc.get(threads[1].getName())
    ).isNotSameAs(
      originalOwner.childInTransaction_tc.get(threads[1].getName())
    );

    assertThat(originalChild.childInTransaction_tc.get(threads[0].getName()).events).containsExactly(
      "SET AutoCommit TO false",
      "CALL prepareStatement(select 'child child' as m, 'childInTransaction 0' as t from dual)",
      "COMMIT",
      "SET AutoCommit TO true",
      "CLOSE"
    );
    assertThat(originalChild.childInTransaction_tc.get(threads[1].getName()).events).containsExactly(
      "SET AutoCommit TO false",
      "CALL prepareStatement(select 'child child' as m, 'childInTransaction 1' as t from dual)",
      "COMMIT",
      "SET AutoCommit TO true",
      "CLOSE"
    );

    assertThat(originalOwner.childInTransaction_tc.get(threads[0].getName()).events).containsExactly(
      "CALL prepareStatement(select 'owner child' as m, 'childInTransaction 0' as t from dual)",
      "CLOSE"
    );
    assertThat(originalOwner.childInTransaction_tc.get(threads[1].getName()).events).containsExactly(
      "CALL prepareStatement(select 'owner child' as m, 'childInTransaction 1' as t from dual)",
      "CLOSE"
    );
  }

  @Test
  public void withoutTransaction() throws Exception {

    TestThread threads[] = new TestThread[2];

    for (int i = 0; i < threads.length; i++) {
      threads[i] = new TestThread("withoutTransaction " + i) {
        @Override
        public void run() {
          try {
            proxiedOwner.withoutTransaction();
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        }
      };
    }

    for (TestThread thread : threads) {
      thread.start();
    }
    for (TestThread thread : threads) {
      thread.join();
    }

    assertThat(commonDb.gotConnections).hasSize(4);

    assertThat(
      originalChild.withoutTransaction_tc.get(threads[0].getName())
    ).isNotSameAs(
      originalOwner.withoutTransaction_tc.get(threads[0].getName())
    );

    assertThat(
      originalChild.withoutTransaction_tc.get(threads[1].getName())
    ).isNotSameAs(
      originalOwner.withoutTransaction_tc.get(threads[1].getName())
    );

    assertThat(originalChild.withoutTransaction_tc.get(threads[0].getName()).events).containsExactly(
      "CALL prepareStatement(select 'child without' as m, 'withoutTransaction 0' as t from dual)",
      "CLOSE"
    );
    assertThat(originalChild.withoutTransaction_tc.get(threads[1].getName()).events).containsExactly(
      "CALL prepareStatement(select 'child without' as m, 'withoutTransaction 1' as t from dual)",
      "CLOSE"
    );

    assertThat(originalOwner.withoutTransaction_tc.get(threads[0].getName()).events).containsExactly(
      "CALL prepareStatement(select 'owner without' as m, 'withoutTransaction 0' as t from dual)",
      "CLOSE"
    );
    assertThat(originalOwner.withoutTransaction_tc.get(threads[1].getName()).events).containsExactly(
      "CALL prepareStatement(select 'owner without' as m, 'withoutTransaction 1' as t from dual)",
      "CLOSE"
    );
  }
}
