package kz.greetgo.gbatis.gen;

import kz.greetgo.gbatis.model.*;

import java.io.PrintWriter;

import static kz.greetgo.gbatis.gen.CodeUtil.*;

public class RequestPrinter {

  public static void print(PrintWriter out, String requestVarName, Request request, int spaces) {
    String s1 = spaces(spaces);
    String s2 = spaces(spaces + 2);

    out.println(s1 + requestVarName + ".sql = " + strValueToCode(request.sql) + ";");
    if (request.type == null) {
      out.println(s1 + requestVarName + ".type = null;");
    } else {
      out.println(s1 + requestVarName + ".type = " + enumValueToCode(RequestType.class, request.type) + ";");
    }

    for (WithView withView : request.withList) {
      out.println(s1 + "{");
      out.println(s2 + WithView.class.getName() + " v = new " + WithView.class.getName() + "();");
      out.println(s2 + "v.table = " + strValueToCode(withView.table) + ";");
      out.println(s2 + "v.view = " + strValueToCode(withView.view) + ";");
      for (String field : withView.fields) {
        out.println(s2 + "v.fields.add(" + strValueToCode(field) + ");");
      }
      out.println(s2 + requestVarName + ".withList.add(v);");
      out.println(s1 + "}");
    }

    for (Param param : request.paramList) {
      out.println(s1 + requestVarName + ".paramList.add(new " + Param.class.getName() + "("
          + classValueToCode(param.type) + ", " + strValueToCode(param.name) + "));"
      );
    }

    out.println(s1 + requestVarName + ".result.type = " + enumValueToCode(ResultType.class, request.result.type) + ";");
    out.println(s1 + requestVarName + ".result.resultDataClass = " + classValueToCode(request.result.resultDataClass) + ";");
    out.println(s1 + requestVarName + ".result.mapKeyField = " + strValueToCode(request.result.mapKeyField) + ";");
    out.println(s1 + requestVarName + ".result.mapKeyClass = " + classValueToCode(request.result.mapKeyClass) + ";");
  }

}
