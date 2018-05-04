package kz.greetgo.gbatis.util.model;

import java.io.IOException;
import java.io.OutputStream;

import static kz.greetgo.gbatis.util.impl.BinUtil.writeInt;

public class InputProcedureParamInt extends InputProcedureParam {
  public final int value;

  public InputProcedureParamInt(int value) {
    this.value = value;
  }

  @Override
  public void writeTo(OutputStream out) throws IOException {
    out.write(2);//argumentTypeId = 2
    writeInt(out, value);
  }
}
