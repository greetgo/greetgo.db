package kz.greetgo.gbatis2.struct;

import java.util.HashMap;
import java.util.Map;

public class DbStruct {

  final Options options = new Options();

  final Aliases aliases = new Aliases();

  final Enums enums = new Enums();

  final Map<String, ParsedEssence> typeMap = new HashMap<>();

}
