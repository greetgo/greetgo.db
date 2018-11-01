package kz.greetgo.db.nf36.bridges;

import java.util.function.Function;

class DataField {
  final String nf6TableName;
  final String fieldName;

  public DataField(String nf6TableName, String fieldName) {
    this.nf6TableName = nf6TableName;
    this.fieldName = alias = fieldName;
  }

  public String alias() {
    return alias;
  }

  private String alias;

  public void applyConverter(Function<String, String> aliasConverter) {
    alias = aliasConverter.apply(fieldName);
  }
}
