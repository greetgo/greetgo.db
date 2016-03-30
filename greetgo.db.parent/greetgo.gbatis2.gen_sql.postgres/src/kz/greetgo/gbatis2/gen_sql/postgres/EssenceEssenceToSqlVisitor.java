package kz.greetgo.gbatis2.gen_sql.postgres;

import kz.greetgo.gbatis2.struct.model.EnumEssence;
import kz.greetgo.gbatis2.struct.model.EssenceVisitor;
import kz.greetgo.gbatis2.struct.model.std.*;

public class EssenceEssenceToSqlVisitor implements EssenceVisitor<String> {

  @Override
  public String visitFloat(StdFloat stdFloat) {
    return "numeric(20,4)";
  }

  @Override
  public String visitInt(StdInt stdInt) {
    return "int";
  }

  @Override
  public String visitLong(StdLong stdLong) {
    return "bigint";
  }

  @Override
  public String visitStr(StdStr stdStr) {
    return "varchar(" + stdStr.length + ")";
  }

  @Override
  public String visitText(StdText stdText) {
    return "text";
  }

  @Override
  public String visitTime(StdTime stdTime) {
    return "timestamp without timezone";
  }

  @Override
  public String visitEnum(EnumEssence enumEssence) {
    return "varchar(255)";
  }
}
