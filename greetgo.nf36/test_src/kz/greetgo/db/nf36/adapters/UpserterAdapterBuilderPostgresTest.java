package kz.greetgo.db.nf36.adapters;

import kz.greetgo.db.ConnectionCallback;
import kz.greetgo.db.DbType;
import kz.greetgo.db.nf36.core.Nf36HistorySelector;
import kz.greetgo.db.nf36.util.ParentDbTest;
import kz.greetgo.db.nf36.util.Use;
import kz.greetgo.util.RND;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static kz.greetgo.db.nf36.Nf36Builder.newNf36Builder;
import static kz.greetgo.db.nf36.util.SqlErrorType.DROP_TABLE;
import static org.fest.assertions.api.Assertions.assertThat;

@SuppressWarnings("SqlResolve")
@Use(DbType.Postgres)
public class UpserterAdapterBuilderPostgresTest extends ParentDbTest {

  private Date[] dateArray = new Date[30];

  @BeforeMethod
  public void prepareDates() {
    Calendar calendar = new GregorianCalendar();
    calendar.add(Calendar.YEAR, -7);
    for (int i = 0; i < dateArray.length; i++) {
      dateArray[i] = calendar.getTime();
      calendar.add(Calendar.HOUR, 1);
      calendar.add(Calendar.MINUTE, 3);
      calendar.add(Calendar.SECOND, 7);
      calendar.add(Calendar.MILLISECOND, 81);
    }
  }

  protected void createClientTables() {
    exec("drop table m_client_surname", DROP_TABLE);
    exec("drop table m_client_name", DROP_TABLE);
    exec("drop table m_client_father_name", DROP_TABLE);
    exec("drop table client", DROP_TABLE);

    exec("create table client (\n" +
        "  id          varchar(32),\n" +
        "  surname     varchar(300),\n" +
        "  name        varchar(300),\n" +
        "  father_name varchar(300),\n" +
        "  inserted_at timestamp default clock_timestamp() not null,\n" +
        "  primary key (id)\n" +
        ")");
    exec("create table m_client_surname (\n" +
        "  id      varchar(32) references client,\n" +
        "  ts      timestamp default clock_timestamp(),\n" +
        "  author  varchar(100),\n" +
        "  surname varchar(300),\n" +
        "  primary key (id, ts)\n" +
        ")");
    exec("create table m_client_name (\n" +
        "  id     varchar(32) references client,\n" +
        "  ts     timestamp default clock_timestamp(),\n" +
        "  author varchar(100),\n" +
        "  name   varchar(300),\n" +
        "  primary key (id, ts)\n" +
        ")");
    exec("create table m_client_father_name (\n" +
        "  id          varchar(32) references client,\n" +
        "  ts          timestamp default clock_timestamp(),\n" +
        "  author      varchar(100),\n" +
        "  father_name varchar(300),\n" +
        "  primary key (id, ts)\n" +
        ")");
    exec("create index on m_client_father_name (id asc, ts desc)");
  }

  @SuppressWarnings("SameParameterValue")
  private void insClient(String id, int dateIndex, String surname, String name, String fatherName) {
    long timestampInMillis = dateArray[dateIndex].getTime();
    long offset = random.nextInt(800) - 400;
    Timestamp ts = new Timestamp(timestampInMillis + offset);

    execParams("insert into client (id, inserted_at, surname, name, father_name) values (?, ?, ?, ?, ?)",
        id, ts, surname, name, fatherName);
  }

  private String author = null;

  private final Random random = new Random();

  private void historyClient(String id, int dateIndex, String fieldName, Object value) {
    long timestampInMillis = dateArray[dateIndex].getTime();
    long offset = random.nextInt(800) - 400;
    Timestamp ts = new Timestamp(timestampInMillis + offset);

    execParams("insert into m_client_" + fieldName + " (id, ts, " + fieldName + ", author) values (?, ?, ?, ?)",
        id, ts, value, author
    );
  }

  protected final SimpleDateFormat STD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

  protected String stdDate(int dateIndex) {
    return STD.format(dateArray[dateIndex]);
  }

  public static class LocalClient {
    public String id;
    public String surname;
    public String name;
    public String fatherName;
  }

  private Nf36HistorySelector createHistorySelectorForClient() {
    return newNf36Builder()
        .historySelector()
        .setLogAcceptor(new ConsoleLogAcceptor())
        .database(connector().type())
        .setJdbc(connector().jdbc())
        .setSelectAuthor(true)
        .build()
        .addId("id")
        .setNf3TableName("client")
        .setTimeFieldName("ts")
        .setInsertedAtFieldName("inserted_at")
        .field("m_client_surname", "surname", null, "author")
        .field("m_client_name", "name", null, "author")
        .field("m_client_father_name", "father_name", null, "author");
  }

