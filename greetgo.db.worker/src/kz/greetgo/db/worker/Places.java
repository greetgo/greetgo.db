package kz.greetgo.db.worker;

import java.io.File;

public class Places {
  public static String nf36ExampleDir() {
    if (new File("greetgo.nf36.gen.example").isDirectory()) return "greetgo.nf36.gen.example";
    if (new File("../greetgo.nf36.gen.example").isDirectory()) return "../greetgo.nf36.gen.example";
    throw new RuntimeException("Cannot file example project");
  }
}
