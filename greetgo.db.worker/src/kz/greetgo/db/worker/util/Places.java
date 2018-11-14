package kz.greetgo.db.worker.util;

import java.io.File;

public class Places {
  public static String withDepinjectDir() {
    if (new File("greetgo.nf36.gen.examples/with_depinject").isDirectory()) {
      return "greetgo.nf36.gen.examples/with_depinject";
    }

    if (new File("../with_depinject").isDirectory()) {
      return "../with_depinject";
    }

    throw new RuntimeException("Cannot file example project");
  }
}