  @Test(enabled = false)
  public void createOneClient() {

    String id = RND.str(7);

    Calendar calendar = new GregorianCalendar();
    calendar.setTime(RND.dateYears(-10, 0));

    final int minutesInMonth = 30 * 24 * 60;

    Date dates[] = new Date[10];

    for (int i = 0; i < dates.length; i++) {
      dates[i] = calendar.getTime();
      calendar.add(Calendar.MINUTE, minutesInMonth / dates.length);
    }

    dateArray = dates;

    String fam = RND.str(5);
    String im = RND.str(5);
    String ot = RND.str(5);

    insClient(id, 0, "Фамилия", "Имя", "Отчество");

    for (int i = 1; i < dates.length; i++) {
      historyClient(id, i, "surname", "fam-" + fam + "-" + i);
      historyClient(id, i, "name", "im-" + im + "-" + i);
      historyClient(id, i, "father_name", "ot-" + ot + "-" + i);
    }
  }

  @Test(enabled = false)
  public void createManyClients() throws Exception {

    Thread threads[] = new Thread[20];
    for (int i = 0; i < threads.length; i++) {
      final int I = i;
      threads[i] = new Thread(() -> {
        for (int j = 0; j < 1000_000; j++) {
          author = "Thread " + I;
          createOneClient();
        }
      });
    }

    for (Thread thread : threads) {
      thread.start();
    }
    for (Thread thread : threads) {
      thread.join();
    }

  }

  @Test(enabled = false)
  public void createManyClients2() throws Exception {

    Supplier<String> author = new Supplier<String>() {
      final String authors[] = ("Менделеев Пушкин Сталин Путин Стрелков Минин Пожарский Некрасов Стругатский" +
          " Поклонская Ворошилов Сидоров Бутлеров Гагарин Королёв Невский Донской").split("\\s+");
      final AtomicInteger index = new AtomicInteger(0);

      @Override
      public String get() {
        return authors[index.getAndIncrement() % authors.length];
      }
    };

    Thread threads[] = new Thread[10];
    for (int i = 0; i < threads.length; i++) {

      threads[i] = new Thread(() -> {

        connector().jdbc().execute((ConnectionCallback<Void>) con -> {

          for (int j = 0; j < 100; j++) {
            createClients(1000, 10, author, con);
          }

          return null;
        });

        System.out.println(" --- Thread " + Thread.currentThread().getName() + " finished");

      });
    }

    long started = System.nanoTime();

    for (Thread thread : threads) {
      thread.start();
    }
    for (Thread thread : threads) {
      thread.join();
      System.out.println(" --- Joined to thread " + thread.getName());
    }

    long delay = System.nanoTime() - started;

    System.out.println("Finish for " + ((double) delay / 1e9) + " seconds");
  }

  @SuppressWarnings("SameParameterValue")
  private void createClients(int clientCount,
                             int changesCount,
                             Supplier<String> author,
                             Connection connection) throws SQLException {

    boolean savedAutoCommit = connection.getAutoCommit();
    connection.setAutoCommit(false);
    try {

      long dates[] = new long[clientCount];
      String ids[] = new String[clientCount];
      for (int i = 0; i < clientCount; i++) {
        ids[i] = RND.str(7);
        dates[i] = RND.dateYears(-20, -1).getTime();
      }

      try (PreparedStatement ps = connection.prepareStatement(
          "insert into client (id, surname, name, father_name, inserted_at) values (?,?,?,?,?)")) {

        for (int i = 0; i < clientCount; i++) {
          ps.setString(1, ids[i]);
          String rndStr = RND.str(5);
          ps.setString(2, "fam-" + rndStr);
          ps.setString(3, "im-" + rndStr);
          ps.setString(4, "ot-" + rndStr);
          ps.setTimestamp(5, new Timestamp(dates[i]));
          ps.addBatch();
        }
        ps.executeBatch();
        connection.commit();
      }

      final long millisInMonth = 1000L * 60L * 60L * 24L * 30L;

      final long millisInStep = millisInMonth / changesCount;

      insertUpdates(ids, dates, changesCount, author, millisInStep, connection, "surname");
      insertUpdates(ids, dates, changesCount, author, millisInStep, connection, "name");
      insertUpdates(ids, dates, changesCount, author, millisInStep, connection, "father_name");

    } finally {
      if (savedAutoCommit) {
        connection.setAutoCommit(true);
      }
    }

    System.out.println("Inserted " + clientCount + " clients from " + Thread.currentThread().getName());
  }

