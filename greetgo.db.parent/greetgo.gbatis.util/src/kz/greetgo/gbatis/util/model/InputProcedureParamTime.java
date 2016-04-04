package kz.greetgo.gbatis.util.model;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import static kz.greetgo.gbatis.util.impl.BinUtil.writeTime;

public class InputProcedureParamTime extends InputProcedureParam {
  public final Date value;

  public InputProcedureParamTime(Date value) {
    this.value = value;
  }

  @Override
  public void writeTo(OutputStream out) throws IOException {
    out.write(4);//argumentTypeId = 4
    writeTime(out, value);
  }
}
