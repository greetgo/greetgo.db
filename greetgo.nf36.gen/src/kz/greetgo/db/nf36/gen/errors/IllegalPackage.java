package kz.greetgo.db.nf36.gen.errors;

public class IllegalPackage extends RuntimeException {
  public IllegalPackage(String basePackageName, String packageName) {
    super("Package " + packageName + " must be in " + basePackageName);
  }
}
