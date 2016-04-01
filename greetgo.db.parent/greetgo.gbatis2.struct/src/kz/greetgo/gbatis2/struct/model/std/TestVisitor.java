package kz.greetgo.gbatis2.struct.model.std;

import kz.greetgo.gbatis2.struct.model.EnumEssence;
import kz.greetgo.gbatis2.struct.model.EssenceVisitor;

import java.util.ArrayList;
import java.util.List;

public class TestVisitor implements EssenceVisitor<Void> {

  public final List<Object> visits = new ArrayList<>();

  @Override
  public Void visitFloat(StdFloat stdFloat) {
    visits.add(stdFloat);
    return null;
  }

  @Override
  public Void visitInt(StdInt stdInt) {
    visits.add(stdInt);
    return null;
  }

  @Override
  public Void visitLong(StdLong stdLong) {
    visits.add(stdLong);
    return null;
  }

  @Override
  public Void visitStr(StdStr stdStr) {
    visits.add(stdStr);
    return null;
  }

  @Override
  public Void visitText(StdText stdText) {
    visits.add(stdText);
    return null;
  }

  @Override
  public Void visitTime(StdTime stdTime) {
    visits.add(stdTime);
    return null;
  }

  @Override
  public Void visitEnum(EnumEssence enumEssence) {
    visits.add(enumEssence);
    return null;
  }
}
