package kz.greetgo.gbatis.gen;

import java.io.File;

public interface GenResult {
  File javaFile();

  File hashFile();

  String implClassName();
}
