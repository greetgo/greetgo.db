package kz.greetgo.gbatis.util.model;

import kz.greetgo.gbatis.util.impl.BinUtil;

import java.io.IOException;
import java.io.OutputStream;

import static kz.greetgo.gbatis.util.impl.BinUtil.writeLong;

public class InputProcedureParamLong extends InputProcedureParam {
  public final long value;

  public InputProcedureParamLong(long value) {
    this.value = value;
  }

  @Override
  public void writeTo(OutputStream out) throws IOException {
    out.write(3);//argumentTypeId = 3 - long
    writeLong(out, value);
  }
}
