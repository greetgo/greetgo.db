package kz.greetgo.gbatis.util.model;

import java.io.IOException;
import java.io.OutputStream;

public abstract class InputProcedureParam {
  public abstract void writeTo(OutputStream out) throws IOException;
}
