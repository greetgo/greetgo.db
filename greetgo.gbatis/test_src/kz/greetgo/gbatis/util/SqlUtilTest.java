package kz.greetgo.gbatis.util;

import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import static org.fest.assertions.api.Assertions.assertThat;

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

  @Test
  public void fromSql_Timestamp() throws Exception {

    final Date date = new Date();

    Object one = SqlUtil.fromSql(new Timestamp(date.getTime()));

    assertThat(one).hasSameClassAs(date);
    assertThat(one.equals(date)).isTrue();
  }

  @Test
  public void asLong_Timestamp() throws Exception {

    final Date date = new Date();

    Object one = SqlUtil.asLong(new Timestamp(date.getTime()));

    assertThat(one).isNull();
  }

  @Test
  public void asLong_null() throws Exception {
    assertThat(SqlUtil.asLong(null)).isNull();
  }

  @Test
  public void asLong_long() throws Exception {

    Object one = SqlUtil.asLong(123L);

    assertThat(one).isEqualTo(123L);
  }

  @Test
  public void asLong_int() throws Exception {

    Object one = SqlUtil.asLong(123);

    assertThat(one).isEqualTo(123L);
  }

  @Test
  public void asLong_BigDecimal() throws Exception {

    Object one = SqlUtil.asLong(new BigDecimal("123"));

    assertThat(one).isEqualTo(123L);
  }
}
