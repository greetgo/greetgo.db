package kz.greetgo.db.multi_sessions;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MarkClients extends DataSourcePreparation {
  public MarkClients() throws Exception {}

  public static void main(String[] args) throws Exception {
    new MarkClients().execute();
    System.out.println("Complete");
  }


  private class MarkingThread extends Thread {
    public MarkingThread(String name) {
      super(name);
    }

    @Override
    public void run() {

      try {
        markAllData();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }

    }

  }

  private void execute() throws Exception {

    final List<MarkingThread> threads = new ArrayList<>();
    for (int i = 1; i <= 10; i++) {
      threads.add(new MarkingThread("T" + i));
    }

    for (MarkingThread thread : threads) {
      thread.start();
    }
    for (MarkingThread thread : threads) {
      thread.join();
    }

  }

  private void markAllData() throws Exception {
    try (final Connection connection = ds.getConnection()) {

      int markedCount = 0;

      while (true) {
        final Long clientId = getUnmarkedClientId(connection);
        if (clientId == null) break;

        markClientById(connection, clientId);
        markedCount++;

        if (markedCount % 100 == 0) {
          System.out.println("Marked " + markedCount + " clients from thread " + Thread.currentThread().getName());
        }
      }

      System.out.println("Total marked clients from thread " + Thread.currentThread().getName() + " is " + markedCount);

    }
  }

  private static Long getUnmarkedClientId(Connection connection) throws Exception {
    try (final PreparedStatement ps = connection.prepareStatement("SELECT id FROM client " +
      "WHERE id NOT IN (SELECT client_id FROM client_thread) LIMIT 1")) {

      try (final ResultSet rs = ps.executeQuery()) {
        if (!rs.next()) return null;
        return rs.getLong(1);
      }

    }
  }

  private static void markClientById(Connection connection, long clientId) throws SQLException {
    try (final PreparedStatement ps = connection.prepareStatement("INSERT INTO client_thread " +
      "(client_id, thread_name, marked_at) VALUES (?, ?, ?)")) {

      ps.setLong(1, clientId);
      ps.setString(2, Thread.currentThread().getName());
      ps.setTimestamp(3, new Timestamp(new Date().getTime()));

      if (ps.executeUpdate() < 1) throw new RuntimeException("Cannot insert into client_thread");

    }
  }

}
