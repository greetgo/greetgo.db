package kz.greetgo.gbatis.util.model;

import kz.greetgo.db.ConnectionCallback;
import kz.greetgo.db.Jdbc;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static kz.greetgo.gbatis.util.impl.BinUtil.writeStrAsUtf8fromLength;

public class ProceduresCaller {
  private final Jdbc jdbc;

  private final List<ProcedureCaller> procedureCallerList = new ArrayList<>();

  public ProceduresCaller(Jdbc jdbc) {
    this.jdbc = jdbc;
  }

  public ProcedureCaller callProcedure(String procedureName) {
    return add(new ProcedureCaller(procedureName));
  }

  private ProcedureCaller add(ProcedureCaller procedureCaller) {
    procedureCallerList.add(procedureCaller);
    return procedureCaller;
  }

  public void run() {
    try {
      runInner();
    } catch (Exception e) {
      if (e instanceof RuntimeException) throw (RuntimeException) e;
      throw new RuntimeException(e);
    }
  }

  private void runInner() throws Exception {
    ByteArrayOutputStream bout = new ByteArrayOutputStream();

    for (ProcedureCaller procedureCaller : procedureCallerList) {
      procedureCaller.writeCall(bout);
    }
    writeStrAsUtf8fromLength(bout, "exit");

    final String inputStr = DatatypeConverter.printBase64Binary(bout.toByteArray());

    jdbc.execute(new ConnectionCallback<Void>() {
      @Override
      public Void doInConnection(Connection connection) throws Exception {

        try (CallableStatement cs = connection.prepareCall("{call q_executeCommandList(?)}")) {
          cs.setString(1, inputStr);
          cs.execute();
        }

        return null;
      }
    });
  }
}
