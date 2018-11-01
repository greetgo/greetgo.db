package kz.greetgo.db.nf36.bridges;

import java.util.function.Function;

class DataField {
  final String nf6TableName;
  final String fieldName;

  public DataField(String nf6TableName, String fieldName) {
    this.nf6TableName = nf6TableName;
    this.fieldName = realFieldName = fieldName;
  }

  public String fieldName() {
    return realFieldName;
  }

  private String realFieldName;

  public void applyConverter(Function<String, String> aliasConverter) {
    realFieldName = aliasConverter.apply(fieldName);
  }
}
