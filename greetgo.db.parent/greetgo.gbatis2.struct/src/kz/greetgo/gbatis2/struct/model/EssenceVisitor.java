package kz.greetgo.gbatis2.struct.model;

import kz.greetgo.gbatis2.struct.model.std.*;

public interface EssenceVisitor<T> {
  T visitFloat(StdFloat stdFloat);

  T visitInt(StdInt stdInt);

  T visitLong(StdLong stdLong);

  T visitStr(StdStr stdStr);

  T visitText(StdText stdText);

  T visitTime(StdTime stdTime);

  T visitEnum(EnumEssence enumEssence);
}
