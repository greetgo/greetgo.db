package kz.greetgo.db;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.fest.assertions.api.Assertions.assertThat;

public class GreetgoTransactionManagerTest {

  TransactionManager tm = null;

  @BeforeMethod
  public void setup() {
    tm = new GreetgoTransactionManager();
  }

  @Test
  public void levelZero_parallelConnections_is_different() throws Exception {

    TestDataSource ds = new TestDataSource();

    final Connection connection1 = notNull(tm.getConnection(ds));
    closeAndAssert(ds, connection1);

    assertSame(connection1, ds.lastGotConnection());

    final Connection connection2 = notNull(tm.getConnection(ds));
    closeAndAssert(ds, connection2);

    assertSame(connection2, ds.lastGotConnection());

    assertNotSame(connection1, connection2);
  }

  private <T> T notNull(T object) {
    assertThat(object).isNotNull();
    return object;
  }

  private void closeAndAssert(TestDataSource ds, Connection connection1) throws SQLException {
    assertThat(connection1.isClosed()).isFalse();
    tm.closeConnection(ds, connection1);
    assertThat(connection1.isClosed()).isTrue();
  }

  private void assertNotSame(Connection connection1, Connection connection2) throws SQLException {
    assertThat(connection1.getSchema()).isNotEqualTo(connection2.getSchema());
  }

  private void assertSame(Connection connection1, Connection connection2) throws SQLException {
    assertThat(connection1.getSchema()).isEqualTo(connection2.getSchema());
  }

  @Test
  public void levelOne_parallelConnections_is_same() throws Exception {

    tm.upLevel(callMetaDefault());

    TestDataSource ds1 = new TestDataSource();
    TestDataSource ds2 = new TestDataSource();

    final Connection connection1_of_ds1 = notNull(tm.getConnection(ds1));
    assertSame(connection1_of_ds1, ds1.lastGotConnection());

    final Connection connection1_of_ds2 = notNull(tm.getConnection(ds2));
    assertSame(connection1_of_ds2, ds2.lastGotConnection());

    assertThat(connection1_of_ds1.isClosed()).isFalse();
    assertThat(connection1_of_ds2.isClosed()).isFalse();

    tm.closeConnection(ds1, connection1_of_ds1);
    tm.closeConnection(ds2, connection1_of_ds2);

    final Connection connection2_of_ds1 = notNull(tm.getConnection(ds1));
    assertSame(connection2_of_ds1, ds1.lastGotConnection());

    final Connection connection2_of_ds2 = notNull(tm.getConnection(ds2));
    assertSame(connection2_of_ds2, ds2.lastGotConnection());

    tm.closeConnection(ds1, connection2_of_ds1);
    tm.closeConnection(ds2, connection2_of_ds2);

    assertSame(connection1_of_ds1, connection2_of_ds1);
    assertSame(connection1_of_ds2, connection2_of_ds2);

    assertNotSame(connection1_of_ds1, connection1_of_ds2);
  }

  private CallMeta callMetaDefault() {
    return new CallMeta(null, IsolationLevel.DEFAULT, null);
  }

  @Test
  public void twoLevels_parallelConnections_differentLevels_is_same() throws Exception {

    tm.upLevel(callMetaDefault());

    TestDataSource ds1 = new TestDataSource();
    TestDataSource ds2 = new TestDataSource();

    final Connection connection1_of_ds1 = notNull(tm.getConnection(ds1));
    assertSame(connection1_of_ds1, ds1.lastGotConnection());

    final Connection connection1_of_ds2 = notNull(tm.getConnection(ds2));
    assertSame(connection1_of_ds2, ds2.lastGotConnection());

    tm.closeConnection(ds1, connection1_of_ds1);
    tm.closeConnection(ds2, connection1_of_ds2);

    tm.upLevel(callMetaDefault());

    final Connection connection2_of_ds1 = notNull(tm.getConnection(ds1));
    assertSame(connection2_of_ds1, ds1.lastGotConnection());

    final Connection connection2_of_ds2 = notNull(tm.getConnection(ds2));
    assertSame(connection2_of_ds2, ds2.lastGotConnection());

    tm.closeConnection(ds1, connection2_of_ds1);
    tm.closeConnection(ds2, connection2_of_ds2);

    assertSame(connection1_of_ds1, connection2_of_ds1);
    assertSame(connection1_of_ds2, connection2_of_ds2);

    assertNotSame(connection1_of_ds1, connection1_of_ds2);
  }

  @Test
  public void levelOne_innerConnections_is_different() throws Exception {

    tm.upLevel(callMetaDefault());

    TestDataSource ds1 = new TestDataSource();
    TestDataSource ds2 = new TestDataSource();

    final Connection connection1_of_ds1 = notNull(tm.getConnection(ds1));
    assertSame(connection1_of_ds1, ds1.lastGotConnection());

    final Connection connection1_of_ds2 = notNull(tm.getConnection(ds2));
    assertSame(connection1_of_ds2, ds2.lastGotConnection());

    final Connection connection2_of_ds1 = notNull(tm.getConnection(ds1));
    assertSame(connection2_of_ds1, ds1.lastGotConnection());

    final Connection connection2_of_ds2 = notNull(tm.getConnection(ds2));
    assertSame(connection2_of_ds2, ds2.lastGotConnection());

    tm.closeConnection(ds1, connection2_of_ds1);
    tm.closeConnection(ds2, connection2_of_ds2);

    tm.closeConnection(ds1, connection1_of_ds1);
    tm.closeConnection(ds2, connection1_of_ds2);

    assertNotSame(connection1_of_ds1, connection2_of_ds1);
    assertNotSame(connection1_of_ds2, connection2_of_ds2);

    assertNotSame(connection1_of_ds1, connection1_of_ds2);
  }

  @Test
  public void zeroLevel() throws Exception {
    TestDataSource ds = new TestDataSource();

    final Connection connection = notNull(tm.getConnection(ds));

    final TestDataSource.TestConnection tc = TestDataSource.extractTestConnection(connection);
    assertThat(tc.setAutoCommitCallCount).isZero();
    assertThat(tc.setTransactionIsolationCallCount).isZero();

    assertThat(connection.isClosed()).isFalse();

    tm.closeConnection(ds, connection);

    assertThat(connection.isClosed()).isTrue();
  }

  @Test
  public void oneLevel_DEFAULT() throws Exception {
    tm.upLevel(callMetaDefault());

    TestDataSource ds = new TestDataSource();

    final Connection connection = notNull(tm.getConnection(ds));

    final TestDataSource.TestConnection tc = TestDataSource.extractTestConnection(connection);
    assertThat(tc.setAutoCommitCallCount).isEqualTo(1);
    assertThat(tc.autoCommit).isFalse();
    assertThat(tc.setTransactionIsolationCallCount).isZero();

    assertThat(connection.isClosed()).isFalse();

    tm.closeConnection(ds, connection);

    assertThat(connection.isClosed()).isFalse();

    final Connection connection2 = notNull(tm.getConnection(ds));

    assertSame(connection, connection2);

    assertThat(connection2.isClosed()).isFalse();

    tm.closeConnection(ds, connection2);

    assertThat(connection2.isClosed()).isFalse();

    assertThat(tc.setAutoCommitCallCount).isEqualTo(1);

    tm.downLevel(null);

    assertThat(connection2.isClosed()).isTrue();

    assertThat(tc.setAutoCommitCallCount).isEqualTo(2);
    assertThat(tc.autoCommit).isTrue();

    assertThat(tc.commitCallCount).isEqualTo(1);
    assertThat(tc.rollbackCallCount).isZero();
  }
}
