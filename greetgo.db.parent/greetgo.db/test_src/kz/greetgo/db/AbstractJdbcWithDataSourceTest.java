package kz.greetgo.db;

import org.testng.annotations.Test;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.fest.assertions.api.Assertions.assertThat;

public class AbstractJdbcWithDataSourceTest {

  class MyAbstractJdbcWithDataSource extends AbstractJdbcWithDataSource {
    @Override
    protected DataSource getDataSource() {
      return testDataSource;
    }

    @Override
    protected TransactionManager getTransactionManager() {
      return transactionManager;
    }
  }

  private TestDataSource testDataSource;
  private GreetgoTransactionManager transactionManager;

  @Test
  public void executeConnection_withTransactionManager() throws Exception {
    transactionManager = new GreetgoTransactionManager();
    testDataSource = new TestDataSource();
    Jdbc jdbc = new MyAbstractJdbcWithDataSource();

    final TestDataSource.TestConnection testConnection[] = new TestDataSource.TestConnection[]{null};

    jdbc.executeConnection(new ConnectionExecutor<Object>() {
      @Override
      public Object execute(Connection connection) throws Exception {
        connection.prepareStatement("select 1 from dual");
        testConnection[0] = TestDataSource.extractTestConnection(connection);
        return null;
      }
    });

    assertThat(testConnection[0].events).containsExactly(
      "CALL prepareStatement(select 1 from dual)",
      "CLOSE"
    );
  }

  @Test
  public void executeConnection_withoutTransactionManager() throws Exception {
    transactionManager = null;
    testDataSource = new TestDataSource();
    Jdbc jdbc = new MyAbstractJdbcWithDataSource();

    final TestDataSource.TestConnection testConnection[] = new TestDataSource.TestConnection[]{null};

    jdbc.executeConnection(new ConnectionExecutor<Object>() {
      @Override
      public Object execute(Connection connection) throws Exception {
        connection.prepareStatement("select 1 from dual");
        testConnection[0] = TestDataSource.extractTestConnection(connection);
        return null;
      }
    });

    assertThat(testConnection[0].events).containsExactly(
      "CALL prepareStatement(select 1 from dual)",
      "CLOSE"
    );
  }

  @Test(expectedExceptions = NullPointerException.class, expectedExceptionsMessageRegExp = "dataSource == null")
  public void executeConnection_noDataSource() throws Exception {
    transactionManager = null;
    testDataSource = null;
    Jdbc jdbc = new MyAbstractJdbcWithDataSource();

    jdbc.executeConnection(new ConnectionExecutor<Object>() {
      @Override
      public Object execute(Connection connection) throws Exception {
        return null;
      }
    });
  }
}