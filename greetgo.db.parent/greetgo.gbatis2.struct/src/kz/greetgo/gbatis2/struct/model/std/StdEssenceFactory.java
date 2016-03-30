package kz.greetgo.gbatis2.struct.model.std;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StdEssenceFactory {

  private static final Pattern STR = Pattern.compile("str(\\d+)");

  public StdEssence parse(String essenceStr) {
    if ("int".equals(essenceStr)) return StdInt.INSTANCE;
    if ("long".equals(essenceStr)) return StdLong.INSTANCE;
    if ("time".equals(essenceStr)) return StdTime.INSTANCE;
    if ("float".equals(essenceStr)) return StdFloat.INSTANCE;
    if ("text".equals(essenceStr)) return StdText.INSTANCE;

    {
      Matcher matcher = STR.matcher(essenceStr);
      if (matcher.matches()) {
        return new StdStr(Integer.parseInt(matcher.group(1)));
      }
    }

    return null;
  }
}
