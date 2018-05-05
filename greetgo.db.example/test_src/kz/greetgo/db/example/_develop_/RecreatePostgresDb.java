package kz.greetgo.db.example._develop_;

import kz.greetgo.db.example.configs.PostgresDbConfig;
import kz.greetgo.db.worker.DbWorkerPostgres;

public class RecreatePostgresDb {
  public static void main(String[] args) throws Exception {
    new DbWorkerPostgres(PostgresDbConfig.instance()).recreateDb();
    System.out.println("RecreatePostgresDb - Ok");
  }
}
