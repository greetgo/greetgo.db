package kz.greetgo.gbatis.util;

import static org.fest.assertions.api.Assertions.assertThat;

import org.testng.annotations.Test;

public class SqlUtilTest {
  @Test
  public void fromSql_long_null() throws Exception {
    Long res = SqlUtil.fromSql(null, Long.TYPE);
    
    assertThat(res).isEqualTo(0);
  }
  
  @Test
  public void fromSql_boolean_null() throws Exception {
    Boolean res = SqlUtil.fromSql(null, Boolean.TYPE);
    
    assertThat(res).isEqualTo(false);
  }
  
  @Test
  public void checkAbstractEnums() throws Exception {
    Object one = SqlUtil.forSql(ForTestSqlUtil.ONE);
    assertThat(one).isInstanceOf(String.class);
    assertThat(one).isEqualTo(ForTestSqlUtil.ONE.name());
  }
}
