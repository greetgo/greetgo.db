package kz.greetgo.gbatis.util.model;

import java.io.IOException;
import java.io.OutputStream;

import static kz.greetgo.gbatis.util.impl.BinUtil.writeStrAsUtf8fromLength;

public class InputProcedureParamStr extends InputProcedureParam {
  public final String value;

  public InputProcedureParamStr(String value) {
    this.value = value;
  }

  @Override
  public void writeTo(OutputStream out) throws IOException {
    out.write(1);//argumentTypeId = 1
    writeStrAsUtf8fromLength(out, value);
  }
}
