package kz.greetgo.db.nf36.gen.example.util;

import java.io.File;

public class Places {
  public static String exampleDir() {
    if (new File("greetgo.nf36.gen.example").isDirectory()) return "greetgo.nf36.gen.example";
    if (new File("../greetgo.nf36.gen.example").isDirectory()) return "../greetgo.nf36.gen.example";
    throw new RuntimeException("Cannot file example project");
  }
}
