package kz.greetgo.gbatis2.struct.exceptions;

import kz.greetgo.gbatis2.struct.ParsedField;

public class FieldAlreadyExists extends GbatisException {
  public final ParsedField field;
  public final ParsedField alreadyExistsField;

  public FieldAlreadyExists(ParsedField field, ParsedField alreadyExistsField) {
    super(field.name + " in " + field.place.placement() + " already defined in " + alreadyExistsField.place.placement());
    this.field = field;
    this.alreadyExistsField = alreadyExistsField;
  }
}
