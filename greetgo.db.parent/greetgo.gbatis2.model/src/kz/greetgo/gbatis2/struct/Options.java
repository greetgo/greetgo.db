package kz.greetgo.gbatis2.struct;

import kz.greetgo.gbatis2.struct.exceptions.OptionAlreadyDefined;
import kz.greetgo.gbatis2.struct.exceptions.OptionIsNotDefined;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Options {

  private final Map<String, Option> storage = new HashMap<>();

  public void parseLine(String key, String value, Place place) {
    Option option = new Option(key, value, place);
    Option alreadyDefined = storage.get(key);
    if (alreadyDefined != null) {
      if (Objects.equals(option.value, alreadyDefined.value)) return;
      throw new OptionAlreadyDefined(option, alreadyDefined);
    }

    storage.put(option.key, option);
  }

  private String getStrOrThrow(String key) {
    Option option = storage.get(key);
    if (option == null) throw new OptionIsNotDefined(key);
    return option.value;
  }

  public String identification() {
    return getStrOrThrow("identification");
  }

  public String tablePrefixId() {
    return getStrOrThrow("tablePrefixId");
  }

  public String tablePrefixMemory() {
    return getStrOrThrow("tablePrefixMemory");
  }

  public String tablePrefixOperative() {
    return getStrOrThrow("tablePrefixOperative");
  }

  public String concatenationSuffix() {
    return getStrOrThrow("concatenationSuffix");
  }
}
