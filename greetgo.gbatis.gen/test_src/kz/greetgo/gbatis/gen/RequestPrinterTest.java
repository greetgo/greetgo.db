package kz.greetgo.gbatis.gen;

import kz.greetgo.gbatis.model.*;
import org.testng.annotations.Test;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

public class RequestPrinterTest {

  @Test
  public void print() throws Exception {
    CharArrayWriter writer = new CharArrayWriter();

    PrintWriter x = new PrintWriter(writer);

    Request r = new Request();
    r.sql = "select 1 from dual";
    r.type = RequestType.Sele;
    r.withList.add(new WithView("t_call", "v_call", "client", "contract", "surname"));
    r.withList.add(new WithView("t_back", "v_back", "surname", "name", "patronymic"));
    r.paramList.add(new Param(String.class, "clientSurname"));
    r.paramList.add(new Param(Integer.class, "age"));

    r.result.type = ResultType.MAP;
    r.result.resultDataClass = RequestPrinterTest.class;
    r.result.mapKeyField = "mapKeyField 1";
    r.result.mapKeyClass = Integer.class;

    RequestPrinter.print(x, "asd", r, 2);

    System.out.println(writer.toString());
  }
}