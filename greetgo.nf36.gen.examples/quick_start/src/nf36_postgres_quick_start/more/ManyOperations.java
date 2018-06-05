package nf36_postgres_quick_start.more;

import kz.greetgo.db.AbstractJdbcWithDataSource;
import kz.greetgo.db.ConnectionCallback;
import kz.greetgo.db.DbProxyFactory;
import kz.greetgo.db.GreetgoTransactionManager;
import kz.greetgo.db.InTransaction;
import kz.greetgo.db.Jdbc;
import kz.greetgo.db.TransactionManager;
import nf36_postgres_quick_start.QuickStart.GetTotal;
import nf36_postgres_quick_start.QuickStart.GetValue;
import nf36_postgres_quick_start.QuickStart.SetValue;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.fest.assertions.api.Assertions.assertThat;

public class ManyOperations {

  public interface RegisterInterface {
    void move(int from, int to, int amount);

    int total();

    int zeroCount();
  }

  @InTransaction //if you comment this annotation then transaction mode off, and will be an error
  static class RegisterImpl implements RegisterInterface {

    private Jdbc jdbc;

    public RegisterImpl(Jdbc jdbc) {
      this.jdbc = jdbc;
    }

    //this method must work in transaction
    @Override
    public void move(int from, int to, int amount) {
      if (from == to) return;

      //to prevent dead lock
      if (from < to) {

        int xxx = from;
        from = to;
        to = xxx;

        amount = -amount;
      }

      int fromValue = jdbc.execute(new GetValue(from));
      int toValue = jdbc.execute(new GetValue(to));

      fromValue -= amount;
      toValue += amount;

      jdbc.execute(new SetValue(from, fromValue));
      jdbc.execute(new SetValue(to, toValue));
    }

    @Override
    public int total() {
      return jdbc.execute(new GetTotal());
    }

    @Override
    public int zeroCount() {
      return jdbc.execute(new ZeroCount());
    }
  }

  public static class Operation {
    public int from, to, amount;

    public void invert() {
      amount = -amount;
    }
  }

  public static class OperationList extends Thread {

    private final RegisterInterface register;

    public OperationList(RegisterInterface register) {
      this.register = register;
    }

    public final List<Operation> operationList = new ArrayList<>();

    @Override
    public void run() {
      operationList.forEach(x -> register.move(x.from, x.to, x.amount));
    }
  }

  public static void main(String[] args) throws Exception {
    //
    // PREPARE DATABASE
    //

    //Admin access from environments
    String adminUrl = System.getenv("PG_ADMIN_URL");
    String adminPassword = System.getenv("PG_ADMIN_PASSWORD");
    String adminUser = System.getenv("PG_ADMIN_USERID");

    String username = System.getProperty("user.name") + "_db_quick_start";
    String password = username + "123";
    String db = username + "_db";

    Class.forName("org.postgresql.Driver");

    try (Connection con = DriverManager.getConnection(adminUrl, adminUser, adminPassword)) {
      exec(con, "drop  database if exists " + db);
      exec(con, "drop  user if exists " + username);
      exec(con, "create  user " + username + " with password '" + password + "'");
      exec(con, "create  database " + db + " with owner = " + username);
    }

    BasicDataSource pool = new BasicDataSource();

    pool.setDriverClassName("org.postgresql.Driver");
    pool.setUrl(changeDb(adminUrl, db));
    pool.setUsername(username);
    pool.setPassword(password);

    try (Connection con = pool.getConnection()) {
      exec(con, "create  table account (id int primary key, value int not null default 0)");
      exec(con, "insert  into account (id) values (1), (2), (3), (4), (5), (6), (7), (8), (9), (10)");
    }

    //
    // PREPARE INFRASTRUCTURE
    //

    TransactionManager transactionManager = new GreetgoTransactionManager();

    Jdbc jdbc = createJdbcFrom(pool, transactionManager);

    RegisterImpl wrappingRegister = new RegisterImpl(jdbc);

    DbProxyFactory dbProxyFactory = new DbProxyFactory(transactionManager);

    RegisterInterface proxy = dbProxyFactory.createProxyOn(RegisterInterface.class, wrappingRegister);

    //
    // CHECK THAT ALL OK
    //
    assertThat(proxy.total()).isZero();
    assertThat(proxy.zeroCount()).isEqualTo(10);

    final List<OperationList> operationListList = new ArrayList<>();
    {
      Random random = new Random();
      for (int i = 0; i < 10; i++) {
        OperationList ol = new OperationList(proxy);
        for (int u = 0; u < 100; u++) {
          Operation o = new Operation();
          o.from = random.nextInt(10) + 1;
          o.to = random.nextInt(10) + 1;
          o.amount = random.nextInt(100) + 1;
          ol.operationList.add(o);
        }
        operationListList.add(ol);
      }
    }

    operationListList.forEach(OperationList::run);

    assertThat(proxy.total()).isZero();
    assertThat(proxy.zeroCount()).isLessThan(10);

    operationListList.forEach(x -> x.operationList.forEach(Operation::invert));

    operationListList.forEach(Thread::start);
    for (OperationList operationList : operationListList) {
      operationList.join();
    }

    //
    // CHECK THAT ALL OK AGAIN
    //
    assertThat(proxy.total()).describedAs("Transactions are absent").isZero();
    assertThat(proxy.zeroCount()).isEqualTo(10);

    System.out.println("All works OK!");
  }



  public static class ZeroCount implements ConnectionCallback<Integer> {
    @Override
    public Integer doInConnection(Connection con) throws Exception {
      try (PreparedStatement ps = con.prepareStatement("select count(1) from account where value = 0")) {
        try (ResultSet rs = ps.executeQuery()) {
          if (!rs.next()) throw new RuntimeException("Left state");
          return rs.getInt(1);
        }
      }
    }
  }

  private static void exec(Connection con, String sql) throws SQLException {
    try (Statement statement = con.createStatement()) {
      statement.execute(sql);
      System.out.println("EXEC SQL: " + sql.replaceAll("\\s+", " "));
    }
  }

  private static String changeDb(String adminUrl, String db) {
    int idx = adminUrl.lastIndexOf('/');
    if (idx < 0) throw new RuntimeException("Left admin url = " + adminUrl);
    return adminUrl.substring(0, idx + 1) + db;
  }

  private static Jdbc createJdbcFrom(BasicDataSource pool, TransactionManager transactionManager) {
    return new AbstractJdbcWithDataSource() {
      @Override
      protected DataSource getDataSource() {
        return pool;
      }

      @Override
      protected TransactionManager getTransactionManager() {
        return transactionManager;
      }
    };
  }
}
