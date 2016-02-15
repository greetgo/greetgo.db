package kz.greetgo.gbatis.util.test;

import kz.greetgo.test.db_providers.TestBase;
import kz.greetgo.util.db.DbType;

import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;

public abstract class MyTestBase extends TestBase {
  
  protected static final String CONNECT_PROVIDER = "connectProvider";
  
  @DataProvider
  protected Object[][] connectProvider() throws Exception {
    return super.connectProvider();
  }
  
  @AfterClass
  protected void tearDownClass() throws Exception {
    cleanAll();
  }
  
  @Override
  protected DbType[] usingDbTypes() {
    return DbType.values();//ALL
  }
  
  @Override
  protected String dbSchema() {
    return "gbatis_util";
  }
}
