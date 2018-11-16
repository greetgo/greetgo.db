package kz.greetgo.db.nf36.util;

import kz.greetgo.db.ConnectionCallback;
import kz.greetgo.db.worker.connector.DatabaseConnector;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import static kz.greetgo.db.worker.connector.DatabaseConnectorBuilder.newDatabaseConnectorBuilder;
import static kz.greetgo.db.worker.utils.DbUtils.extractSqlException;

public class ParentDbTest {

  private DatabaseConnector connector = null;

  protected DatabaseConnector connector() {
    if (connector != null) {
      return connector;
    }

    return connector = newDatabaseConnectorBuilder()
        .setDbType(getClass().getAnnotation(Use.class).value())
        .setDbName(System.getProperty("user.name") + "_db_security2")
        .setDbUser(System.getProperty("user.name") + "_db_security2")
        .setDbPassword("111")
        .build()
        ;
  }

  protected void exec(String sql, SqlErrorType... types) {
    try {

      connector().jdbc().execute((ConnectionCallback<Void>) con -> {
        try (Statement statement = con.createStatement()) {
          statement.execute(sql);
        }
        System.out.println("OK SQL Exec: " + sql.replace('\n', ' ').trim());
        return null;
      });

    } catch (RuntimeException e) {

      SQLException sqlException = extractSqlException(e);

      if (sqlException == null) {
        throw e;
      }

      SqlError sqlError = new SqlError(sqlException);

      for (SqlErrorType type : types) {
        if (type == sqlError.type) {
          return;
        }
      }

      throw sqlError;
    }
  }

  @SuppressWarnings("UnusedReturnValue")
  protected int execParams(String sql, Object... params) {
    return connector().jdbc().execute(con -> {
      try (PreparedStatement ps = con.prepareStatement(sql)) {
        int index = 1;
        for (Object param : params) {
          ps.setObject(index++, param);
        }
        int ret = ps.executeUpdate();
        System.out.println("OK SQL Exec: " + sql.replace('\n', ' ').trim());
        System.out.println("     Params: " + Arrays.toString(params));
        return ret;
      }
    });
  }

}
