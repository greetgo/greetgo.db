package kz.greetgo.db.example.utils;

import java.sql.SQLException;

public class TableAlreadyExists extends RuntimeException {
  public TableAlreadyExists(SQLException e) {
    super(e);
  }
}
