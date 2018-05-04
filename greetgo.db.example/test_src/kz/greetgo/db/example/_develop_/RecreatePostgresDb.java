package kz.greetgo.db.example._develop_;

import kz.greetgo.db.worker.DbWorker;

public class RecreatePostgresDb {
  public static void main(String[] args) throws Exception {
    new DbWorker(PostgresDbConfig.instance()).recreateDb();
  }

}
