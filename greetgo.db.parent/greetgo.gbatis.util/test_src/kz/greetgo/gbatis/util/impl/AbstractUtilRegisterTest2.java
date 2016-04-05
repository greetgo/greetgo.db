package kz.greetgo.gbatis.util.impl;

import kz.greetgo.gbatis.util.impl.test_sql_source.TestHelper;
import kz.greetgo.gbatis.util.model.ProceduresCaller;
import kz.greetgo.gbatis.util.sqls.SqlSrc;
import kz.greetgo.gbatis.util.test.MyTestBase;
import kz.greetgo.util.db.DbType;
import org.testng.annotations.Test;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.List;

import static kz.greetgo.gbatis.util.impl.BinUtil.*;
import static org.fest.assertions.api.Assertions.assertThat;

public class AbstractUtilRegisterTest2 extends MyTestBase {

  @Override
  protected DbType[] usingDbTypes() {
    return new DbType[]{DbType.PostgreSQL};
    //return new DbType[]{DbType.Oracle};
  }

  @Test(dataProvider = CONNECT_PROVIDER)
  public void listExecutor_nativeCall(Connection con) throws Exception {
    TestHelper th = new TestHelper(con);
    th.prepareTempStorage();

    th.prepareProcedure_sayHelloWorld();
    th.prepareProcedure_sayQuWithStrStr();
    th.prepareProcedure_sayQuWithIntStr();
    th.prepareProcedure_sayQuWithLongLong();
    th.prepareProcedure_sayQuWithTimeTime();

    th.executeSqls(SqlSrc.get(con).sql("stored/executeCommandList_procedures"));

    ByteArrayOutputStream bout = new ByteArrayOutputStream();

    writeStrAsUtf8fromLength(bout, "callProcedure");
    writeStrAsUtf8fromLength(bout, "sayHelloWorld");
    writeInt(bout, 0);//arguments count

    // CALL say_qu_with_str_str
    writeStrAsUtf8fromLength(bout, "callProcedure");
    writeStrAsUtf8fromLength(bout, "say_qu_with_str_str");
    writeInt(bout, 2);//arguments count
    bout.write(1);//argumentTypeId = 1 - str
    writeStrAsUtf8fromLength(bout, "你好世界 - העלא וועלט");
    bout.write(1);//argumentTypeId = 1 - str
    writeStrAsUtf8fromLength(bout, null);

    // CALL say_qu_with_int_str
    writeStrAsUtf8fromLength(bout, "callProcedure");
    writeStrAsUtf8fromLength(bout, "say_qu_with_int_str");
    writeInt(bout, 2);//arguments count
    bout.write(2);//argumentTypeId = 2 - int
    writeInt(bout, -3);//int argument
    bout.write(1);//argumentTypeId = 1 - str
    writeStrAsUtf8fromLength(bout, "До свидания");

    // CALL say_qu_with_long_long
    writeStrAsUtf8fromLength(bout, "callProcedure");
    writeStrAsUtf8fromLength(bout, "say_qu_with_long_long");
    writeInt(bout, 2);//arguments count
    bout.write(3);//argumentTypeId = 3 - long
    writeLong(bout, 78_286_201L);//long argument
    bout.write(3);//argumentTypeId = 3 - long
    writeLong(bout, -201L);//long argument

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    // CALL say_qu_with_time_time
    writeStrAsUtf8fromLength(bout, "callProcedure");
    writeStrAsUtf8fromLength(bout, "say_qu_with_time_time");
    writeInt(bout, 2);//arguments count
    bout.write(4);//argumentTypeId = 4 - time
    writeTime(bout, null);//time argument
    bout.write(4);//argumentTypeId = 4 - time
    writeTime(bout, sdf.parse("1991-05-11 18:00:21.876"));//time argument

    // EXIT
    writeStrAsUtf8fromLength(bout, "exit");

    th.executeCommandList(DatatypeConverter.printBase64Binary(bout.toByteArray()));

    List<String> lines = th.readTempStorage();

    //for (String line : lines) {System.out.println(line);}

    assertThat(lines.get(0)).isEqualTo("I am saying: \"Hello world, 你好世界\"");
    assertThat(lines.get(1)).isEqualTo("I am saying qu with str str: s1 = 你好世界 - העלא וועלט, s2 = <NULL>");
    assertThat(lines.get(2)).isEqualTo("I am saying qu with int str: i1 = -3, s2 = До свидания");
    assertThat(lines.get(3)).isEqualTo("I am saying qu with long long: long1 = 78286201, long2 = -201");
    assertThat(lines.get(4)).isEqualTo("I am saying qu with time time: time1 = <NULL>, time2 = 1991-05-11 18:00:21.876");

  }

  @Test(dataProvider = CONNECT_PROVIDER)
  public void listExecutor_registerCall(Connection con) throws Exception {
    TestHelper th = new TestHelper(con);
    th.prepareTempStorage();

    th.prepareProcedure_sayHelloWorld();
    th.prepareProcedure_sayQuWithStrStr();
    th.prepareProcedure_sayQuWithIntStr();
    th.prepareProcedure_sayQuWithLongLong();
    th.prepareProcedure_sayQuWithTimeTime();

    th.executeSqls(SqlSrc.get(con).sql("stored/executeCommandList_procedures"));

    TestingUtilRegister r = new TestingUtilRegister();
    r.setConnection(con);

    ProceduresCaller pc = r.callProcedures();

    pc.callProcedure("sayHelloWorld");

    pc.callProcedure("say_qu_with_str_str").paramStr("你好世界").paramStr(null);

    pc.callProcedure("say_qu_with_int_str").paramInt(123).paramStr("HI");

    pc.callProcedure("say_qu_with_long_long").paramLong(-987234L).paramLong(123_098_001L);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    pc.callProcedure("say_qu_with_time_time").paramTime(null).paramTime(sdf.parse("1991-05-11 18:00:21.876"));

    pc.run();

    List<String> lines = th.readTempStorage();

    //for (String line : lines) {System.out.println(line);}

    assertThat(lines.get(0)).isEqualTo("I am saying: \"Hello world, 你好世界\"");
    assertThat(lines.get(1)).isEqualTo("I am saying qu with str str: s1 = 你好世界, s2 = <NULL>");
    assertThat(lines.get(2)).isEqualTo("I am saying qu with int str: i1 = 123, s2 = HI");
    assertThat(lines.get(3)).isEqualTo("I am saying qu with long long: long1 = -987234, long2 = 123098001");
    assertThat(lines.get(4)).isEqualTo("I am saying qu with time time: time1 = <NULL>, time2 = 1991-05-11 18:00:21.876");
  }

}
