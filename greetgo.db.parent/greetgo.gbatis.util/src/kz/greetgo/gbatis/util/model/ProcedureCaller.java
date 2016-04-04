package kz.greetgo.gbatis.util.model;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static kz.greetgo.gbatis.util.impl.BinUtil.writeInt;
import static kz.greetgo.gbatis.util.impl.BinUtil.writeStrAsUtf8fromLength;

public class ProcedureCaller {
  public final String procedureName;

  public final List<InputProcedureParam> procedureInputParams = new ArrayList<>();

  ProcedureCaller(String procedureName) {
    if (procedureName == null) throw new IllegalArgumentException("procedureName == null");
    this.procedureName = procedureName;
  }

  public ProcedureCaller paramStr(String value) {
    procedureInputParams.add(new InputProcedureParamStr(value));
    return this;
  }


  public ProcedureCaller paramInt(int value) {
    procedureInputParams.add(new InputProcedureParamInt(value));
    return this;
  }

  public ProcedureCaller paramLong(long value) {
    procedureInputParams.add(new InputProcedureParamLong(value));
    return this;
  }

  public ProcedureCaller paramTime(Date value) {
    procedureInputParams.add(new InputProcedureParamTime(value));
    return this;
  }

  public void writeCall(OutputStream out) throws IOException {
    writeStrAsUtf8fromLength(out, "callProcedure");
    writeStrAsUtf8fromLength(out, procedureName);
    writeInt(out, procedureInputParams.size());

    for (InputProcedureParam procedureInputParam : procedureInputParams) {
      procedureInputParam.writeTo(out);
    }
    
  }
}