  private void insertUpdates(String[] ids, long[] dates, int changesCount, Supplier<String> author,
                             long millisInStep, Connection connection, String fieldName) throws SQLException {
    int clientCount = ids.length;
    Random random = new Random();
    try (PreparedStatement ps = connection.prepareStatement(
        "insert into m_client_" + fieldName + " (id, ts, author, " + fieldName + ") values (?,?,?,?)")) {

      for (int i = 0; i < clientCount; i++) {

        long ts = dates[i];
        String id = ids[i];

        for (int j = 0; j < changesCount; j++) {

          ps.setString(1, id);
          ps.setTimestamp(2, new Timestamp(ts));
          ps.setString(3, author.get());
          ps.setString(4, fieldName + "-" + RND.str(7));
          ps.addBatch();

          ts += millisInStep + random.nextInt(800) - 400;
        }
      }

      ps.executeBatch();
      connection.commit();

    }
  }

  @Test
  public void putTo_onlyFields() throws Exception {
    connector().prepareDatabase();

    createClientTables();

    String id1 = "id1";
    String id2 = "id2";

    author = "Инициатор №1";

    insClient(id1, 0, "И", "С", "П");

    historyClient(id1, 2, "surname", "Иванов");
    historyClient(id1, 2, "name", "Сидор");
    historyClient(id1, 2, "father_name", "Анатольевич");

    author = "Инициатор №2";

    insClient(id2, 4, "П", "Г", "В");

    historyClient(id2, 4, "surname", "Потапов");
    historyClient(id2, 4, "name", "Геннадий");
    historyClient(id2, 4, "father_name", "Васильевич");

    author = "Менятель №1";

    historyClient(id1, 6, "surname", "Иванов-Ганич");
    historyClient(id2, 6, "surname", "Потапов-Ганич");
    historyClient(id1, 6, "name", "Сидорус");
    historyClient(id2, 6, "name", "Генна");

    author = "Менятель №2";

    historyClient(id1, 8, "surname", "Иванов-Ганичев");
    historyClient(id2, 8, "surname", "Потапов-Ганичев");
    historyClient(id1, 8, "name", "Сидорий");
    historyClient(id2, 8, "name", "Геннадиус");
    historyClient(id1, 8, "father_name", "Анатолич");
    historyClient(id2, 8, "father_name", "Василич");

    author = "Менятель №3";

    historyClient(id1, 10, "surname", "Иванов-Сукачёв");
    historyClient(id2, 10, "surname", "Потапов-Сукачёв");
    historyClient(id1, 10, "name", "Сидор-Валирийский");
    historyClient(id2, 10, "name", "Геннадий-Валирийский");
    historyClient(id1, 10, "father_name", "Анатольевич-Валирийского");
    historyClient(id2, 10, "father_name", "Васильевич-Валирийского");

    System.out.println();
    System.out.println("  select '" + stdDate(1) + "'::timestamp as t1   -- date 1");
    System.out.println("       , '" + stdDate(3) + "'::timestamp as t3   -- date 3");
    System.out.println("       , '" + stdDate(5) + "'::timestamp as t5   -- date 5");
    System.out.println("       , '" + stdDate(7) + "'::timestamp as t7   -- date 7");
    System.out.println("       , '" + stdDate(9) + "'::timestamp as t9   -- date 9");
    System.out.println("       , '" + stdDate(11) + "'::timestamp as t11  -- date 11");

    {
      LocalClient client = new LocalClient();
      client.id = "id1";

      client.surname = RND.str(10);
      client.name = RND.str(10);
      client.fatherName = RND.str(10);

      //
      //
      boolean exists = createHistorySelectorForClient().at(dateArray[3]).putTo(client);
      //
      //

      assertThat(exists).isTrue();

      assertThat(client.surname).isEqualTo("Иванов");
      assertThat(client.name).isEqualTo("Сидор");
      assertThat(client.fatherName).isEqualTo("Анатольевич");
    }

    {
      LocalClient client = new LocalClient();
      client.id = "id2";

      client.surname = RND.str(10);
      client.name = RND.str(10);
      client.fatherName = RND.str(10);

      //
      //
      boolean exists = createHistorySelectorForClient().at(dateArray[3]).putTo(client);
      //
      //

      assertThat(exists).isFalse();

      assertThat(client.surname).isNotEqualTo("Иванов");
      assertThat(client.name).isNotEqualTo("Сидор");
      assertThat(client.fatherName).isNotEqualTo("Анатольевич");

      assertThat(client.surname).isNotNull();
      assertThat(client.name).isNotNull();
      assertThat(client.fatherName).isNotNull();
    }

    {
      LocalClient c1 = new LocalClient();
      c1.id = "id1";
      c1.surname = RND.str(10);
      c1.name = RND.str(10);
      c1.fatherName = RND.str(10);

      LocalClient c2 = new LocalClient();
      c2.id = "id2";
      c2.surname = RND.str(10);
      c2.name = RND.str(10);
      c2.fatherName = RND.str(10);

      //
      //
      boolean exists1 = createHistorySelectorForClient().at(dateArray[5]).putTo(c1);
      boolean exists2 = createHistorySelectorForClient().at(dateArray[5]).putTo(c2);
      //
      //

      assertThat(exists1).isTrue();
      assertThat(exists2).isTrue();

      assertThat(c1.surname).isEqualTo("Иванов");
      assertThat(c2.surname).isEqualTo("Потапов");
      assertThat(c1.name).isEqualTo("Сидор");
      assertThat(c2.name).isEqualTo("Геннадий");
      assertThat(c1.fatherName).isEqualTo("Анатольевич");
      assertThat(c2.fatherName).isEqualTo("Васильевич");
    }

    {
      LocalClient c1 = new LocalClient();
      c1.id = "id1";
      c1.surname = RND.str(10);
      c1.name = RND.str(10);
      c1.fatherName = RND.str(10);

      LocalClient c2 = new LocalClient();
      c2.id = "id2";
      c2.surname = RND.str(10);
      c2.name = RND.str(10);
      c2.fatherName = RND.str(10);

      //
      //
      boolean exists1 = createHistorySelectorForClient().at(dateArray[7]).putTo(c1);
      boolean exists2 = createHistorySelectorForClient().at(dateArray[7]).putTo(c2);
      //
      //

      assertThat(exists1).isTrue();
      assertThat(exists2).isTrue();

      assertThat(c1.surname).isEqualTo("Иванов-Ганич");
      assertThat(c2.surname).isEqualTo("Потапов-Ганич");
      assertThat(c1.name).isEqualTo("Сидорус");
      assertThat(c2.name).isEqualTo("Генна");
      assertThat(c1.fatherName).isEqualTo("Анатольевич");
      assertThat(c2.fatherName).isEqualTo("Васильевич");
    }

    {
      LocalClient c1 = new LocalClient();
      c1.id = "id1";
      c1.surname = RND.str(10);
      c1.name = RND.str(10);
      c1.fatherName = RND.str(10);

      LocalClient c2 = new LocalClient();
      c2.id = "id2";
      c2.surname = RND.str(10);
      c2.name = RND.str(10);
      c2.fatherName = RND.str(10);

      //
      //
      boolean exists1 = createHistorySelectorForClient().at(dateArray[9]).putTo(c1);
      boolean exists2 = createHistorySelectorForClient().at(dateArray[9]).putTo(c2);
      //
      //

      assertThat(exists1).isTrue();
      assertThat(exists2).isTrue();

      assertThat(c1.surname).isEqualTo("Иванов-Ганичев");
      assertThat(c2.surname).isEqualTo("Потапов-Ганичев");
      assertThat(c1.name).isEqualTo("Сидорий");
      assertThat(c2.name).isEqualTo("Геннадиус");
      assertThat(c1.fatherName).isEqualTo("Анатолич");
      assertThat(c2.fatherName).isEqualTo("Василич");
    }

    {
      LocalClient c1 = new LocalClient();
      c1.id = "id1";
      c1.surname = RND.str(10);
      c1.name = RND.str(10);
      c1.fatherName = RND.str(10);

      LocalClient c2 = new LocalClient();
      c2.id = "id2";
      c2.surname = RND.str(10);
      c2.name = RND.str(10);
      c2.fatherName = RND.str(10);

      //
      //
      boolean exists1 = createHistorySelectorForClient().at(dateArray[11]).putTo(c1);
      boolean exists2 = createHistorySelectorForClient().at(dateArray[11]).putTo(c2);
      //
      //

      assertThat(exists1).isTrue();
      assertThat(exists2).isTrue();

      assertThat(c1.surname).isEqualTo("Иванов-Сукачёв");
      assertThat(c2.surname).isEqualTo("Потапов-Сукачёв");
      assertThat(c1.name).isEqualTo("Сидор-Валирийский");
      assertThat(c2.name).isEqualTo("Геннадий-Валирийский");
      assertThat(c1.fatherName).isEqualTo("Анатольевич-Валирийского");
      assertThat(c2.fatherName).isEqualTo("Васильевич-Валирийского");
    }

  }
}
