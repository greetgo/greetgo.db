package kz.greetgo.db.multi_sessions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class PrepareData extends DataSourcePreparation {

  public PrepareData() throws Exception {
  }

  public static void main(String[] args) throws Exception {

    new PrepareData().execute();

    System.out.println("Complete");
  }

  private void execute() throws SQLException {

    try (final Connection connection = ds.getConnection()) {

      try (final PreparedStatement ps = connection.prepareStatement("INSERT INTO client " +
        "(id, surname, name, birth_date) VALUES (nextval('s_client'), ?, ?, ?)")) {

        for (int i = 0; i < 200; i++) {
          ps.setString(1, "surname " + i);
          ps.setString(2, "name " + i);
          ps.setTimestamp(3, new Timestamp(new Date().getTime()));
          ps.addBatch();
        }

        ps.executeBatch();

      }

    }
  }


}
