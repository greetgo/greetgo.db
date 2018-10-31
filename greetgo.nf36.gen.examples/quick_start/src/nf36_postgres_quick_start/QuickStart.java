package nf36_postgres_quick_start;

import kz.greetgo.db.AbstractJdbcWithDataSource;
import kz.greetgo.db.ConnectionCallback;
import kz.greetgo.db.DbProxyFactory;
import kz.greetgo.db.GreetgoTransactionManager;
import kz.greetgo.db.InTransaction;
import kz.greetgo.db.Jdbc;
import kz.greetgo.db.TransactionManager;
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

public class QuickStart {

  public interface RegisterInterface {
    void move(int from, int to, int amount);

    int total();
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
      if (from == to) {
        return;
      }

      //to prevent dead lock
      if (from < to) {
        amount = -amount;

        int xxx = from;
        from = to;
        to = xxx;
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
  }

  public static void main(String[] args) throws Exception {
    //
    // PREPARE DATABASE
    //

    //Admin access from environments
    String adminUrl = System.getenv("PG_ADMIN_URL");
    String adminUser = System.getenv("PG_ADMIN_USERID");
    String adminPassword = System.getenv("PG_ADMIN_PASSWORD");

    //Or you can uncomment following lines:
    //    String adminUrl = "jdbc:postgresql://localhost/postgres";
    //    String adminUser = "postgres";
    //    String adminPassword = "password for postgres";

    String username = System.getProperty("user.name") + "_db_quick_start";
    String password = username + "123";
    String db = username + "_db";

    Class.forName("org.postgresql.Driver");

    try (Connection con = DriverManager.getConnection(adminUrl, adminUser, adminPassword)) {
      exec(con, "drop database if exists " + db);
      exec(con, "drop user if exists " + username);
      exec(con, "create user " + username + " with password '" + password + "'");
      exec(con, "create database " + db + " with owner = " + username);
    }

    BasicDataSource pool = new BasicDataSource();

    pool.setDriverClassName("org.postgresql.Driver");
    pool.setUrl(changeDb(adminUrl, db));
    pool.setUsername(username);
    pool.setPassword(password);

    try (Connection con = pool.getConnection()) {
      exec(con, "create table account (id int primary key, value int not null default 0)");
      exec(con, "insert into account (id) values (1), (2), (3), (4), (5), (6), (7), (8), (9), (10)");
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

    List<Thread> threadList = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      threadList.add(new Thread(() -> {
        Random random = new Random();
        for (int u = 0; u < 100; u++) {
          int from = random.nextInt(10) + 1;
          int to = random.nextInt(10) + 1;
          int amount = random.nextInt(100) + 1;

          proxy.move(from, to, amount);
        }
      }));
    }

    threadList.forEach(Thread::start);//run in threads
    for (Thread thread : threadList) {
      thread.join();
    }

    //
    // CHECK THAT ALL OK AGAIN
    //
    assertThat(proxy.total()).describedAs("Transactions are absent").isZero();

    System.out.println("All works OK!");
  }

  public static class GetValue implements ConnectionCallback<Integer> {
    private final int id;

    public GetValue(int id) {
      this.id = id;
    }

    @Override
    public Integer doInConnection(Connection con) throws Exception {
      try (PreparedStatement ps = con.prepareStatement("select value from account where id = ? for update")) {
        ps.setInt(1, id);
        try (ResultSet rs = ps.executeQuery()) {
          if (!rs.next()) {
            throw new RuntimeException("Left state");
          }
          return rs.getInt(1);
        }
      }
    }
  }

  public static class SetValue implements ConnectionCallback<Void> {
    private final int id;
    private final int value;

    public SetValue(int id, int value) {
      this.id = id;
      this.value = value;
    }

    @Override
    public Void doInConnection(Connection con) throws Exception {
      try (PreparedStatement ps = con.prepareStatement("update account set value = ? where id = ?")) {
        ps.setInt(1, value);
        ps.setInt(2, id);
        int count = ps.executeUpdate();
        if (count <= 0) {
          throw new RuntimeException("Left update");
        }
        return null;
      }
    }
  }

  public static class GetTotal implements ConnectionCallback<Integer> {
    @Override
    public Integer doInConnection(Connection con) throws Exception {
      try (PreparedStatement ps = con.prepareStatement("select sum(value) from account")) {
        try (ResultSet rs = ps.executeQuery()) {
          if (!rs.next()) {
            throw new RuntimeException("Left state");
          }
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
    if (idx < 0) {
      throw new RuntimeException("Left admin url = " + adminUrl);
    }
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
